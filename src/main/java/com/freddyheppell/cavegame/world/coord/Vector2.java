package com.freddyheppell.cavegame.world.coord;

/**
 * Represents a 2-dimensional vector in the x,y plane
 */
public class Vector2 {
    public int dx;

    public int dy;

    /**
     * Create a new Vector2 instance
     *
     * @param dx The difference in x
     * @param dy The difference in y
     */
    public Vector2(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Get the magnitude of the vector
     *
     * @return The transform's vector
     */
    public double magnitude() {
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
