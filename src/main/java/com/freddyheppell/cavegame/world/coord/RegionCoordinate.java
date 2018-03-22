package com.freddyheppell.cavegame.world.coord;

import com.freddyheppell.cavegame.config.Config;

import java.util.Objects;

public class RegionCoordinate {
    public int rx;
    public int ry;

    public RegionCoordinate(int rx, int ry) {
        this.rx = rx;
        this.ry = ry;
    }


    /**
     * Get a hash representation of the coordinate
     *
     * @return The hash of the coordinate
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
     * Returns the bottom left corner
     *
     * @return The world coordinate of the corner
     */
    public WorldCoordinate getCornerWorldCoordinate() {
        return new WorldCoordinate(rx * Config.getInt("iRegionSize"), ry * Config.getInt("iRegionSize"));
    }

    /**
     * Get a string representation of the coordinate
     *
     * @return The coordinate as an ordered pair
     */
    public String toString() {
        return String.format("R: (%d, %d)", rx, ry);
    }

}
