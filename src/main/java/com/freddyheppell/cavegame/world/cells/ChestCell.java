package com.freddyheppell.cavegame.world.cells;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.entities.Player;
import com.freddyheppell.cavegame.items.Item;
import com.freddyheppell.cavegame.items.ItemRegistry;

import java.util.ArrayList;
import java.util.Random;

public class ChestCell extends Cell {
    /**
     * The items to be given to the player
     */
    private ArrayList<Item> reward = new ArrayList<>();
    /**
     * If the chest has been claimed yet or not
     */
    private boolean claimed = false;
    /**
     * A reference to the chest's location so it can instruct the save manager to
     * re-save the region when the chest has been accessed
     */
    private String locationHash;

    @Override
    public String toString() {
        if (claimed) {
            return "□";
        } else {
            return "■";
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
    public void onEnter(Player player) {
        if (!claimed) {
            // Give the contents of the chest to the player
            player.addItems(reward);
            // Remove the reward as it no longer needs to be stored
            reward.clear();
            // Mark as claimed
            claimed = true;
        }
    }

    /**
     * Generate the contents of a chest cell
     */
    public void generate() {
        Random random = new Random();

        // This prevents 0 from being generated
        // nextInt generates from 0 to n, we want 1 to n. By generating from 0 to (n-1) and adding 1, we get from 1 to n
        int itemCount = random.nextInt(Config.CHEST_MAX_ITEMS - 1) + 1;

        for (int i = 0; i < itemCount; i++) {
            // Now pick itemCount number of items
            reward.add(ItemRegistry.getInstance().selectItem());
        }
    }
}
