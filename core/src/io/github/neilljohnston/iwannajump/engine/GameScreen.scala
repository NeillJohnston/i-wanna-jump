package io.github.neilljohnston.iwannajump.engine

import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.maps.MapProperties
import com.badlogic.gdx.maps.tiled._
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.{Gdx, ScreenAdapter}
import io.github.neilljohnston.iwannajump.elements.Sprite
import io.github.neilljohnston.iwannajump.elements.Sprite.Solid
import io.github.neilljohnston.iwannajump.exmath.Smooth
import io.github.neilljohnston.iwannajump.engine.IWannaJump.Ps

/**
  * A screen that plays the game.
  * @param game     IWannaJump base object
  * @param mapFile  Path of the Tiled map
  */
abstract class GameScreen(game: IWannaJump, val mapFile: String) extends ScreenAdapter with Smooth {
    /**
      * Name of the Tiled layer that will contain tiles.
      */
    // TODO see if this val and the other 2 belong in a game manager class.
    val tileLayerName: String = "tiles"

    /**
      * Name of the Tiled layer that will contain sprites.
      */
    val spriteLayerName: String = "sprites"

    /**
      * Name of the Tiled layer that will contain background.
      */
    val bgLayerName: String = "bg"

    /**
      * A list of the layers to draw, in ascending order of importance.
      * The spriteLayerName should be included in the correct order.
      */
    val layerPriority: List[String] = List(bgLayerName, tileLayerName, spriteLayerName)

    /**
      * Screen width.
      */
    val width = 1280

    /**
      * Screen height.
      */
    val height = 720

    /**
      * The orthographic camera that will draw the screen.
      */
    val camera = new OrthographicCamera()
    camera.setToOrtho(false, width, height)

    /**
      * The viewport for the screen.
      */
    val viewport = new FitViewport(width, height, camera)

    /**
      * Map loaded from a Tiled map from the location mapFile.
      */
    val fullMap: TiledMap = new TmxMapLoader().load(mapFile)

    /**
      * Create a renderer that will render an orthogonal Tiled map.
      */
    val fullMapRenderer: OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(fullMap, Ps / 16, game.batch)
    fullMapRenderer.setView(camera)

    /**
      * Layer that contains tiles.
      */
    val fullTiles: TiledMapTileLayer = fullMap.getLayers.get(tileLayerName).asInstanceOf[TiledMapTileLayer]

    /**
      * A BroadMap that contains all of the sprites in the map.
      */
    val fullSprites: BroadMap[Sprite] = new SplitGridSpacialMap[Sprite](4 * Ps, 4 * Ps, 16, 16, 0, 0)
    val layerSprites: TiledMapTileLayer = fullMap.getLayers.get(spriteLayerName).asInstanceOf[TiledMapTileLayer]
    // Add all the sprites from layerSprites to fullSprites
    for(y <- 0 until layerSprites.getHeight; x <- 0 until layerSprites.getWidth) {
        try {
            val cell = layerSprites.getCell(x, y)
            val spriteType = cell.getTile.getProperties.get("type").asInstanceOf[String]
            fullSprites add spriteFactory(spriteType, x * Ps, y * Ps, cell.getTile.getProperties)
        }
        catch {
            case e: NullPointerException => // Do nothing, not our problem :D
        }
    }

    /**
      * Updates the sprites, checks for collisions, and calls their respective step function.
      * Override to extend collision detection (recommended).
      * @param delta    Time passed since last step (seconds)
      */
    def stepSprites(delta: Float): Unit = {
        // Update sprites
        for(s <- fullSprites) {
            s match {
                case x: Solid =>
                    x.searchTiles(fullTiles)
                    for(o <- fullSprites.scan(s))
                        x queueCollision o
            }
            s.step(delta)
        }
    }

    /**
      * Keeps the camera boxed around all the sprites.
      * Override to provide camera tracking technology (recommended).
      * @param delta    Time passed since last step (seconds)
      */
    def trackCamera(delta: Float): Unit = {
        val bounds = new Rectangle(camera.position.x, camera.position.y, 0, 0)
        var newCamX = camera.position.x
        var newCamY = camera.position.y
        var newCamZoom = camera.zoom
        for(s <- fullSprites) {
            bounds.merge(s)
            newCamX = bounds.x + bounds.width / 2
            newCamY = bounds.y + bounds.height / 2
            newCamZoom += delta * smoothTo(camera.zoom,
                4 * math.max(bounds.width / camera.viewportWidth, bounds.height / camera.viewportHeight),
                4.0f)
        }
        camera.position.x = newCamX
        camera.position.y = newCamY
        camera.zoom = newCamZoom
    }

