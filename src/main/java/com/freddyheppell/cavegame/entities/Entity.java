package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.world.World;
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

    /**
     * Get the current world coordinate of the Entity
     *
     * @return The entity's world coordinates
     */
    public WorldCoordinate getWorldCoordinate() {
        return worldCoordinate;
    }

    /**
     * Get the cells that are visible to the entity;
     *
     * @return A `List` of visible cells
     */
    public List<WorldCoordinate> visibleCells() {
        return new ArrayList<>();
    }

    /**
     * Move the entity by a transform
     *
     * @param transform the `Transform` to move the entity by
     */
    public void move(Transform transform, World world) {
        WorldCoordinate newCoordinate = worldCoordinate.addTransform(transform);

        if (world.getCell(newCoordinate).type.isBlocking()) {
            // If the move is not valid, do not perform it
            return;
        } else {
            // If it is valid, change the player's coordinates
            worldCoordinate = newCoordinate;
        }
    }
}
