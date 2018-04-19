package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.CaveGame;
import com.freddyheppell.cavegame.config.Config;
import com.freddyheppell.cavegame.items.ArmourItem;
import com.freddyheppell.cavegame.items.Item;
import com.freddyheppell.cavegame.items.ItemRegistry;
import com.freddyheppell.cavegame.items.SwordItem;
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
    private int iEquippedArmour;

    private WorldCoordinate location;
    private transient int moveCounter = 0;
    private static final Logger logger = LogManager.getLogger();

    public Player(WorldCoordinate location) {
        super();
        this.location = location;

        // Add starter inventory
        inventory.add(ItemRegistry.getInstance().starterArmour);
        inventory.add(ItemRegistry.getInstance().starterSword);

        // Set equipped index values
        iEquippedArmour = 0;
        iEquippedWeapon = 1;
        resetStats();
    }

    @Override
    public int getViewDistance() {
        return 0;
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
        logger.debug("Running afterMove event for " + newCoordinate);
        if (world.getCell(newCoordinate).listener != null) {
            logger.debug("Coordinate " + newCoordinate + " has an event");
            // An entity somewhere has requested that they be alerted when a user enters this cell
            // First find the coordinates of that entity
            WorldCoordinate triggerCoordinate = world.getCell(newCoordinate).listener;

            // Check if the player has LoS to the entity
            logger.info("Checking LoS");
            if (world.hasLineOfSight(newCoordinate, triggerCoordinate)) {
                // If it does, begin combat
                Entity entity = world.getRegion(triggerCoordinate.getRegionCoordinate()).getEntityAt(triggerCoordinate);
                CombatManager combatManager = new CombatManager(this, entity);
                combatManager.simulate();
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

    @Override
    public float getStartingHealth() {
        return 50f;
    }

    @Override
    public int getAttackDamage() {
        return getEquippedWeapon().getDamage();
    }

    @Override
    public int getStartingArmour() {
        return getEquippedArmour().getShielding();

    }


    public ArmourItem getEquippedArmour() {
        // Cast the item to its polymorphic type
        return (ArmourItem) inventory.get(iEquippedArmour);
    }

    public SwordItem getEquippedWeapon() {
        // Cast the item to its polymorphic type
        return (SwordItem) inventory.get(iEquippedWeapon);
    }
}
