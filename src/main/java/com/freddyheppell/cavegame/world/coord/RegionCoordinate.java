package com.freddyheppell.cavegame.world.coord;

import java.util.Objects;

public class RegionCoordinate {
    public int rx;
    public int ry;

    public RegionCoordinate(int rx, int ry) {
        this.rx = rx;
        this.ry = ry;
    }

    /** A representation of class data for comparison, HashMap keys etc.
     *
     * @return the object's hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(rx, ry);
    }

    /**
     * Determine if two coordinates are equal
     *
     * @param o The object for comparison
     * @return If the objects are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof RegionCoordinate)) {
            return false;
        }

        RegionCoordinate regionCoordinate = (RegionCoordinate) o;

        return rx == regionCoordinate.rx && ry == regionCoordinate.ry;
    }

    /**
     * Get a string representation of the coordinate
     *
     * @return
     */
    public String toString() {
        return String.format("R: (%d, %d)", rx, ry);
    }

}
