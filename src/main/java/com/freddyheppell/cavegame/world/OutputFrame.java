package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.world.cells.Cell;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

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
        System.out.println();
        Region region = world.region;
        Cell[][] cells = region.getCells();

        StringBuilder outputString = new StringBuilder();

        List<WorldCoordinate> visibleCells = player.visibleCells();
        int lastY = visibleCells.get(0).wy;

        for (WorldCoordinate worldCoordinate : visibleCells) {
            if (worldCoordinate.wy != lastY) {
                outputString.append("\n");
            }

            if (worldCoordinate.equals(player.getWorldCoordinate())) {
                outputString.append(EnumCellType.PLAYER).append("  ");
            } else {
//                outputString.append(worldCoordinate);
                outputString.append(cells[worldCoordinate.cx][worldCoordinate.cy]);
            }

            lastY = worldCoordinate.wy;
        }

        return outputString.toString();
    }
}
