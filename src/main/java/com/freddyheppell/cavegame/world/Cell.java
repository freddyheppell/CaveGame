package com.freddyheppell.cavegame.world;

public class Cell {
    public EnumCellType type;

    public Cell(EnumCellType enumCellType) {
        this.type = enumCellType;
    }


    public String toString() {
        return type.toString() + "  ";
    }
}
