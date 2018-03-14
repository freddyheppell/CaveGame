package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.items.Item;
import com.freddyheppell.cavegame.utility.Console;
import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    private ArrayList<Item> inventory = new ArrayList<>();

    public Player(WorldCoordinate worldCoordinate) {
        super(worldCoordinate);
    }

    /**
     * Calculate the player's rectangular viewing range
     *
     * @return A list of cells visible to the player
     */
    @Override
    public List<WorldCoordinate> getVisibleCells() {
        int h = Console.getWidth();
        int v = Console.getHeight();
        int leftmostX = location.wx - h;
        int rightmostX = location.wx + h;
        int topmostY = location.wy + v;
        int bottommostY = location.wy - v;

        int hSize = (2 * h) + 1;
        int vSize = (2 * v) + 1;

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

    public void showInventory() {
        System.out.println("INVENTORY");
        for (Item item :
                inventory) {
            System.out.println(item.getDisplayName());

        }
        System.out.println("END INVENTORY");
        Console.readInput();
    }

    /**
     * Add multiple items to the inventory
     *
     * @param items An ArrayList of items
     */
    public void addItems(ArrayList<Item> items) {
        inventory.addAll(items);
    }

    /**
     * When a player has moved, alert the cell
     *
     * @param newCoordinate The player's new location
     * @param world The world the player is in
     */
    @Override
    public void afterMove(WorldCoordinate newCoordinate, World world) {
        world.getCell(newCoordinate).onEnter(this, newCoordinate.getRegionCoordinate());
    }

    public String toString() {
        return "P";
    }
}
