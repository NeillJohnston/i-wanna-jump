package io.github.neilljohnston.iwannajump.java.elements;

import com.badlogic.gdx.math.Rectangle;

/**
 * A platform is any AABB that only respects collision on one side. This platform collision will only
 * hold back con (an {@link SpriteAABB}) if it collides from the top side.
 *
 * @author Neill Johnston
 */
public class PlatformCollision implements Collision<AABB, SpriteAABB> {
    /**
     * Allow con to pass through solely the top side of pro.
     *
     * @param pro   The hit
     * @param con   The hitter
     * @param delta Time since last step (s)
     */
    @Override
    public void resolve(AABB pro, SpriteAABB con, float delta) {
        float dx = con.v.x * delta;
        float dy = con.v.y * delta;

        Rectangle xyStep = new Rectangle(con);
        xyStep.x += dx;
        xyStep.y += dy;

        Rectangle yStep = new Rectangle(con);
        yStep.y += dy;

        if(xyStep.overlaps(pro) && yStep.overlaps(pro) && dy < 0 && con.y >= pro.y + pro.height) {
            con.y = pro.y + pro.height;
            con.v.y = 0;
        }
    }
}
