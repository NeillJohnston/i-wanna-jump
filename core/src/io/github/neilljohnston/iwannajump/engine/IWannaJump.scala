package io.github.neilljohnston.iwannajump.engine

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.{BitmapFont, SpriteBatch}
import com.badlogic.gdx.maps.MapProperties
import io.github.neilljohnston.iwannajump.elements.Sprite

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

        // Set the screen to a new game screen in here
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

    /**
      * Derpy little command system.
      * @param command  Command
      */
    def runCommand(command: String): Unit = {
        val t = command.stripMargin.split("\\s+")
        t(0) match {
            //case "map" => setScreen(new IWJScreen(this, t(1)))
            case "stop" => dispose()
            case echo => t(1) match {
                case "-twice" => println(t(2) + " " + t(2))
                case _ => println(t(1))
            }
        }
    }
}

/**
  * IWJEnvironment companion object.
  */
object IWannaJump {
    // Default pixel scale size
    final val Ps = 64
}
