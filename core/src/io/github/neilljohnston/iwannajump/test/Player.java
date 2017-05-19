package io.github.neilljohnston.iwannajump.test;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.sun.org.apache.xml.internal.resolver.readers.ExtendedXMLCatalogReader;
import io.github.neilljohnston.iwannajump.java.elements.AABB;
import io.github.neilljohnston.iwannajump.java.elements.PlatformCollision;
import io.github.neilljohnston.iwannajump.java.elements.SpriteAABB;
import io.github.neilljohnston.iwannajump.java.elements.SquareCollision;
import io.github.neilljohnston.iwannajump.java.engine.AnimationMap;
import io.github.neilljohnston.iwannajump.java.engine.KeyLogger;
import io.github.neilljohnston.iwannajump.java.math.ExMath;
import io.github.neilljohnston.iwannajump.java.math.Smooth;
import com.badlogic.gdx.Input.Keys;

import static io.github.neilljohnston.iwannajump.java.engine.IWJEnvironment.PS;

public class Player extends SpriteAABB {
    private static final float G = -20 * PS;
    private static final int[] KEYS = {
            Keys.A,
            Keys.D,
            Keys.SPACE,
            Keys.SHIFT_LEFT
    };

    private State state;
    private Direction direction;

    private AnimationMap<State> animations;
    private KeyLogger logger;

    public Player(float x, float y) {
        this.x = x;
        this.y = y;
        this.width = PS * 0.75f;
        this.height = PS * 1.75f;
        this.v.y = -20;

        Texture playerTexture = new Texture(Gdx.files.internal("THE_DUDE.png"));
        animations = new AnimationMap<State>();
        animations.put(State.IDLE, playerTexture, 16, 32, 1f/8f, 0, 0, 0, 0, Animation.PlayMode.LOOP);
        animations.put(State.RUNNING, playerTexture, 16, 32, 1f/8f, 1, 0, 6, 0, Animation.PlayMode.LOOP);
        animations.put(State.WALKING, playerTexture, 16, 32, 1f/8f, 1, 1, 6, 1, Animation.PlayMode.LOOP);

        logger = new KeyLogger(KEYS);

        state = State.IDLE;
        direction = Direction.RIGHT;

        collisions.put(PlatformAABB.class, new PlatformCollision());
    }

    @Override
    public void step(float delta) {
        super.step(delta);

        logger.step(delta);

        v.x *= 0.8;
        state = State.IDLE;
        direction = v.x < 0 ? Direction.LEFT : Direction.RIGHT;

        if(logger.is(Keys.SHIFT_LEFT)) {
            if (logger.is(Keys.A))
                v.x = Smooth.lerp(0, -6.5f * PS, Smooth.unitSin(1f/0.60f * ExMath.absMin(logger.on(Keys.A), 0.60f)));
            if (logger.is(Keys.D))
                v.x = Smooth.lerp(0, 6.5f * PS, Smooth.unitSin(1f/0.60f * ExMath.absMin(logger.on(Keys.D), 0.60f)));

            if(Math.abs(v.x) > PS)
                state = State.RUNNING;
        } else {
            if (logger.is(Keys.A))
                v.x = Smooth.lerp(0, -3.5f * PS, Smooth.unitSin(1f/0.40f * ExMath.absMin(logger.on(Keys.A), 0.40f)));
            if (logger.is(Keys.D))
                v.x = Smooth.lerp(0, 3.5f * PS, Smooth.unitSin(1f/0.40f * ExMath.absMin(logger.on(Keys.D), 0.40f)));

            if(Math.abs(v.x) > PS)
                state = State.WALKING;
        }

        if(v.y == 0) {
            if(logger.is(Keys.SPACE))
                v.y = -0.45f * G;
        } else {
            if(logger.is(Keys.SPACE) && v.y > 0)
                v.y -= 0.30f * G * delta;
        }

        v.y += G * delta;
    }

    @Override
    public void draw(SpriteBatch batch, float delta) {
        Animation<TextureRegion> current = animations.get(state);
        batch.draw(current.getKeyFrame(TimeUtils.millis() % 100000 / 1000f),
                direction == Direction.LEFT ? x + 14 : x - 2, y,
                direction == Direction.LEFT ? -16 : 16, 32);
    }

    private enum State {
        IDLE,
        WALKING,
        RUNNING,
        AIRBORNE
    }

    private enum Direction {
        LEFT,
        RIGHT
    }
}
