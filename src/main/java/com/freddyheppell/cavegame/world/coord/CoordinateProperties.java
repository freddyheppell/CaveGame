package com.freddyheppell.cavegame.world.coord;

public class CoordinateProperties {
    /**
     * An array of transforms for adjacent cells
     * This is the Moore neighbourhood, excluding the starting cell
     */
    public static final Transform[] adjacentTransforms = {
            new Transform(-1, 1),
            new Transform(-1, -1),
            new Transform(-1, 0),
            new Transform(0, 1),
            new Transform(0, -1),
            new Transform(1, 1),
            new Transform(1, 0),
            new Transform(1, -1),
    };

    /**
     * Transforms for directly adjacent cells
     */
    public static final Transform UP = new Transform(0, 1);
    public static final Transform LEFT = new Transform(1, 0);
    public static final Transform DOWN = new Transform(0, -1);
    public static final Transform RIGHT = new Transform(-1, 0);

    public static WorldCoordinate getSpiralCoordinate(int n) {
        int a = (int) Math.ceil((Math.sqrt((double) n) - 1) / 2);
        int t = 2 * a + 1;
        int c = (int) Math.pow(t, 2);
        t = t - 1;

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
            return new WorldCoordinate(a, (a - (c - n - t)));
        }
    }
}
