package io.github.neilljohnston.iwannajump.java.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * A mobile AABB.
 *
 * @author Neill Johnston
 */
public abstract class SpriteAABB extends AABB {
    /**
     * Velocity vector of this sprite.
     */
    public Vector2 v;

    /**
     * Initialize.
     */
    public SpriteAABB() {
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
     * Draw this sprite to the specified batch. Do not start or end the batch.
     * Override to actually use this method.
     *
     * @param batch SpriteBatch to draw to
     */
    abstract public void draw(SpriteBatch batch, float delta);

    /**
     * Get the possible area of collision (for collision detection).
     *
     * @return  An AABB that represents an area between this rectangle and its velocity vector
     */
    public AABB getCollisionArea(float delta) {
        Rectangle rectangle = new Rectangle(this);
        rectangle.x += v.x * delta;
        rectangle.y += v.y * delta;
        return new AABB(rectangle.merge(this));
    }

    /**
     * Type = SpriteAABB.
     *
     * @return {@link SpriteAABB}
     */
    @Override
    public Class<? extends AABB> type() {
        return SpriteAABB.class;
    }
}
