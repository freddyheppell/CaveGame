package com.freddyheppell.cavegame.world.coord;

import java.util.Objects;

public class RegionCoordinate {
    public int rx;
    public int ry;

    public RegionCoordinate(int rx, int ry) {
        this.rx = rx;
        this.ry = ry;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rx, ry);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof RegionCoordinate)) {
            return false;
        }

        RegionCoordinate regionCoordinate = (RegionCoordinate) o;

        return rx == regionCoordinate.rx && rx == regionCoordinate.ry;
    }

    public String toString() {
        return String.format("R: (%d, %d)", rx, ry);
    }

}
