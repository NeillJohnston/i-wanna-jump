package io.github.neilljohnston.iwannajump.java.elements;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A sprite with collision detection and resolution.
 *
 * @author Neill Johnston
 */
abstract public class SolidSpriteAABB extends SpriteAABB {
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
    public SolidSpriteAABB() {
        super();
        queue = new HashSet<AABB>();
        collisions = new HashMap<Class<? extends AABB>, Collision>();

        // Add to the collision reactions
        collisions.put(AABB.class, new SquareCollision());
    }

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
                System.err.println("Unchecked collision error");
                e.printStackTrace();
            }
        }
        queue.clear();
    }

    @Override
    public void step(float delta) {
        resolveQueue(delta);
        super.step(delta);
    }
}
