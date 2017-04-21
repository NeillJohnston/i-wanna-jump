package io.neilljohnston.github

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}

class IWannaJump extends Game {
    // Init batch, font with placeholders (do not construct, otherwise libgdx will crash)
    var batch: SpriteBatch = _
    var font: BitmapFont = _

    /**
      * Creates the game.
      */
    override def create(): Unit = {
        batch = new SpriteBatch()
        font = new BitmapFont()

        setScreen(new GameScreen(this))
    }

    /**
      * Calls super to render.
      */
    override def render(): Unit = super.render()

    /**
      * Disposes batch, font.
      */
    override def dispose(): Unit = {
        batch.dispose()
        font.dispose()
    }
}

/**
  * IWannaJump companion object.
  */
object IWannaJump {
    // Default pixel scale size
    final val Ps = 64
}