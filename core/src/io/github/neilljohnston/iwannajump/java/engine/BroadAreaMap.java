package io.github.neilljohnston.iwannajump.java.engine;

import java.util.Iterator;
import java.util.Set;

/**
 * A broad-phase map intended to be used for collision detection for a specific type of 2-D body.
 *
 * @param <Body>    The type of 2-D body that will be mapped
 * @author Neill Johnston
 */
public interface BroadAreaMap<Body> extends Iterable<Body> {
    /**
     * Adds a Body to the map.
     *
     * @param body  Body to be added
     */
    void add(Body body);

    /**
     * Removes a Body from the map.
     *
     * @param body  Body to be removed
     */
    void remove(Body body);

    /**
     * Scan for collisions against a specific Body, and return the possible matches.
     *
     * @param body  Body to be scanned
     * @return Possible objects that are colliding with body
     */
    Iterable<Body> scan(Body body);

    /**
     * Updates the map.
     */
    void update();
}
