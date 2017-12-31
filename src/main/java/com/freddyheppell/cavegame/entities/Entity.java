package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.world.coord.Transform;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    protected WorldCoordinate worldCoordinate;
    private int viewDistance = 0;
    private char entityChar = 'X';

    /**
     * The Entity class should not be directly instantiated
     */
    public Entity(WorldCoordinate worldCoordinate) {
        this.worldCoordinate = worldCoordinate;
    }

    public WorldCoordinate getWorldCoordinate() {
        return worldCoordinate;
    }

    public List<WorldCoordinate> visibleCells() {
        return new ArrayList<>();
    }

    public void move(Transform transform) {
        this.worldCoordinate = worldCoordinate.addTransform(transform);
    }
}
