package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.cells.Cell;
import com.freddyheppell.cavegame.world.coord.Transform;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    protected WorldCoordinate location;
    protected List<WorldCoordinate> visibleCells;

    /**
     * The Entity class should not be directly instantiated
     *
     * @param location The location of the entity
     */
    public Entity(WorldCoordinate location) {
        this.location = location;
    }

    /**
     * Get the current world coordinate of the Entity
     *
     * @return The entity's world coordinates
     */
    public WorldCoordinate getLocation() {
        return location;
    }

    /**
     * Get the radial view distance that the entity can see within
     *
     * @return The radial view distance
     */
    public int getViewDistance() {
        return 0;
    }

    /**
     * Get the cells that are visible to the entity
     */
    public void calculateVisibleCells() {
        ArrayList<WorldCoordinate> visibleCells = new ArrayList<>();
        int r = getViewDistance();
        // a is the decision variable
        int a = 1 - r;
        int dx = 1;
        int dy = -2 * r;
        int x = 0;
        int y = r;

        // Add the point at the top of the circle
        visibleCells.add(new WorldCoordinate(location.wx, location.wy + r));
        // ... and the other points at the right, bottom and left.
        visibleCells.add(new WorldCoordinate(location.wx, location.wy - r));
        visibleCells.add(new WorldCoordinate(location.wx + r, location.wy));
        visibleCells.add(new WorldCoordinate(location.wx - r, location.wy));

        // Bresenham's Circle Algorithm
        // Used to rasterise a circle onto the coordinate grid
        while (x < y) {
            if (a >= 0) {
                y -= 1;
                dy += 2;
                a += dy;
            }

            x += 1;
            dx += 2;
            a += dx;

            // Add the corresponding points in all eight octants
            visibleCells.add(new WorldCoordinate(location.wx + x, location.wy + y));
            visibleCells.add(new WorldCoordinate(location.wx - x, location.wy + y));
            visibleCells.add(new WorldCoordinate(location.wx + x, location.wy - y));
            visibleCells.add(new WorldCoordinate(location.wx - x, location.wy - y));
            visibleCells.add(new WorldCoordinate(location.wx + y, location.wy + x));
            visibleCells.add(new WorldCoordinate(location.wx - y, location.wy + x));
            visibleCells.add(new WorldCoordinate(location.wx + y, location.wy - x));
            visibleCells.add(new WorldCoordinate(location.wx - y, location.wy - x));

        }
    }

    public List<WorldCoordinate> getVisibleCells() {
        return visibleCells;
    }

    /**
     * Move the entity by a transform
     *
     * @param transform the `Transform` to move the entity by
     * @param world     The world instance to verify that the transform leads to a valid cell
     */
    public void move(Transform transform, World world) {
        WorldCoordinate newCoordinate = location.addTransform(transform);
        Cell targetCell = world.getCell(newCoordinate);

        if (targetCell.isBlocking()) {
            // If the move is not valid, do not perform it
            System.out.println("BAD MOVE");
        } else {
            // If it is valid, change the player's coordinates
            location = newCoordinate;
            afterMove(newCoordinate, world);
        }
    }

    /**
     * Event function to be executed after the entity moves
     *
     * @param newCoordinate The location of the player now that the move has been performed
     * @param world         The world instance the move took place in
     */
    public void afterMove(WorldCoordinate newCoordinate, World world) {

    }

    /**
     * @return The string representation of the entity
     */
    public String toString() {
        return "";
    }
}
