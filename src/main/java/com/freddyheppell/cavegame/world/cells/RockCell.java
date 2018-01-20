package com.freddyheppell.cavegame.world.cells;

public class RockCell extends Cell {

    @Override
    public String toString() {
        return "#" + "  ";
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
    public void onEnter() {

    }

}
