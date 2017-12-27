package com.freddyheppell.cavegame.player;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.world.coord.Coordinate;
import com.freddyheppell.cavegame.world.coord.Transform;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Coordinate worldCoordinate;

    public Player(Coordinate worldCoordinate) {
        this.worldCoordinate = worldCoordinate;

        int wx = worldCoordinate.x;
        int wy = worldCoordinate.y;
    }


    public Coordinate getWorldCoordinate() {
        return worldCoordinate;
    }

    public void moveCellCoordinate(Transform transform) {
        this.worldCoordinate = worldCoordinate.addTransform(transform);
    }

    public List<Coordinate> visibleCells() {
        int leftmostX = worldCoordinate.x - Config.VIEW_DISTANCE_H;
        int rightmostX = worldCoordinate.x + Config.VIEW_DISTANCE_H;
        int topmostY = worldCoordinate.y + Config.VIEW_DISTANCE_V;
        int bottomostY = worldCoordinate.y - Config.VIEW_DISTANCE_V;

        int hSize = (2 * Config.VIEW_DISTANCE_H) + 1;
        int vSize = (2 * Config.VIEW_DISTANCE_V) + 1;

        List<Coordinate> validCoords = new ArrayList<>();

        for (int x = leftmostX; x <= rightmostX; x++) {
            for (int y = bottomostY; y <= topmostY; y++) {
                Coordinate coordinate = new Coordinate(x, y);
                System.out.println("a" + coordinate);
                validCoords.add(coordinate);
            }
        }

        return validCoords;
    }
}
