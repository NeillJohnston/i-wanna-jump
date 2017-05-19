package io.github.neilljohnston.iwannajump.test;

import io.github.neilljohnston.iwannajump.java.elements.AABB;

/**
 * Created by Neill on 5/17/2017.
 */
public class PlatformAABB extends AABB {
    public PlatformAABB(float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public Class<? extends AABB> type() {
        return PlatformAABB.class;
    }
}
