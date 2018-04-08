package com.freddyheppell.cavegame.world.cells;


import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;

public abstract class Cell {
    public abstract String toString();

    /**
     * Check if the player can move through this cell type
     *
     * @return If the cell is blocking
     */
    public abstract boolean isBlocking();

    /**
     * Check if the player can spawn on this cell type
     *
     * @return If the player can spawn on this cell type
     */
    public abstract boolean isSpawnAllowed();

    /**
     * Event function for when the player enters this cell
     *
     * @param player The player instance
     * @param regionCoordinate The region in which this cell is located
     */
    public abstract void onEnter(Player player, RegionCoordinate regionCoordinate);

    /**
     * Empty constructor for serialisation
     */
    public Cell() {
    }
}
