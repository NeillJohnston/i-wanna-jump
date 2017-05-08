package io.github.neilljohnston.iwannajump.elements

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.TimeUtils.millis
import io.github.neilljohnston.iwannajump.elements.AABB.{PlatformAABB, SlopeAABB}
import io.github.neilljohnston.iwannajump.engine.IWannaJump.Ps
import io.github.neilljohnston.iwannajump.elements.Sprite.{Gravity, Solid}
import io.github.neilljohnston.iwannajump.exmath.Smooth

import scala.collection.mutable

/**
  * A sprite is defined primarily by a rectangular hitbox.
  */
abstract class Sprite extends AABB {
    import Sprite.debugTexture

    /**
      * Velocity vector of this sprite.
      */
    val v: Vector2 = new Vector2(0f, 0f)

    /**
      * Update the sprite. Should update everything about a sprite: state, position, velocity, etc.
      * @param delta    Time passed since last update
      */
    def step(delta: Float): Unit = {
        x += v.x * delta
        y += v.y * delta
    }

    /**
      * Draw this sprite to the specified sprite batch. Override or this will default to simply drawing the
      * debug texture, a red wireframe box about the sprite's hitbox. Sad!
      * @param batch    Batch to draw to
      */
    def draw(batch: SpriteBatch): Unit = batch.draw(debugTexture, x, y, width, height)

    /**
      * Draw this sprite like above, but align it to pixels. X and y are never changed, but it looks more retro.
      * Kind of experimental, kind of just for fun.
      * Note: Unless you perform a similar operation to the camera coordinates, this will look REALLY weird.
      * @param batch    Batch to draw to
      * @param native   Native dimension of the texture graphics, i.e. the precision of the pixel alignment
      */
    def drawRetro(batch: SpriteBatch, native: Float = 16): Unit = {
        val f = Ps / native
        batch.draw(debugTexture, f * (x / f).toInt, f * (y / f).toInt, width, height)
    }

    /**
      * Returns a new AABB that's been translated a little bit over. Useful mainly in collision detection.
      * @param dx   How much to translate on x
      * @param dy   How much to translate on y
      * @return A new AABB
      */
    def translated(dx: Float, dy: Float): AABB = AABB.factory(x + dx, y + dy, width, height, "")
}

/**
  * AABBSprite companion object, featuring sprite traits that can aid the creation extension of the AABBSprite class.
  */
object Sprite {
    val debugTexture = new Texture(Gdx.files.internal("debug-sprite.png"))

    /**
      * Defines a sprite that should be able to receive collision detection information. Mixin the Solid trait if
      * the sprite will ever have any interaction and response with any object, ever (even tiles).
      */
    trait Solid extends Sprite {
        /**
          * Set of all the objects this Solid could be colliding with.
          * This set should have objects added by the collisions manager, and cleared each step.
          */
        val colliding: mutable.HashSet[AABB] = new mutable.HashSet[AABB]

        /**
          * Queues another object to the colliding set, in case you don't want to directly access colliding. Also looks
          * neater in my opinion, even though all it does is add more typing.
          * One of the nice parts about this is that Scala devs can use operator notation with their sprite, e.g.
          * "sprite queueCollision object" which also looks neat in my eyes.
          * @param that Colliding AABB
          */
        def queueCollision(that: AABB): Unit = { colliding += that }

        /**
          * Resolves all pending collisions.
          * ...
          * That "_" is why I fuckin DIG Scala.
          * I mean look at that! One line to do what Java would do in like 3 at least.
          * Well actually Java 1.8 (the new one) added lambda expressions, so it could do this in one line.
          * Sadly my ramble about Scala has completely negated the benefits of Scala's underscore syntax.
          * Sorry.
          * @param delta    Time passed since last step
          */
        def resolveAll(delta: Float): Unit = colliding.foreach(resolve(delta, _))

        /**
          * Figures out which AABB collision type to use, then resolve accordingly.
          * @param delta    Time passed since last step
          * @param that     Colliding AABB
          */
        def resolve(delta: Float, that: AABB): Unit = that match {
            case a: PlatformAABB => resolvePlatform(delta, a)
            case a: SlopeAABB => resolveSlope(delta, a)
            case a: Sprite => resolveSprite(delta, a)
            case a: AABB => resolveFull(delta, a)
        }

