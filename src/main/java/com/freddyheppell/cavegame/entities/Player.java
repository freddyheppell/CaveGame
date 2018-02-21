package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.items.Item;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    ArrayList<Item> inventory = new ArrayList<>();

    public Player(WorldCoordinate worldCoordinate) {
        super(worldCoordinate);
    }

    /**
     * Calculate the player's rectangular viewing range
     *
     * @return A list of cells visible to the player
     */
    @Override
    public List<WorldCoordinate> visibleCells() {
        int leftmostX = worldCoordinate.wx - Config.PLAYER_VIEW_DISTANCE_H;
        int rightmostX = worldCoordinate.wx + Config.PLAYER_VIEW_DISTANCE_H;
        int topmostY = worldCoordinate.wy + Config.PLAYER_VIEW_DISTANCE_V;
        int bottommostY = worldCoordinate.wy - Config.PLAYER_VIEW_DISTANCE_V;

        int hSize = (2 * Config.PLAYER_VIEW_DISTANCE_H) + 1;
        int vSize = (2 * Config.PLAYER_VIEW_DISTANCE_V) + 1;

        List<WorldCoordinate> validCoords = new ArrayList<>();

        for (int y = topmostY; y >= bottommostY; y--) {
            for (int x = leftmostX; x <= rightmostX; x++) {
                WorldCoordinate worldCoordinate = new WorldCoordinate(x, y);
                validCoords.add(worldCoordinate);
            }
        }

        return validCoords;
    }

    /**
     * Add a single item to the inventory
     *
     * @param item The single item
     */
    public void addItem(Item item) {
        inventory.add(item);
    }

    public void inventoryDebug() {
        System.out.println("INVENTORY");
        for (Item item :
                inventory) {
            System.out.println(item.getDisplayName());

        }
        System.out.println("END INVENTORY");
    }

    /**
     * Add multiple items to the inventory
     *
     * @param items An ArrayList of items
     */
    public void addItems(ArrayList<Item> items) {
        inventory.addAll(items);
    }

    public String toString() {
        return "P";
    }
}
