package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.player.Player;
import com.freddyheppell.cavegame.world.coord.Coordinate;

public class OutputFrame {
    private World world;
    private Player player;

    public OutputFrame(World world, Player player) {
        this.world = world;
        this.player = player;
    }

//    public Cell[][] viewableCells() {
//
//    }

    public String toString() {
        Region region = world.region;
        Cell[][] cells = region.getCells();
        Coordinate playerCoordinates  = player.getWorldCoordinate();

        StringBuilder outputString = new StringBuilder();

        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[x].length; y++) {
                if (x == playerCoordinates.x && y == playerCoordinates.y) {
                    // The Player is in this cell
                    outputString.append("P  ");
                } else {
                    outputString.append(cells[y][x]);
                }
            }
            outputString.append("\n");
        }

        return outputString.toString();
    }
}
