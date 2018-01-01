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
        // floorDiv divides, finds the floor and casts to an integer
        this.rx = Math.floorDiv(wx, Config.REGION_SIZE);
        this.ry = Math.floorDiv(wy, Config.REGION_SIZE);

        // Find the coordinates within that region
        // floorMod is required for negative coordinates to work
        this.cx = Math.floorMod(wx, Config.REGION_SIZE);
        this.cy = Math.floorMod(wy, Config.REGION_SIZE);
    }

    public static WorldCoordinate origin() {
        return new WorldCoordinate(0,0);
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

    /**
     * A representation of class data for comparison, HashMap keys etc.
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