        /**
          * Smoothly resolves a collision between this and another AABB.
          * @param delta    Time passed since last step
          * @param that     Colliding AABB
          */
        def resolveFull(delta: Float, that: AABB): Unit = {
            val (vx, vy) = (v.x * delta, v.y * delta)
            val xyStep = translated(vx, vy)

            if(xyStep overlaps that) {
                // Handle the x half of collisions first
                val xStep = translated(vx, 0)
                if(xStep overlaps that) {
                    if(v.x < 0) x = that.x + that.width
                    else if(v.x > 0) x = that.x - width
                    v.x = 0
                }

                // Handle the y half of collision second
                val yStep = translated(0, vy)
                if(yStep overlaps that) {
                    if(v.y < 0) y = that.y + that.height
                    else if(v.y > 0) y = that.y - height
                    v.y = 0
                }
            }
        }

        /**
          * Resolves a collision between this and a platform (collisions enabled on only top face).
          * @param delta    Time passed since last step
          * @param that     Colliding PlatformAABB
          */
        def resolvePlatform(delta: Float, that: PlatformAABB): Unit = {
            val (vx, vy) = (v.x * delta, v.y * delta)
            val xyStep = translated(vx, vy)

            if(xyStep overlaps that) {
                val yStep = translated(0, vy)
                if((yStep overlaps that) && v.y < 0 && y >= that.y + that.height) {
                    y = that.y + that.height
                    v.y = 0
                }
            }
        }

        /**
          * Resolves a collision between this and a slope.
          * TODO include slope-sticking
          * @param delta    Time passed since last step
          * @param that     Colliding SlopeAABB
          */
        def resolveSlope(delta: Float, that: SlopeAABB): Unit = {
            val (vx, vy) = (v.x * delta, v.y * delta)
            val xyStep = translated(vx, vy)

            if(that overlaps xyStep) {
                // Handle the x half of collisions first
                val xStep = translated(vx, 0)
                if(that overlaps xStep) {
                    val yStep = translated(0, vy)
                    if(that overlaps yStep) {
                        v.x *= math.abs(math.cos(math.atan(that.m))).toFloat
                        y -= math.min(y - that.f(x), y - that.f(x + width))
                        v.y = 0
                    }
                }
            }
        }

        /**
          * Resolves a sprite-on-sprite collision, and provides nuance to sprite interactions.
          * Since there are so many different ways of sprites interacting, this function is left unimplemented.
          * This method should be overridden to declare different types of sprite collisions, for each specific sprite.
          * Developers can implement sprite collisions however they want, but be aware that there is no default.
          * @param delta    Time passed since last step
          * @param that     Colliding AABBSprite
          */
        def resolveSprite(delta: Float, that: Sprite): Unit = {}

        /**
          * Search available tiles to find collisions.
          * @param tiles    Relevant tiles layer
          */
        def searchTiles(tiles: TiledMapTileLayer): Unit = {
            val tw = (width / Ps).toInt + 1
            val th = (height / Ps).toInt + 1

            // Scan for collisions
            for(oy <- -1 to th; ox <- -1 to tw) {
                val tx = (x / Ps).toInt + ox
                val ty = (y / Ps).toInt + oy
                try {
                    // Get the player to collide with a tile depending on the tile "type" tag
                    val t = tiles.getCell(tx, ty).getTile
                    val aabb = t.getProperties.get("type").asInstanceOf[String]
                    queueCollision(AABB.factory(tx * Ps, ty * Ps, aabb))
                }
                catch {
                    case e: NullPointerException => //
                }
            }
        }
    }

    /**
      * Defines a gravity-affected sprite.
      */
    trait Gravity extends Sprite with Smooth {
        val g: Float = -24.0f * Ps

        /**
          * Apply gravity to the sprite.
          * @param delta    Time passed since last step
          */
        def applyGravity(delta: Float): Unit = { v.y += g * delta }
    }

