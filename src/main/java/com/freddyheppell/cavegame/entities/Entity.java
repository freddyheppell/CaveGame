package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.cells.Cell;
import com.freddyheppell.cavegame.world.coord.Transform;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    protected WorldCoordinate worldCoordinate;

    /**
     * The Entity class should not be directly instantiated
     * @param worldCoordinate The location of the entity
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
        // TODO: Radial view area
        return new ArrayList<>();
    }

    /**
     * Move the entity by a transform
     *
     * @param transform the `Transform` to move the entity by
     * @param world The world instance to verify that the transform leads to a valid cell
     */
    public void move(Transform transform, World world) {
        WorldCoordinate newCoordinate = worldCoordinate.addTransform(transform);
        Cell targetCell = world.getCell(newCoordinate);
        System.out.println(newCoordinate);

        if (targetCell.isBlocking()) {
            // If the move is not valid, do not perform it
            System.out.println("BAD MOVE");
        } else {
            // If it is valid, change the player's coordinates
            worldCoordinate = newCoordinate;
        }
    }

    public String toString() {
        return "";
    }
}
