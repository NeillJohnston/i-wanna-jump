package io.github.neilljohnston.iwannajump.java.engine;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import io.github.neilljohnston.iwannajump.java.elements.AABB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import static io.github.neilljohnston.iwannajump.java.engine.IWJEnvironment.PROP_TILE_DESCRIPTION;
import static io.github.neilljohnston.iwannajump.java.engine.IWJEnvironment.PS;

/**
 * A simple grid of tiles, used for collision detection.
 */
public class TileGridMap implements BroadAreaMap<AABB> {
    private static final AABB NOT = new AABB(Float.MIN_VALUE, 0, 0, 0);

    private AABB[][] grid;
    public final int gridWidth;
    public final int gridHeight;

    /**
     * Construct an entire grid from a layer using {@link IWJScreen#tileFactory(float, float, String) tileFactory}.
     *
     * @param layer TiledMapTileLayer that holds the tiles
     */
    public TileGridMap(TiledMapTileLayer layer, IWJScreen screen) {
        this.gridWidth = layer.getWidth();
        this.gridHeight = layer.getHeight();
        grid = new AABB[gridHeight][gridWidth];

        for(int y = 0; y < layer.getHeight(); y++) {
            for(int x = 0; x < layer.getWidth(); x++) {
                try {
                    TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                    String description = (String) cell.getTile().getProperties().get(PROP_TILE_DESCRIPTION);
                    grid[y][x] = screen.tileFactory(x * PS, y * PS, description);
                } catch(NullPointerException e) {
                    // Do nothing, not our problem :D
                }
            }
        }
    }

    /**
     * Return all the cells in a specified range, in a rectangle from (x, y) to (x + width, y + height).
     *
     * @param x         X-coordinate of the bottom left of the range
     * @param y         Y-coordinate of the bottom left of the range
     * @param width     Number of tiles that the range spans x-wise
     * @param height    Number of tiles that the range spans y-wise
     * @return An Iterable containing all the tiles in the range
     */
    public Iterable<AABB> range(final int x, final int y, final int width, final int height) {
        return new Iterable<AABB>() {
            @Override
            public Iterator<AABB> iterator() {
                return new Iterator<AABB>() {
                    int w = Math.max(width, 0);
                    int h = Math.max(height, 0);
                    int ix = Math.min(Math.max(0, x), gridWidth - w);
                    int iy = Math.min(Math.max(0, y), gridHeight - h);

                    @Override
                    public boolean hasNext() {
                        return iy <= y + h;
                    }

                    @Override
                    public AABB next() {
                        AABB tile = null;
                        try {
                            tile = grid[iy][ix];
                        } catch(ArrayIndexOutOfBoundsException e) {
                            return null;
                        } finally {
                            if (ix >= x + w) {
                                ix = Math.max(0, x);
                                iy++;
                            } else {
                                ix++;
                            }
                        }
                        return tile;
                    }

                    @Override
                    public void remove() {
                        // I don't actually know what this method is supposed to do
                    }
                };
            }
        };
    }

    /**
     * Replace or add a tile.
     *
     * @param body  Body to be added
     */
    @Override
    public void add(AABB body) {
        int x = (int) body.x / PS;
        int y = (int) body.y / PS;

        grid[y][x] = body;
    }

    /**
     * Remove the tile at the specified body location.
     *
     * @param body  Body to be removed
     */
    @Override
    public void remove(AABB body) {
        int x = (int) body.x / PS;
        int y = (int) body.y / PS;

        grid[y][x] = null;
    }

    /**
     * Scan the tiles for possible tile collisions with body.
     *
     * @param body  Body to be scanned
     * @return All tiles that possible collide with body
     */
    @Override
    public Iterable<AABB> scan(AABB body) {
        return range((int) body.x / PS, (int) body.y / PS, 1 + (int) body.width / PS, 1 + (int) body.height / PS);
    }

    /**
     * Do nothing, a tile grid shouldn't have to be updated.
     */
    @Override
    public void update() {}

    /**
     * Unimplemented on purpose because nobody should ever have to iterate over every tile in a layer. Ever!
     *
     * @return A nothing
     */
    @Override
    public Iterator<AABB> iterator() {
        return new ArrayList<AABB>().iterator();
    }
}
