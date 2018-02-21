package com.freddyheppell.cavegame.world.cells;


import com.freddyheppell.cavegame.entities.Player;

public abstract class Cell {
    public abstract String toString();
    public abstract boolean isBlocking();
    public abstract boolean isSpawnAllowed();
    public abstract void onEnter(Player player);

    public Cell(){}
}
