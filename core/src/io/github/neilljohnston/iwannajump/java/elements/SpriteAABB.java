package io.github.neilljohnston.iwannajump.java.elements;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A mobile AABB with collision detection.
 *
 * @author Neill Johnston
 */
public abstract class SpriteAABB extends AABB {
    /**
     * Velocity vector of this sprite.
     */
    public Vector2 v;

    /**
     * The available AABBs to check for collisions.
     */
    protected HashSet<AABB> queue;

    /**
     * All collision reaction types for this sprite.
     */
    protected HashMap<Class<? extends AABB>, Collision> collisions;



    /**
     * Initialize.
     */
    public SpriteAABB() {
        super();

        v = new Vector2();
        queue = new HashSet<AABB>();
        collisions = new HashMap<Class<? extends AABB>, Collision>();

        // Add to the collision reactions
        collisions.put(AABB.class, new SquareCollision());
    }

    /**
     * Update the sprite, will just add velocity by default.
     *
     * @param delta Time since last step (s)
     */
    public void step(float delta) {
        resolveQueue(delta);

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
     * Add a possible collision to the queue.
     *
     * @param aabb  The new AABB to collision check
     */
    public void addToQueue(AABB aabb) {
        queue.add(aabb);
    }

    /**
     * Resolve all queued collisions.
     *
     * @param delta Time since last step (s)
     */
    public void resolveQueue(float delta) {
        for(AABB o : queue) {
            try {
                Collision c = collisions.get(o.type());
                c.resolve(o, this, delta);
            } catch(NullPointerException e) {
                System.err.printf("Unchecked collision error: %s on %s\n", o.type(), this.type());
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
        queue.clear();
    }

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
