package com.freddyheppell.cavegame.world.cells;


public abstract class Cell {
    public abstract String toString();
    public abstract boolean isBlocking();
    public abstract boolean isSpawnAllowed();
    public abstract void onEnter();

    public Cell(){}
}
