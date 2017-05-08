package io.github.neilljohnston.iwannajump.java.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import io.github.neilljohnston.iwannajump.java.elements.AABB;
import io.github.neilljohnston.iwannajump.java.elements.AABBSprite;
import io.github.neilljohnston.iwannajump.java.elements.SolidAABBSprite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static io.github.neilljohnston.iwannajump.java.engine.IWJEnvironment.*;


/**
 * A screen that can be used for tile-based games. The map itself is loaded from a Tiled .tmx map.
 *
 * @author Neill Johnston
 */
public abstract class IWJScreen extends ScreenAdapter {
    /**
     * IWJ environment.
     */
    protected final IWJEnvironment game;

    /**
     * Path to the Tiled map for this Screen.
     */
    protected final String mapPath;

    /**
     * Screen width.
     */
    public int width;

    /**
     * Screen height.
     */
    public int height;

    /**
     * The orthographic camera that will draw the screen.
     */
    protected OrthographicCamera camera;

    /**
     * The viewport that stretches to fill the screen.
     */
    protected FitViewport viewport;

    /**
     * The Tiled map from the mapPath tmx file.
     */
    protected TiledMap map;

    /**
     * Renderer for the map.
     */
    protected OrthogonalTiledMapRenderer mapRenderer;

    /**
     * Sprites broad-phaser.
     */
    protected BroadAreaMap<AABBSprite> sprites;

    /**
     * Tiles grid.
     * TODO allow better customization for this.
     */
    protected BroadAreaMap<AABB> tiles;

    /**
     * Layer draw order.
     */
    protected List<String> layerPriority;



    /**
     * Create a new IWJScreen and initialize all relevant objects.
     *
     * @param game      Host {@link IWJEnvironment} instance
     * @param mapPath   Map to load
     */
    public IWJScreen(IWJEnvironment game, String mapPath, int width, int height) {
        // Initialize parameters
        this.game = game;
        this.mapPath = mapPath;
        this.width = width;
        this.height = height;

        // Set up camera and viewport
        camera = new OrthographicCamera();
        camera.setToOrtho(false, width, height);
        viewport = new FitViewport(width, height, camera);

        // Initialize map and map renderer
        map = new TmxMapLoader().load(mapPath);
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1, game.batch);
        mapRenderer.setView(camera);
        sprites = new SplitGridAreaMap();

        // Initialize the sprites map
        TiledMapTileLayer spritesTiles = (TiledMapTileLayer) map.getLayers().get(LAYER_SPRITES);
        for(int y = 0; y < spritesTiles.getHeight(); y++) {
            for(int x = 0; x < spritesTiles.getWidth(); x++) {
                try {
                    TiledMapTileLayer.Cell cell = spritesTiles.getCell(x, y);
                    String desc = (String) cell.getTile().getProperties().get(PROP_SPRITE_DESCRIPTION);
                    sprites.add(spriteFactory(x * PS, y * PS, desc));
                } catch(NullPointerException e) {
                    // Do nothing, not our problem :D
                }
            }
        }

        // Initialize the layer priority, do nothing with it yet
        layerPriority = new ArrayList<String>();

        game.log("Screen has been initialized", LOG_HIGH);
    }

    /**
     * Construct a sprite from the Tiled map.
     *
     * @param x             X-coordinate of the sprite
     * @param y             Y-coordinate of the sprite
     * @param description   Description of the sprite
     * @return A {@link AABBSprite} that matches the description given
     */
    abstract public AABBSprite spriteFactory(float x, float y, String description);

    /**
     * Construct a tile from the Tiled map.
     *
     * @param x             X-coordinate of the tile
     * @param y             Y-coordinate of the tile
     * @param description   Description of the tile
     * @return A {@link AABB} that matches the description given
     */
    abstract public AABB tileFactory(float x, float y, String description);



    /**
     * Game logic, will update sprites and do collision checking by default.
     * Override to tweak game logic (not recommended). Ideally, the basic step method is enough to do most of the game
     * logic - in which case, just override stepExtra.
     *
     * @param delta Time since last step (s)
     */
    protected void step(float delta) {
        for(AABBSprite sprite : sprites) {
            if(sprite instanceof SolidAABBSprite) {
                for (int y = 0; y < sprite.height / PS; y++) {
                    for (int x = 0; y < sprite.width / PS; x++) {
                        // TODO queue sprite collisions
                    }
                }
                sprite.step(delta);
            }
        }
    }

    /**
     * Camera tracking, will stretch the view around all sprites by default.
     * Override to provide more specific camera tracking (recommended).
     * @param delta Time since last step (s)
     */
    protected void trackCamera(float delta) {
        Rectangle bounds = new Rectangle(camera.position.x, camera.position.y, 0, 0);
        for(AABBSprite s : sprites)
            bounds.merge(s);
        float z = Math.max(bounds.width / camera.viewportWidth, bounds.height / camera.viewportHeight);
        camera.position.x = bounds.x + bounds.width / 2;
        camera.position.y = bounds.y + bounds.height / 2;
        camera.zoom = z + (float) Math.pow(Math.E, -z);
    }

    /**
     * Drawing order, will draw in the order specified by layerPriority by default.
     * Override to change the way that layers are drawn (not recommended).
     */
    protected void drawLayers() {
        // First draw all tile layers
        for(String layer : layerPriority)
            mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get(layer));
        // Then draw the sprites
        for(AABBSprite sprite : sprites)
            sprite.draw(game.batch);
    }

    /**
     * Perform game logic and render the screen.
     * Order of operations for the render method:
     *  - clear screen
     *  - stepSprites
     *  - stepExtra
     *  - fullSprites.update
     *  - trackCamera
     *  - limitCamera
     *  - camera.update
     *  - fullMapRenderer.setView
     *  - drawLayers (amid some calls to game.batch)
     *  - drawExtra
     *
     * @param delta Time since last step (s)
     */
    @Override
    public void render(float delta) {
        // Clear the screen to black
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Run game logic
        step(delta);

        // Run extra game logic
        stepExtra(delta);

        // Update the sprites map
        sprites.update();

        // Operate camera
        trackCamera(delta);

        // Update the camera and map renderer view, set to render a large area around the camera
        camera.update();
        mapRenderer.setView(camera.combined, 0, 0, width, height);

        // Draw screen from batch
        viewport.apply();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        drawLayers();
        game.batch.end();

        // Draw extra
        drawExtra();
    }

    /**
     * Blank method that can be overridden to provide extra functionality to the game step, without having to override
     * the entire step function.
     * This method is always run right after step.
     *
     * @param delta Time since last step (s)
     */
    protected void stepExtra(float delta) {}

    /**
     * Blank method that can be overridden to provide exta functionality to the game draw, without having to override
     * the entire draw function.
     * This method is always run right after draw.
     */
    protected void drawExtra() {}



    /**
     * Update the size of the screen.
     *
     * @param width     New screen width
     * @param height    New screen height
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        this.width = width;
        this.height = height;

        game.log("Window resized", LOG_LOW);
    }
}
