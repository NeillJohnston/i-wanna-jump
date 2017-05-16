package io.github.neilljohnston.iwannajump.java.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import io.github.neilljohnston.iwannajump.java.elements.*;
import io.github.neilljohnston.iwannajump.java.engine.IWJEnvironment;
import io.github.neilljohnston.iwannajump.java.engine.IWJScreen;

import static io.github.neilljohnston.iwannajump.java.engine.IWJEnvironment.LOG_LOW;
import static io.github.neilljohnston.iwannajump.java.engine.IWJEnvironment.PS;

/**
 * 'Tis but a harmless test.
 *
 * @author Neill Johnston
 */
public class IWJTest extends IWJEnvironment {
    @Override
    public void create() {
        super.create();
        setScreen(new TestIWJScreen(this, "test.tmx", 1280, 720));
    }
}

class TestIWJScreen extends IWJScreen {
    public TestIWJScreen(IWJEnvironment iwj, String mapPath, int width, int height) {
        super(iwj, mapPath, width, height);
        layerPriority.add("bg");
        layerPriority.add("tiles");
    }

    @Override
    public SpriteAABB spriteFactory(float x, float y, String description) {
        if(true) {
            SolidSpriteAABB s = new SolidSpriteAABB() {
                private static final float G = -20.0f * PS;

                private Texture t = new Texture(Gdx.files.internal("THE_DUDE.png"));
                private TextureRegion[][] tSplit = TextureRegion.split(t, 16, 32);
                private Animation<TextureRegion> ta = new Animation<TextureRegion>(
                        1f/8f,
                        tSplit[0][1],
                        tSplit[0][2],
                        tSplit[0][3],
                        tSplit[0][4],
                        tSplit[0][5],
                        tSplit[0][6]
                );
                private boolean onGround = true;
                private boolean hidden = false;

                @Override
                public void step(float delta) {
                    super.step(delta);

                    onGround = v.y == 0;
                    hidden = Gdx.input.isKeyPressed(Input.Keys.Z);

                    if(onGround)
                        v.x *= 0.6f;
                    else
                        v.x *= 0.98f;

                    if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
                        v.x = -72;
                    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                        v.x = 72;
                    if(onGround && Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
                        v.y = -28.0f * G * delta;

                    if(hidden)
                        v.x = 0;

                    v.y += G * delta;
                }

                @Override
                public void draw(SpriteBatch batch, float delta) {
                    if(hidden)
                        batch.draw(tSplit[1][0], x - PS / 8, y);
                    else if(Math.abs(v.x) < 10)
                        batch.draw(tSplit[0][0], x - PS / 8, y);
                    else
                        batch.draw(ta.getKeyFrame((TimeUtils.millis() % 100000) / 1000f), x - PS / 8, y);
                }

                public SolidSpriteAABB init() {
                    ta.setPlayMode(Animation.PlayMode.LOOP);
                    collisions.put(PlatformAABB.class, new PlatformCollision());
                    collisions.put(SlopeAABB.class, new SlopeCollision());
                    return this;
                }
            }.init();
            s.x = x;
            s.y = y;
            s.width = PS * 0.75f;
            s.height = PS * 1.75f;

            game.log("Player sprite has been created", LOG_LOW);
            return s;
        }
        else {
            return null;
        }
    }

    @Override
    public AABB tileFactory(float x, float y, String description) {
        if(description == null) {
            return new AABB(x, y, PS, PS);
        } else if(description.equals("platform")) {
            return new PlatformAABB(new AABB(x, y, PS, PS));
        } else if(description.startsWith("slope")) {
            float m = Float.valueOf(description.split(",")[1]);
            float b = Float.valueOf(description.split(",")[2]) * PS;
            return new SlopeAABB(new AABB(x, y, PS, PS), m, b);
        } else {
            return new AABB(0, 0, 0, 0);
        }
    }

    @Override
    protected void trackCamera(float delta) {
        super.trackCamera(delta);
        camera.zoom /= 4;
    }

    @Override
    protected void drawLayers(float delta) {
        super.drawLayers(delta);

        mapRenderer.renderTileLayer((TiledMapTileLayer) map.getLayers().get("cosmetic"));
    }

    protected class PlatformAABB extends AABB {
        public PlatformAABB(Rectangle from) {
            super(from);
        }

        @Override
        public Class<? extends AABB> type() {
            return PlatformAABB.class;
        }
    }
}
