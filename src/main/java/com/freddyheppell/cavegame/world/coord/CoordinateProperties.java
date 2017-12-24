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
}
