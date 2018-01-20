package com.freddyheppell.cavegame.world.cells;

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
    public void onEnter() {
    }

    @Override
    public String toString() {
        return "   ";
    }

}
