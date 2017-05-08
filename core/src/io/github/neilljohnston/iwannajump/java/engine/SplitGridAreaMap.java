package io.github.neilljohnston.iwannajump.java.engine;

import io.github.neilljohnston.iwannajump.java.elements.AABBSprite;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * A basic kind of BroadAreaMap based on two parallel planes for tracking.
 */
public class SplitGridAreaMap implements BroadAreaMap<AABBSprite> {
    private HashSet<AABBSprite> sprites;

    public SplitGridAreaMap() {
        sprites = new HashSet<AABBSprite>();
    }

    @Override
    public void add(AABBSprite body) {
        sprites.add(body);
    }

    @Override
    public void remove(AABBSprite body) {
        sprites.remove(body);
    }

    @Override
    public Set<AABBSprite> scan(AABBSprite body) {
        return sprites;
    }

    @Override
    public void update() {
        // TODO update locations
    }

    @Override
    public Iterator<AABBSprite> iterator() {
        return sprites.iterator();
    }
}
