package io.neilljohnston.github

import com.badlogic.gdx.{Gdx, ScreenAdapter}
import com.badlogic.gdx.graphics.{GL20, OrthographicCamera}
import com.badlogic.gdx.maps.tiled._
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.{FitViewport, ScreenViewport}
import io.neilljohnston.github.IWannaJump.Ps
import exmath.Smooth

class GameScreen(game: IWannaJump, val mapFile: String) extends ScreenAdapter with Smooth {
    // Camera/screen resources
    val camera = new OrthographicCamera()
    camera.setToOrtho(false, 1280, 720)
    val viewport = new FitViewport(1280, 720, camera)
    val stage = new Stage(new ScreenViewport())

    // Load map
    val fullMap: TiledMap = new TmxMapLoader().load(mapFile)
    val fullMapRenderer: OrthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(fullMap, Ps / 16, game.batch)
    fullMapRenderer.setView(camera)

    // Get tiles from map
    val fullTiles: TiledMapTileLayer = fullMap.getLayers.get("tiles").asInstanceOf[TiledMapTileLayer]

    val player = new Player
    player.x = Ps * 32 / 2
    player.y = Ps * 32 / 2

    /**
      * Render entire screen and make logic calls.
      * @param delta    How much time has passed (s) since the last call to render
      */
    override def render(delta: Float): Unit = {
        // Clear the screen to black
        Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        // Update the game logic
        player.searchTiles(fullTiles)
        player.step(delta)

        // Update the camera view to track the player, smoothly
        camera.position.set(
            (player.x - camera.position.x) * 0.25f + camera.position.x,
            (player.y - camera.position.y) * 0.25f + camera.position.y, 0)

        // Don't allow the camera to go out of bounds
        if(camera.position.x - camera.viewportWidth / 2 < 0)
            camera.position.x = camera.viewportWidth / 2
        if(camera.position.x + camera.viewportWidth / 2 > fullTiles.getWidth * Ps)
            camera.position.x = fullTiles.getWidth * Ps - camera.viewportWidth / 2
        if(camera.position.y - camera.viewportHeight / 2 < 0)
            camera.position.y = camera.viewportHeight / 2
        if(camera.position.y + camera.viewportHeight / 2 > fullTiles.getHeight * Ps)
            camera.position.y = fullTiles.getHeight * Ps - camera.viewportHeight / 2

        // Update the camera and map renderer view, set to render a large area around the camera
        camera.update()
        fullMapRenderer.setView(
            camera.combined,
            camera.position.x - 2 * camera.viewportWidth, camera.position.y - 2 * camera.viewportHeight,
            4 * camera.viewportWidth, 4 * camera.viewportHeight)

        // Render main game from batch
        viewport.apply()
        game.batch.setProjectionMatrix(camera.combined)
        game.batch.begin()

        // Tiled map retrieval/drawing
        def getLayer(name: String) =
            fullMap.getLayers.get(name).asInstanceOf[TiledMapTileLayer]
        def drawLayer(name: String) =
            fullMapRenderer.renderTileLayer(getLayer(name))

        // Tile map rendering
        try {
            drawLayer("bg")
            drawLayer("tiles")
            drawLayer("cosmetic")
        }
        catch {
            case e: NullPointerException =>
        }

        player.draw(game.batch)

        game.batch.end()

        /*
        // Render stage (GUI) after batch ends
        stage.getViewport apply true
        stage act delta
        stage draw()
        */
    }

    /**
      * Updates the viewports when the screen is resized.
      * @param width    New screen width
      * @param height   New screen height
      */
    override def resize(width: Int, height: Int): Unit = {
        viewport.update(width, height, true)
        stage.getViewport update(width, height, true)
    }

    /**
      * Manual garbage collecting.
      */
    override def dispose(): Unit = fullMap.dispose()
}
