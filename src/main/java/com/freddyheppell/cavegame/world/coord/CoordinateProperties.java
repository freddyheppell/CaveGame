package com.freddyheppell.cavegame.world.coord;

public class CoordinateProperties {
    public static final Transform[] adjacentTransforms = {
            new Transform(-1,1),
            new Transform(-1, -1),
            new Transform(-1, 0),
            new Transform(0, 1),
            new Transform(0, -1),
            new Transform(1,1),
            new Transform(1,0),
            new Transform(1, -1),
    };

    public static final Transform NORTH = new Transform(0, 1);
    public static final Transform EAST = new Transform(1, 0);
    public static final Transform SOUTH = new Transform(0, -1);
    public static final Transform WEST = new Transform(-1, 0);
}
