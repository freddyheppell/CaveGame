package com.freddyheppell.cavegame.world.cells;

import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;

/**
 * Represents the cells the player can travel through
 * Overridden methods are documented in the parent class
 */
public class FloorCell extends Cell {
    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public boolean isSpawnAllowed() {
        return true;
    }

    @Override
    public void onEnter(Player player, RegionCoordinate regionCoordinate) {

    }

    @Override
    public String toString() {
        return " ";
    }

}
