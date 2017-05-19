package io.github.neilljohnston.iwannajump.java.elements;

/**
 * No collision. Do nothing. Prevent unchecked collision errors.
 */
public class NonCollision implements Collision<AABB, SpriteAABB> {
    /**
     * Do nothing.
     *
     * @param pro   The hit
     * @param con   The hitter
     * @param delta Time since last step (s)
     */
    @Override
    public void resolve(AABB pro, SpriteAABB con, float delta) {
        // Do nothing.
    }
}
