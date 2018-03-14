package com.freddyheppell.cavegame.world.cells;


import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

public abstract class Cell {
    public abstract String toString();
    public abstract boolean isBlocking();
    public abstract boolean isSpawnAllowed();
    public abstract void onEnter(Player player, RegionCoordinate regionCoordinate);

    public Cell(){}
}
