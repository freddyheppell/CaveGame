package com.freddyheppell.cavegame.world.cells;

import com.freddyheppell.cavegame.entities.Player;

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
    public void onEnter(Player player) {

    }

    @Override
    public String toString() {
        return "";
    }

}
