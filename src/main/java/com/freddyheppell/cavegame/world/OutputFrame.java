package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
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

        StringBuilder outputString = new StringBuilder();

        List<WorldCoordinate> visibleCells = player.visibleCells();
        int lastY = visibleCells.get(0).wy;

        for (WorldCoordinate worldCoordinate : visibleCells) {
            if (worldCoordinate.wy != lastY) {
                outputString.append("\n");
            }

            if (worldCoordinate.equals(player.getWorldCoordinate())) {
                outputString.append(Config.PLAYER_INDICATOR).append("  ");
            } else {
                outputString.append(world.getCell(worldCoordinate)).append("  ");
            }

            lastY = worldCoordinate.wy;
        }

        return outputString.toString();
    }
}