    /**
      * Stops the camera from going out of screen bounds.
      * Override to limit the camera to something over than screen bounds.
      * @param delta    Time passed since last step (seconds)
      */
    def limitCamera(delta: Float): Unit = {
        if(camera.position.x - camera.viewportWidth / 2 < 0)
            camera.position.x = camera.viewportWidth / 2
        if(camera.position.x + camera.viewportWidth / 2 > fullTiles.getWidth * Ps)
            camera.position.x = fullTiles.getWidth * Ps - camera.viewportWidth / 2
        if(camera.position.y - camera.viewportHeight / 2 < 0)
            camera.position.y = camera.viewportHeight / 2
        if(camera.position.y + camera.viewportHeight / 2 > fullTiles.getHeight * Ps)
            camera.position.y = fullTiles.getHeight * Ps - camera.viewportHeight / 2
    }

    /**
      * Draws in the order specified by layerPriority.
      * Override to change the way layers are drawn (not recommended).
      */
    def drawLayers(): Unit = {
        // Helper method to draw an entire tile layer.
        def drawLayer(name: String) =
            fullMapRenderer.renderTileLayer(fullMap.getLayers.get(name).asInstanceOf[TiledMapTileLayer])

        // Draw all the layers.
        for(l <- layerPriority) l match {
            case `spriteLayerName` =>
                for(s <- fullSprites)
                    s.draw(game.batch)
            case _ => drawLayer(l)
        }
    }

    /**
      * Constructs sprites for the map. Each call to spriteFactory should yield a new sprite in the correct x, y
      * location, using the specified spriteType string and MapProperties object.
      * Must be overridden!
      * @param spriteType       String representing what type of sprite to make
      * @param x                X-coordinate of the cell the sprite originates from
      * @param y                Y-coordinate
      * @param spriteProperties Properties of the sprite
      * @return A new sprite based on spriteType and spriteProperties
      */
    def spriteFactory(spriteType: String, x: Float, y: Float, spriteProperties: MapProperties): Sprite

    /**
      * Render entire screen and make logic calls.
      * Override to tweak the rendering/game logic process a bit. Note that this function basically just calls other
      * auxiliary functions (stepSprites, trackCamera, limitCamera, drawLayers).
      * In case you want to add functionality to the render function without overriding it (render is an annoyingly
      * complicated function), there are two auxiliary "plugin" functions (stepExtra and drawExtra) that are called
      * within render. Override these to get extra functionality without overriding render entirely.
      * The final order that events and method calls happen in:
      *     - clear screen
      *     - stepSprites
      *     - stepExtra
      *     - fullSprites.update
      *     - trackCamera
      *     - limitCamera
      *     - camera.update
      *     - fullMapRenderer.setView
      *     - drawLayers (amid some calls to game.batch)
      *     - drawExtra
      * @param delta    How much time has passed (s) since the last call to render
      */
    override def render(delta: Float): Unit = {
        // Clear the screen to black.
        Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Run game logic
        stepSprites(delta)

        // Perform extra game logic if overridden
        stepExtra(delta)

        // Update the sprites map
        fullSprites.update()

        // Perform camera ops: track, then stop
        trackCamera(delta)
        limitCamera(delta)

        // Update the camera and map renderer view, set to render a large area around the camera
        camera.update()
        fullMapRenderer.setView(
            camera.combined,
            camera.position.x - camera.zoom * camera.viewportWidth,
            camera.position.y - camera.zoom * camera.viewportHeight,
            2 * camera.zoom * camera.viewportWidth,
            2 * camera.zoom * camera.viewportHeight)

        // Draw screen from batch
        viewport.apply()
        game.batch.setProjectionMatrix(camera.combined)
        game.batch.begin()
        drawLayers()
        game.batch.end()

        // Draw extra if overridden
        drawExtra()
    }

    /**
      * Updates the viewports when the screen is resized.
      * @param width    New screen width
      * @param height   New screen height
      */
    override def resize(width: Int, height: Int): Unit = {
        viewport.update(width, height, true)
    }

    /**
      * Manual garbage collecting. Just dispose of the fullMap, it should be the container of all resources.
      * Override if more resources were created than just fullMap (e.g., GUI images).
      */
    override def dispose(): Unit = fullMap.dispose()

    /**
      * An unimplemented "plugin" method that is called right after stepSprites in render.
      * Override to get any use out of this at all.
      * @param delta    Time since last step (seconds)
      */
    def stepExtra(delta: Float): Unit = {}

    /**
      * An unimplemented "plugin" method that is called right after drawLayers in render.
      * Override to get any use out of this at all.
      */
    def drawExtra(): Unit = {}
}
