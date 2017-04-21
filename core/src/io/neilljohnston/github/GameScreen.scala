package io.neilljohnston.github

import com.badlogic.gdx.{Gdx, ScreenAdapter}
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.maps.tiled._
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.{FitViewport, ScreenViewport}
import io.neilljohnston.github.IWannaJump.Ps
import exmath.{Smooth, SplitGridSpacialMap}

class GameScreen(game: IWannaJump) extends ScreenAdapter with Smooth {
    // Camera/screen resources
    val camera = new OrthographicCamera()
    camera.setToOrtho(false, 1280, 720)
    val viewport = new FitViewport(1280, 720, camera)
    val stage = new Stage(new ScreenViewport())

    // Load map
    val fullMap: TiledMap = new TmxMapLoader().load("test.tmx")
    val fullMapRenderer: OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(fullMap, Ps / 16, game.batch)
    fullMapRenderer.setView(camera)

    // Get tiles from map
    val fullTiles: TiledMapTileLayer = fullMap.getLayers.get("tiles").asInstanceOf[TiledMapTileLayer]
    val fullTilesSpacial = new SplitGridSpacialMap(2 * Ps, 2 * Ps, 40, 40)

    // Get the sprites from the map
    val fullSprites: SplitGridSpacialMap = new SplitGridSpacialMap(2 * Ps, 2 * Ps, 20, 20)

    val player = new Player
    player.x = 600
    player.y = 700

    /**
      * Render entire screen and make logic calls.
      * What happens, in order:
      * 1. Gdx.gl calls to clear screen,
      * 2. Game logic is performed (step/update/whatever),
      * 3. game.batch renders the game,
      * 4. game.batch renders the GUI,
      * 5.
      * @param delta    How much time has passed (s) since the last call to render
      */
    override def render(delta: Float): Unit = {
        // Clear the screen
        Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Update the game logic
        for(y <- -1 to 1; x <- -1 to 1) {
            val tx = (player.x / Ps).toInt + x
            val ty = (player.y / Ps).toInt + y
            try {
                // Get the player to collide with a tile depending on the tile "type" tag
                val t = fullTiles.getCell(tx, ty).getTile
                val aabb = t.getProperties.get("type").asInstanceOf[String]
                player queueCollision AABB.factory(tx * Ps, ty * Ps, aabb)
            }
            catch {
                case e: NullPointerException => //
            }
        }
        player.step(delta)

        // Update the camera view to track the player, smoothly
        camera.position.set(
            (player.x - camera.position.x) * 0.25f + camera.position.x,
            (player.y - camera.position.y) * 0.125f + camera.position.y, 0)
        camera.update()
        fullMapRenderer.setView(
            camera.combined,
            camera.position.x - 2 * camera.viewportWidth, camera.position.y - 2 * camera.viewportHeight,
            4 * camera.viewportWidth, 4 * camera.viewportHeight)

        // Render main game from batch
        viewport.apply()
        game.batch.setProjectionMatrix(camera.combined)
        game.batch.begin()

        // Tile map rendering
        def renderLayer(name: String) =
            fullMapRenderer.renderTileLayer(fullMap.getLayers.get(name).asInstanceOf[TiledMapTileLayer])
        renderLayer("bg")
        renderLayer("tiles")

        player.draw(game.batch)

        game.batch.end()

        /*
        // Render stage (GUI) after batch ends
        stage.getViewport apply true
        stage act delta
        stage draw()
        */
    }

    override def resize(width: Int, height: Int): Unit = {
        viewport.update(width, height, true)
        stage.getViewport update(width, height, true)
    }

    /**
      * Manual garbage collecting.
      */
    override def dispose(): Unit = {
        fullMap.dispose()
    }
}
