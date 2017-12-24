package com.freddyheppell.cavegame.world;

public enum EnumCellType {
    UNSET ("-"),
    FLOOR ("."),
    ROCK ("#"),
    WALL ("%");

    private final String name;

    EnumCellType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
