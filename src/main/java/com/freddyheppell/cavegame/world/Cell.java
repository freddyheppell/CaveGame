package com.freddyheppell.cavegame.world;

public class Cell {
    public EnumCellType type;

    public Cell(EnumCellType enumCellType) {
        this.type = enumCellType;
    }


    public String toString() {
        return type.toString() + "  ";
    }

    public boolean isBlocking() {
        return this.type.isBlocking();
    }

    public boolean isNonBlocking() {
        return !isBlocking();
    }
}
