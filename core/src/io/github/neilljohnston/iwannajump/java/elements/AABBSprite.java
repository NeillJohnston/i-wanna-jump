package io.github.neilljohnston.iwannajump.java.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * A mobile AABB.
 *
 * @author Neill Johnston
 */
public abstract class AABBSprite extends AABB {
    /**
     * Velocity vector of this sprite.
     */
    public Vector2 v;

    /**
     * Initialize.
     */
    public AABBSprite() {
        v = new Vector2();
    }

    /**
     * Update the sprite, will just add velocity by default.
     *
     * @param delta Time since last step (s)
     */
    public void step(float delta) {
        x += v.x * delta;
        y += v.y * delta;
    }

    /**
     * Draw this sprite to the specified batch.
     * Override to actually use this method.
     *
     * @param batch SpriteBatch to draw to
     */
    abstract public void draw(SpriteBatch batch);
}
