package com.freddyheppell.cavegame.world.cells;

import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;

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
