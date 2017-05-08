package io.github.neilljohnston.iwannajump.java.elements;

import com.badlogic.gdx.math.Rectangle;

public class SquareCollision implements Collision<AABB, AABBSprite> {
    /**
     * Resolve a collision between two simple rectangles.
     *
     * @param pro   The hit
     * @param con   The hitter
     * @param delta Time since last step (s)
     */
    @Override
    public void resolve(AABB pro, AABBSprite con, float delta) {
        // Create test case rectangles
        float dx = con.v.x * delta, dy = con.v.y * delta;
        Rectangle xyStep = new Rectangle(con);
        Rectangle xStep = new Rectangle(con);
        Rectangle yStep = new Rectangle(con);
        xyStep.x += dx;
        xyStep.y += dy;
        xStep.x += dx;
        yStep.y += dy;

        if(xyStep.overlaps(pro)) {
            // Handle the x half of collisions first
            if(xStep.overlaps(pro)) {
                if(dx < 0)
                    con.x = pro.x + pro.width;
                else if(dx > 0)
                    con.x = pro.x - con.width;
                con.v.x = 0;
            }

            // Handle the y half of collision second
            if(yStep.overlaps(pro)) {
                if(dy < 0)
                    con.y = pro.y + pro.height;
                else if(dy > 0)
                    con.y = pro.y - con.height;
                con.v.y = 0;
            }
        }
    }
}
