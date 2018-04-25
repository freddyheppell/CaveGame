package com.freddyheppell.cavegame.world.cells;

import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;

/**
 * Represents an empty cell that has yet to be filled by the generation algorithm
 * Note that this is not the same as a floor cell
 * The final world shouldn't contain any of these
 *
 * Overridden methods are documented in the parent class
 */
public class EmptyCell extends Cell {
    @Override
    public String toString() {
        return "X";
    }

    @Override
    public boolean isBlocking() {
        return true;
    }

    @Override
    public boolean isSpawnAllowed() {
        return false;
    }

    @Override
    public void onEnter(Player player, RegionCoordinate regionCoordinate) {

    }
}
