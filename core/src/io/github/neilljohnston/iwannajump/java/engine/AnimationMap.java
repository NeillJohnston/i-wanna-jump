package io.github.neilljohnston.iwannajump.java.engine;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

/**
 * Provides a way to manage sprite animations that come from a sprite sheet.
 */
public class AnimationMap<Key> extends HashMap<Key, Animation<TextureRegion>> {
    public AnimationMap() {
        // TODO maybe???
    }

    /**
     * Create a new {@link Animation} to add to the map.
     *
     * @param key       Key for this new Animation
     * @param texture   Texture to draw from
     * @param width     Width (in pixels) of each tile in the sprite sheet
     * @param height    Height (in pixels) of each tile in the sprite sheet
     * @param duration  How long each frame lasts
     * @param x1        X-coordinate for the top-left corner of the grid
     * @param y1        Y-coordinate for the top-left corner of the grid
     * @param x2        X-coordinate for the bottom-right corner of the grid
     * @param y2        Y-coordinate for the bottom-right corner of the grid
     * @param playMode  PlayMode of this new animation
     */
    public void put(Key key, Texture texture, int width, int height, float duration, int x1, int y1, int x2, int y2,
                    Animation.PlayMode playMode) {
        // Split up the texture into regions
        TextureRegion[][] split = TextureRegion.split(texture, width, height);

        // Instantiate an Array of frames
        Array<TextureRegion> frames = new Array<TextureRegion>((x2 - x1 + 1) * (y2 - y1 + 1));

        // Iterate over the coordinates from the parameters
        for(int y = y1; y <= y2; y++)
            for(int x = x1; x <= x2; x++)
                frames.add(split[y][x]);

        // Create and add the animation to the map
        Animation<TextureRegion> animation = new Animation<TextureRegion>(duration, frames, playMode);
        put(key, animation);
    }

    /**
     * Create a new static animation (i.e., not actually an animation) and add it to the map.
     * This method is shorthand for the above method in the case that you only need a single image as the animation.
     *
     * @param key       Key for this new Animation
     * @param texture   Texture to draw from
     * @param width     Width (in pixels) of each tile in the sprite sheet
     * @param height    Height (in pixels) of each tile in the sprite sheet
     * @param x         X-coordinate for region
     * @param y         Y-coordinate for region
     */
    public void put(Key key, Texture texture, int width, int height, int x, int y) {
        // Split up the texture into regions
        TextureRegion[][] split = TextureRegion.split(texture, width, height);

        // Create and add the animation to the map
        Animation<TextureRegion> animation = new Animation<TextureRegion>(1,
                new Array<TextureRegion>(new TextureRegion[]{split[x][y]}), Animation.PlayMode.NORMAL);
        put(key, animation);
    }

    /**
     * Create a new static animation (i.e., not actually an animation) and add it to the map.
     * This method is shorthand for the above method in the case that you only need a single region as the animation.
     *
     * @param key           Key for this new Animation
     * @param textureRegion Single image as a {@link TextureRegion}
     */
    public void put(Key key, TextureRegion textureRegion) {
        // Create and add the animation to the map
        Animation<TextureRegion> animation = new Animation<TextureRegion>(1,
                new Array<TextureRegion>(new TextureRegion[]{textureRegion}), Animation.PlayMode.NORMAL);
        put(key, animation);
    }
}
