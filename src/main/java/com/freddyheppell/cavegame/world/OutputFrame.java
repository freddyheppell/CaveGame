package com.freddyheppell.cavegame.world;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.entities.Entity;
import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

import java.util.List;

/**
 * Represents a frame to be outputted to the user
 */
public class OutputFrame {
    /**
     * The instance of the world at this point
     */
    private World world;

    /**
     * The instance of the player at this point
     */
    private Player player;

    public OutputFrame(World world, Player player) {
        this.world = world;
        this.player = player;
    }

    /**
     * Turn this frame into a string
     *
     * @return Multiline string of current game state
     */
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

                if (entity.alive) {
                    // Only show the entity if it is alive
                    outputString.append(entity);
                } else {
                    // Just show a blank space otherwise
                    outputString.append(" ");
                }
            } else {
                // Otherwise show the cell's character
                outputString.append(world.getCell(worldCoordinate));
            }

            outputString.append("  ");

            lastY = worldCoordinate.wy;
        }

        if (Config.getBoolean("bShowDebugInfo")) {
            outputString.append("\n").append(player.getLocation());
        }

        return outputString.toString();
    }
}
