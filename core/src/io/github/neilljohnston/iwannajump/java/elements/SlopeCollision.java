package io.github.neilljohnston.iwannajump.java.elements;

import com.badlogic.gdx.math.Rectangle;

/**
 * A collision between a sprite and a straight slope. Made for {@link SlopeAABB}.
 */
public class SlopeCollision implements Collision<SlopeAABB, SpriteAABB> {
    @Override
    public void resolve(SlopeAABB pro, SpriteAABB con, float delta) {
        // Create test case rectangles
        float dx = con.v.x * delta;
        float dy = con.v.y * delta;

        Rectangle xyStep = new Rectangle(con);
        xyStep.x += dx;
        xyStep.y += dy;

        float left = Math.max(pro.x, con.x);
        float right = Math.min(pro.x + pro.width, con.x + con.width);

        if(xyStep.overlaps(pro) && (xyStep.y < pro.f(left) || xyStep.y < pro.f(right))) {
            // Only push con up
            con.y = Math.max(pro.f(left), pro.f(right));
            con.v.y = 0;
        } else if(xyStep.overlaps(pro) && Math.signum(dx) != Math.signum(pro.getSlope()) && Math.abs(dy / dx) < Math.abs(pro.getSlope())) {
            con.y = Math.max(pro.f(left), pro.f(right));
            con.v.y = 0;
        }
    }
}
