package io.github.neilljohnston.iwannajump.java.engine;

import io.github.neilljohnston.iwannajump.java.elements.SpriteAABB;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A basic kind of BroadAreaMap based on two parallel planes for tracking.
 */
public class SplitGridAreaMap implements BroadAreaMap<SpriteAABB> {
    private HashSet<SpriteAABB> sprites;

    public SplitGridAreaMap() {
        sprites = new HashSet<SpriteAABB>();
    }

    @Override
    public void add(SpriteAABB body) {
        sprites.add(body);
    }

    @Override
    public void remove(SpriteAABB body) {
        sprites.remove(body);
    }

    @Override
    public Set<SpriteAABB> scan(SpriteAABB body) {
        return sprites;
    }

    @Override
    public void update() {
        // TODO update locations
    }

    @Override
    public Iterator<SpriteAABB> iterator() {
        return sprites.iterator();
    }
}
