package io.github.neilljohnston.iwannajump.java.elements;

import java.util.HashMap;
import java.util.HashSet;

/**
 * A sprite with collision detection and resolution.
 *
 * @author Neill Johnston
 */
abstract public class SolidAABBSprite extends AABBSprite {
    /**
     * The available AABBs to check for collisions.
     */
    private HashSet<AABB> queue;

    /**
     * All collision reaction types for this sprite.
     */
    private HashMap<Class<AABB>, Collision> collisions;

    /**
     * Initialize.
     */
    public SolidAABBSprite() {
        super();
        queue = new HashSet<AABB>();
        collisions = new HashMap<Class<AABB>, Collision>();

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
            collisions.get(o.getClass()).resolve(o, this, delta);
        }
    }
}
