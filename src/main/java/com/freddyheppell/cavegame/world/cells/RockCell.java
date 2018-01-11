package com.freddyheppell.cavegame.world.cells;

public class RockCell extends Cell {
    private char character = '#';

    @Override
    public String toString() {
        return character + "  ";
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
