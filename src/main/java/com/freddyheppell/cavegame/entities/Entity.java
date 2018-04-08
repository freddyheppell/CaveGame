package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.CaveGame;
import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.cells.Cell;
import com.freddyheppell.cavegame.world.coord.Transform;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

import java.util.ArrayList;
import java.util.List;

public class Entity {
    protected transient List<WorldCoordinate> visibleCells;

    /**
     * The Entity class should not be directly instantiated
     */
    public Entity() {
    }

    /**
     * Get the radial view distance that the entity can see within
     *
     * @return The radial view distance
     */
    public int getViewDistance() {
        return 3;
    }

    /**
     * Get the cells that are visible to the entity
     */
    public void calculateVisibleCells(WorldCoordinate location) {
        this.visibleCells = new ArrayList<>();
        int r = getViewDistance();
        int rSq = r*r;
        float correction = r * 0.8f;
        float limit = rSq + correction;

        for (int y = (-r + location.wy); y <= (r + location.wy); y++) {
            for (int x = (-r + location.wx); x <= (r + location.wy); x++) {
                if ((x*x) + (y*y) <= limit) {
                    visibleCells.add(new WorldCoordinate(x, y));
                }
            }
        }

        registerVisibleCells(location);
    }

    /**
     * Register events for the visible cells
     *
     * @param location The location of this entity
     */
    private void registerVisibleCells(WorldCoordinate location) {
        for (WorldCoordinate visibleCell:
             visibleCells) {
            CaveGame.game.registerEvent(location, visibleCell);
        }
    }

    public List<WorldCoordinate> getVisibleCells() {
        return visibleCells;
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
