package com.freddyheppell.cavegame.world.cells;

import com.freddyheppell.cavegame.world.EnumCellType;

public class Cell {
    public final char character = 'X';
    public final boolean isBlocking = true;
    public final boolean allowSpawn = false;

    public String toString() {
        return character + "  ";
    }

    public void onEnter() {
        return;
    }
}
