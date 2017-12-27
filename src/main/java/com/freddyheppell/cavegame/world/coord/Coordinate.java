package com.freddyheppell.cavegame.world.coord;

import java.util.Objects;

public class Coordinate {
    public int x;
    public int y;

    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate addTransform(Transform transform) {
        int newX = x + transform.dx;
        int newY = y + transform.dy;

        return new Coordinate(newX, newY);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Coordinate)) {
            return false;
        }

        Coordinate coordinate = (Coordinate) o;

        return x == coordinate.x && y == coordinate.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public String toString() {
        return String.format("(%d,%d)", x, y);
    }
}
