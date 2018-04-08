package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.CaveGame;
import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.items.Item;
import com.freddyheppell.cavegame.save.SaveManager;
import com.freddyheppell.cavegame.utility.Console;
import com.freddyheppell.cavegame.world.Region;
import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.cells.Cell;
import com.freddyheppell.cavegame.world.coord.Transform;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {
    private ArrayList<Item> inventory = new ArrayList<>();

    // The indexes of the equipped weapon and armor
    private int iEquippedWeapon;
    private int iEquippedArmor;

    private WorldCoordinate location;
    private transient int moveCounter = 0;
    private static final Logger logger = LogManager.getLogger();

    public Player(WorldCoordinate location) {
        super();
        this.location = location;
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
     * @param world         The world the player is in
     */
    @Override
    public void afterMove(WorldCoordinate newCoordinate, World world) {
        if (world.getRegion(newCoordinate.getRegionCoordinate()).hasEventForCoordinate(newCoordinate)) {
            // An entity somewhere has requested that they be alerted when a user enters this cell
            // First find the coordinates of that entity
            WorldCoordinate triggerCoordinate = world.getRegion(newCoordinate.getRegionCoordinate()).getEventForCoordinate(newCoordinate);

            // Check if the player has LoS to the entity
            if (world.hasLineOfSight(newCoordinate, triggerCoordinate)) {
                // If it does, begin combat
                Entity entity = world.getRegion(triggerCoordinate.getRegionCoordinate()).getEntityAt(triggerCoordinate);
                CombatManager combatManager = new CombatManager(this, entity);

            }
        }

        // Alert the cell that the player has entered the cell
        world.getCell(newCoordinate).onEnter(this, newCoordinate.getRegionCoordinate());
    }

    /**
     * Move the entity by a transform
     *
     * @param transform the `Transform` to move the entity by
     * @param world     The world instance to verify that the transform leads to a valid cell
     */
    public void move(Transform transform, World world) {
        WorldCoordinate newCoordinate = location.addTransform(transform);
        Cell targetCell = world.getCell(newCoordinate);

        if (targetCell.isBlocking()) {
            // If the move is not valid, do not perform it
            logger.warn("Invalid move attempted");
        } else {
            // If it is valid, change the player's coordinates
            location = newCoordinate;

            // Increment the move counter
            moveCounter++;

            if (moveCounter >= Config.getInt("iPlayerSaveFrequency")) {
                moveCounter = 0;
                CaveGame.game.savePlayer();
            }

            afterMove(newCoordinate, world);
        }
    }

    /**
     * Get the current world coordinate of the Entity
     *
     * @return The entity's world coordinates
     */
    public WorldCoordinate getLocation() {
        return location;
    }

    public String toString() {
        return "P";
    }
}
