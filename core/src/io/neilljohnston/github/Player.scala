package io.neilljohnston.github

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.{SpriteBatch, TextureRegion}
import io.neilljohnston.github.IWannaJump.Ps
import io.neilljohnston.github.Sprite.KeyControllable

/**
  * Created by Neill on 4/16/2017.
  */
class Player extends FullSprite with KeyControllable with exmath.Smooth {
    // Constants for accel/vel
    final val MagJump: Float = 12 * Ps
    final val MagVel: Float = 8 * Ps
    final val FloatUp: Float = g * 0.45f
    final val FloatDown: Float = g * 0.3f

    // Make player slightly smaller than a tile size
    width = Ps * 0.75f
    height = Ps * 0.75f

    val playerTexture = new Texture(Gdx.files.internal("cat.png"))
    val toDraw = new TextureRegion(playerTexture, 16, 7*16, 16, 16)

    // Add keyboard inputs
    addHoldInput(Keys.LEFT, (d: Float) => if(v.y == 0) v.x += smoothTo(v.x, -MagVel, 20f) * d else v.x += smoothTo(v.x, -MagVel, 8f) * d)
    addHoldInput(Keys.RIGHT, (d: Float) => if(v.y == 0) v.x += smoothTo(v.x, MagVel, 20f) * d else v.x += smoothTo(v.x, MagVel, 8f) * d)
    addTapInput(Keys.SPACE, (_: Float) => if(v.y == 0) v.y = MagJump)
    addHoldInput(Keys.SPACE, (d: Float) => if(v.y > 0) v.y -= FloatUp * d else v.y -= FloatDown * d)
    addRepInput(Keys.DOWN, (d: Float) => v.y = MagJump / 2, 500)

    /**
      * Apply gravity to the sprite.
      * @param delta    Time passed since last step
      */
    override def applyGravity(delta: Float): Unit = {
        if(keyDown(Keys.SPACE))
            v.y = -accelWithMax(-v.y, -gMax * 0.6f, -g * delta)
        else
            v.y = -accelWithMax(-v.y, -gMax, -g * delta)
    }

    /**
      * Override step to include key controls.
      * @param delta    Time passed since last update
      */
    override def step(delta: Float): Unit = {
        callInputs(delta)
        super.step(delta)
        if(!keyDown(Keys.LEFT) && !keyDown(Keys.RIGHT) && v.y == 0)
            v.x *= 0.6f
        if(!keyDown(Keys.LEFT) && !keyDown(Keys.RIGHT) && v.y != 0)
            v.x *= 0.95f
    }

    override def draw(batch: SpriteBatch): Unit = batch.draw(toDraw, x - 8, y - 4, Ps, Ps)
}
