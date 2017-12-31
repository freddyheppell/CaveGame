package com.freddyheppell.cavegame.world.coord;

public class Transform {
    public int dx;
    public int dy;

    /**
     * Create a new Transform instance
     *
     * @param dx The difference in x
     * @param dy The difference in y
     */
    public Transform(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Get the magnitude of the transform
     *
     * @return The transform's magnitude
     */
    public double magnitude() {
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
