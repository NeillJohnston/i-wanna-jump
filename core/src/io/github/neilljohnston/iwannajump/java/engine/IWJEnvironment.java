package io.github.neilljohnston.iwannajump.java.engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Environment and main class for a game.
 *
 * @author Neill Johnston
 */
public class IWJEnvironment extends Game {
    /**
     * Default tile size
     */
    public static final int PS = 16;

    /**
     * Default tiles layer name.
     */
    public static final String LAYER_TILES = "tiles";

    /**
     * Default sprites layer name.
     */
    public static final String LAYER_SPRITES = "sprites";

    /**
     * Name of the sprite description property in Tiled.
     * Mainly useful for creating sprites from {@link IWJScreen#spriteFactory(float, float, String) spriteFactory}.
     */
    public static final String PROP_SPRITE_DESCRIPTION = "desc";

    /**
     * Name of the tile description property in Tiled.
     * Mainly useful for creating tiles from {@link IWJScreen#tileFactory(float, float, String) tileFactory}.
     */
    public static final String PROP_TILE_DESCRIPTION = "desc";

    /**
     * Log verbosity level: off, no messages shown.
     */
    public static final int LOG_OFF = -1;

    /**
     * Log verbosity level: high priority.
     */
    public static final int LOG_HIGH = 0;

    /**
     * Log verbosity level: low priority.
     */
    public static final int LOG_LOW = 1;

    /**
     * Verbosity level of the console log.
     */
    public static int priority = LOG_LOW;



    /**
     * Batch for drawing to OpenGL.
     */
    public SpriteBatch batch;

    /**
     * Create the game through IWJ. Naturally, this needs to be overloaded or else it does nothing.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();
    }

    /**
     * Log a message to console under a specified priority.
     * Priority determines how important a message is, and the variable {@link #priority} will determine the lowest
     * (most important) priority level of messages to display.
     * Right, low priority number = high importance = high priority. Sorry if that's confusing. Think of DEFCON.
     *
     * @param message           The message to display
     * @param messagePriority   Message will not be displayed if this is greater than the current {@link #priority}
     */
    public void log(String message, int messagePriority) {
        if(messagePriority <= priority)
            System.out.println(message);
    }
}
