package com.freddyheppell.cavegame.world.cells;

import com.freddyheppell.cavegame.CaveGame;
import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.items.Item;
import com.freddyheppell.cavegame.items.ItemRegistry;
import com.freddyheppell.cavegame.world.coord.RegionCoordinate;

import java.util.ArrayList;

/**
 * Represents a chest cell which provides the player with a reward when they enter it
 * Overridden methods are documented in the parent class
 */
public class ChestCell extends Cell {
    /**
     * The items to be given to the player
     */
    private ArrayList<Item> reward;

    /**
     * If the chest has been claimed yet or not
     */
    private boolean claimed = false;

    @Override
    public String toString() {
        if (claimed) {
            return " ";
        } else {
            return "C";
        }
    }

    @Override
    public boolean isBlocking() {
        return false;
    }

    @Override
    public boolean isSpawnAllowed() {
        return false;
    }

    @Override
    public void onEnter(Player player, RegionCoordinate regionCoordinate) {
        if (!claimed) {
            // Give the contents of the chest to the player
            player.addItems(reward);
            // Remove the reward as it no longer needs to be stored
            reward.clear();
            // Mark as claimed
            claimed = true;

            CaveGame.game.world.getRegionManager().resaveRegion(regionCoordinate);
            CaveGame.game.savePlayer();
        }
    }

    /**
     * Generate the contents of a chest cell
     */
    public void generate() {
        reward = ItemRegistry.getInstance().generateReward();
    }
}
