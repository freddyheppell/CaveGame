package com.freddyheppell.cavegame.world;

public enum EnumCellType {
    UNSET ("-", false),
    FLOOR (" ", false),
    ROCK ("#", true),
    WALL ("%", true),
    PLAYER ("P", false);

    private final String name;
    private final boolean isBlocking;

    EnumCellType(String s, boolean isBlocking) {
        name = s;
        this.isBlocking = isBlocking;
    }

    public boolean equalsName(String otherName) {
        // (otherName == null) check is not needed because name.equals(null) returns false
        return name.equals(otherName);
    }

    public boolean isBlocking() {
        return isBlocking;
    }

    public String toString() {
        return this.name;
    }
}
