package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.player.Player;
import com.freddyheppell.cavegame.world.coord.Coordinate;

import java.util.List;

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

        StringBuilder outputString = new StringBuilder();

        List<Coordinate> visibleCells = player.visibleCells();
        int lastY = visibleCells.get(0).y;

        for (Coordinate coordinate : visibleCells) {
            if (coordinate.y != lastY) {
                outputString.append("\n");
            }

            if (coordinate.equals(player.getWorldCoordinate())) {
                outputString.append(EnumCellType.PLAYER + " ");
            } else {
                outputString.append(cells[coordinate.x][coordinate.y]);
            }

            lastY = coordinate.y;
        }

        return outputString.toString();
    }
}
