package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.entities.Entity;
import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class OutputFrame {
    private World world;
    private Player player;

    private static final Logger logger = LogManager.getLogger();

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

        List<WorldCoordinate> visibleCells = player.getVisibleCells();
        int lastY = visibleCells.get(0).wy;

        for (WorldCoordinate worldCoordinate : visibleCells) {
            if (worldCoordinate.wy != lastY) {
                outputString.append("\n");
            }

            // Decide what character should be shown for this coordinate
            if (worldCoordinate.equals(player.getLocation())) {
                // If the player is on this cell, show the player character
                outputString.append(player);
            } else if (world.getRegion(worldCoordinate.getRegionCoordinate()).isEntityAt(worldCoordinate)) {
                Entity entity = world.getRegion(worldCoordinate.getRegionCoordinate()).getEntityAt(worldCoordinate);
                outputString.append(entity);
            } else if (world.getCell(worldCoordinate).listener != null){
                outputString.append("!");
            }
            else{
                // Otherwise show the cell's character
                outputString.append(world.getCell(worldCoordinate));
            }

            outputString.append("  ");

            lastY = worldCoordinate.wy;
        }

        return outputString.toString();
    }
}
