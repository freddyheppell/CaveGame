package com.freddyheppell.cavegame.world.cells;


public abstract class Cell {
    private char character = 'X';
    private boolean isBlocking = true;
    private boolean allowSpawn = false;

    public abstract String toString();
    public abstract boolean isBlocking();
    public abstract boolean isSpawnAllowed();
    public abstract void onEnter();
}
