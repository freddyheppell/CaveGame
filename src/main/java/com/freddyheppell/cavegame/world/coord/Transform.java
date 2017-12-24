package com.freddyheppell.cavegame.world.coord;

public class Transform {
    public int dx;
    public int dy;

    public Transform(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double magnitude() {
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }
}
