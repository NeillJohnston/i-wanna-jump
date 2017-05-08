package io.github.neilljohnston.iwannajump.java.elements;

/**
 * Information about a certain type of collision between two objects.
 *
 * @param <Pro> The "holder" of this collision, a.k.a. the one that gets hit
 * @param <Con> The "reactor" of this collision, a.k.a. the one that hits
 * @author Neill Johnston
 */
public interface Collision<Pro extends AABB, Con extends AABB> {
    /**
     * Resolve a collision of con hitting pro
     *
     * @param pro   The hit
     * @param con   The hitter
     * @param delta Time since last step (s)
     */
    public void resolve(Pro pro, Con con, float delta);
}
