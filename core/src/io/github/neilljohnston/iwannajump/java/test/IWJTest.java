package io.github.neilljohnston.iwannajump.java.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.neilljohnston.iwannajump.java.elements.AABBSprite;
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
        layerPriority.add("tiles");
    }

    @Override
    public AABBSprite spriteFactory(float x, float y, String description) {
        if(true) {
            AABBSprite s = new AABBSprite() {
                private Texture t = new Texture(Gdx.files.internal("cat.png"));
                private TextureRegion tr = new TextureRegion(t, 0, 0, PS, PS);

                @Override
                public void step(float delta) {
                    super.step(delta);
                    if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
                        v.x = -16;
                    if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
                        v.x = 16;
                }

                @Override
                public void draw(SpriteBatch batch) {
                    batch.draw(tr, x, y);
                }
            };
            s.x = x;
            s.y = y;

            game.log("Player sprite has been created", LOG_LOW);
            return s;
        }
        else {
            return null;
        }
    }

    @Override
    protected void trackCamera(float delta) {
        super.trackCamera(delta);
        camera.zoom /= 4;
    }
}
