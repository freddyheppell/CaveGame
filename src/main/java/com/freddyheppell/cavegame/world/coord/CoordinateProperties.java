package com.freddyheppell.cavegame.world.coord;

/**
 * Data class that holds information about the coordinate system
 */
public class CoordinateProperties {
    /**
     * An array of vectors for adjacent cells
     * This is the Moore neighbourhood, excluding the starting cell
     */
    public static final Vector2[] ADJACENT_VECTORS = {
            new Vector2(-1, 1),
            new Vector2(-1, -1),
            new Vector2(-1, 0),
            new Vector2(0, 1),
            new Vector2(0, -1),
            new Vector2(1, 1),
            new Vector2(1, 0),
            new Vector2(1, -1),
    };

    /**
     * Vectors for directly adjacent cells
     */
    public static final Vector2 UP = new Vector2(0, 1);
    public static final Vector2 LEFT = new Vector2(1, 0);
    public static final Vector2 DOWN = new Vector2(0, -1);
    public static final Vector2 RIGHT = new Vector2(-1, 0);

    /**
     * Generate an all-int Ulam Spiral
     *
     * @param n The value to get the coordinates of
     * @return The coordinates of the nth value
     */
    public static WorldCoordinate getSpiralCoordinate(int n) {
        // This is the 'round' the current value is on
        int a = (int) Math.ceil((Math.sqrt((double) n) - 1) / 2);
        int t = 2 * a + 1;
        int c = (int) Math.pow(t, 2);
        t = t - 1;

        // The cell could be in either of the four quadrants
        // When n>=(c-t), it must be in the current quadrant, going clockwise

        if (n >= (c - t)) {
            return new WorldCoordinate((a - (c - n)), -a);
        } else {
            c -= t;
        }

        if (n >= (c - t)) {
            return new WorldCoordinate(-a, (-a + (c - n)));
        } else {
            c -= t;
        }

        if (n >= (c - t)) {
            return new WorldCoordinate((-a + (c - n)), a);
        } else {
            // If it isn't in the first three quadrants it must be in the fourth
            return new WorldCoordinate(a, (a - (c - n - t)));
        }
    }
}
