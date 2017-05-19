package io.github.neilljohnston.iwannajump.java.elements;

import com.badlogic.gdx.math.Rectangle;

public class SquareCollision implements Collision<AABB, SpriteAABB> {
    /**
     * Resolve a collision between two simple rectangles.
     *
     * @param pro   The hit
     * @param con   The hitter
     * @param delta Time since last step (s)
     */
    @Override
    public void resolve(AABB pro, SpriteAABB con, float delta) {
        // Create test case rectangles
        float dx = con.v.x * delta;
        float dy = con.v.y * delta;

        Rectangle xyStep = new Rectangle(con);
        xyStep.x += dx;
        xyStep.y += dy;

        Rectangle xStep = new Rectangle(con);
        xStep.x += dx;

        if(xyStep.overlaps(pro)) {
            // Handle the x half of collisions first
            if(xStep.overlaps(pro)) {
                con.x = dx < 0 ? pro.x + pro.width : pro.x - con.width;
                con.v.x = 0;
            }

            Rectangle yStep = new Rectangle(con);
            yStep.y += dy;

            // Handle the y half of collision second
            if(yStep.overlaps(pro)) {
                con.y = dy < 0 ? pro.y + pro.height : pro.y - con.height;
                con.v.y = 0;
            }
        }
    }
}
