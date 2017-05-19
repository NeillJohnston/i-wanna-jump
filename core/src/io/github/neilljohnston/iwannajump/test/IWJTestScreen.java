package io.github.neilljohnston.iwannajump.test;

import io.github.neilljohnston.iwannajump.java.elements.AABB;
import io.github.neilljohnston.iwannajump.java.elements.SpriteAABB;
import io.github.neilljohnston.iwannajump.java.engine.IWJEnvironment;
import io.github.neilljohnston.iwannajump.java.engine.IWJScreen;

import static io.github.neilljohnston.iwannajump.java.engine.IWJEnvironment.PS;

public class IWJTestScreen extends IWJScreen {
    public IWJTestScreen(IWJEnvironment game, String mapPath, int width, int height) {
        super(game, mapPath, width, height);

        layerPriority.add("bg");
        layerPriority.add("tiles");
        layerPriority.add("cosmetic");
    }

    @Override
    public AABB tileFactory(float x, float y, String description) {
        if(description.equals("platform"))
            return new PlatformAABB(x, y, PS, PS);
        else
            return new AABB(x, y, PS, PS);
    }

    @Override
    public SpriteAABB spriteFactory(float x, float y, String description) {
        if(description.equals("player"))
            return new Player(x, y);
        else
            return null;
    }

    @Override
    protected void trackCamera(float delta) {
        super.trackCamera(delta);
        camera.zoom /= 4;
    }
}