    /**
      * Defines a sprite that can be controlled with the keyboard.
      */
    trait KeyControllable extends Sprite {
        /**
          * Input types:
          *     Tap inputs respond when a key is pressed initially.
          *     Hold inputs respond when a key is held down.
          *     Rep inputs respond initially, then repeatedly with a specified period.
          * Hashmap of (input, callback) pairs.
          * Callback functions must take a float as a parameter, this is for passing delta as an argument.
          */
        val tapInputs: mutable.HashMap[Int, (Float) => Unit] = new mutable.HashMap[Int, (Float) => Unit]
        val holdInputs: mutable.HashMap[Int, (Float) => Unit] = new mutable.HashMap[Int, (Float) => Unit]
        val repInputs: mutable.HashMap[Int, (Float) => Unit] = new mutable.HashMap[Int, (Float) => Unit]

        /**
          * Timers and timings for repeated inputs.
          * Int -> (Long, Long) explanation: Int is the keycode from gdx.Input.Keys. The first long is the number of
          * milliseconds until repeat, the second long is the next scheduled repeat time.
          */
        val repInputTimers: mutable.HashMap[Int, (Long, Long)] = new mutable.HashMap[Int, (Long, Long)]

        /**
          * Add to the tap input map.
          * @param key      Key to respond to, a magic number from gdx.Input.Keys
          * @param callback Function to perform when the key is called
          */
        def addTapInput(key: Int, callback: (Float) => Unit): Unit = { tapInputs += (key -> callback) }

        /**
          * Add to the hold input map.
          * @param key      Key to respond to, a magic number from gdx.Input.Keys
          * @param callback Function to perform when the key is called
          */
        def addHoldInput(key: Int, callback: (Float) => Unit): Unit = { holdInputs += (key -> callback) }

        /**
          * Add to the rep input map.
          * @param key      Key to respond to, a magic number from gdx.Input.Keys
          * @param callback Function to perform when the key is called
          * @param r        Period of repetition for this callback
          */
        def addRepInput(key: Int, callback: (Float) => Unit, r: Long): Unit = {
            repInputs += (key -> callback)
            repInputTimers += (key -> (r, 0l))
        }

        /**
          * Call on the inputs.
          * @param delta    Time passed since last update
          */
        def callInputs(delta: Float): Unit = {
            for((key, callback) <- tapInputs)
                if(Gdx.input.isKeyJustPressed(key))
                    callback(delta)
            for((key, callback) <- holdInputs)
                if(Gdx.input.isKeyPressed(key))
                    callback(delta)
            for((key, callback) <- repInputs) {
                val r = repInputTimers(key)._1
                val t = repInputTimers(key)._2
                if (Gdx.input.isKeyPressed(key) && millis > t) {
                    callback(delta)
                    repInputTimers(key) = (r, millis + r)
                }
            }
        }

        /**
          * Convenience reference to Gdx.input.isKeyPressed.
          * @param key  Key
          * @return True if key is down, false otherwise
          */
        def keyDown(key: Int): Boolean = Gdx.input.isKeyPressed(key)

        /**
          * Convenience reference to Gdx.input.isKeyJustPressed.
          * @param key  Key
          * @return True if key was just pressed, false otherwise
          */
        def keyPress(key: Int): Boolean = Gdx.input.isKeyJustPressed(key)
    }
}

/**
  * An abstract extension of AABBSprite that includes every "typical" feature trait: Solid, Gravity. Used to minimize
  * boilerplate, and has an implementation of step that correctly orders the various steps.
  */
// TODO update this class as I add more AABBSprite traits.
abstract class FullSprite extends Sprite with Solid with Gravity {
    /**
      * Step with gravity and collisions in mind.
      * @param delta    Time passed since last update
      */
    override def step(delta: Float): Unit = {
        applyGravity(delta)
        resolveAll(delta)
        resolveAll(delta)
        colliding.clear()

        // Add displacement = velocity * time to the position.
        x += v.x * delta
        y += v.y * delta
        // Author's note: On 2017-04-16, I got stuck here for over an hour by forgetting to multiply v by delta.
        // If you're a young (or old) programmer reading this sometime in the future, please, learn from my mistakes.
        // And no matter how dumb you feel, you'll never be as dumb as me for that dreaded hour on 2017-04-16.
        //      -Neill Johnston
    }
}
