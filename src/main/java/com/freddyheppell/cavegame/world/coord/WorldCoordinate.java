package com.freddyheppell.cavegame.world.coord;

import com.freddyheppell.cavegame.config.Config;

import java.util.Objects;

public class WorldCoordinate {
    public int wx;
    public int wy;
    public int rx;
    public int ry;
    public int cx;
    public int cy;


    public WorldCoordinate(int wx, int wy) {
        this.wx = wx;
        this.wy = wy;

        // Find the region that (wx, wy) is in
        this.rx = Math.abs(wx / Config.REGION_SIZE);
        this.ry = Math.abs(wy / Config.REGION_SIZE);

        // Find the coordinates within that region
        // As all values are integers, integer division will be used, which is
        // the same as using the DIV operator
        this.cx = Math.abs(wx % Config.REGION_SIZE);
        this.cy = Math.abs(wy % Config.REGION_SIZE);
    }

    /**
     * Create a new coordinate that has been altered by the transform
     *
     * @param transform
     * @return
     */
    public WorldCoordinate addTransform(Transform transform) {
        int newX = wx + transform.dx;
        int newY = wy + transform.dy;

        return new WorldCoordinate(newX, newY);
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
        if (!(o instanceof WorldCoordinate)) {
            return false;
        }

        WorldCoordinate worldCoordinate = (WorldCoordinate) o;

        return wx == worldCoordinate.wx && wy == worldCoordinate.wy;
    }

    /**
     * Get the Region coordinates as an instance of the RegionCoordinate class
     *
     * @return the region coordinate
     */
    public RegionCoordinate getRegionCoordinate() {
        return new RegionCoordinate(rx, ry);
    }

    /** A representation of class data for comparison, HashMap keys etc.
     *
     * @return the object's hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(wx, wy);
    }

    /**
     * Get a string representation of the coordinate
     *
     * @return
     */
    public String toString() {
        return String.format("W:(%d,%d), R: (%d, %d), C: (%d, %d)", wx, wy, rx, ry, cx, cy);
    }

}
