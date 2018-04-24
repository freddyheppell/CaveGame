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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void showInventory() throws IOException {
        boolean showInv = true;

        while (showInv) {
            Console.clearScreen();

            System.out.println("Your Inventory");

            StringBuilder invString = new StringBuilder();

            for (int i = 0; i < inventory.size(); i++) {
                // For each item, append it's human readable number and name
                invString.append(i + 1).append(": ")
                        .append(inventory.get(i).getDisplayName());

                if (i == iEquippedArmour || i == iEquippedWeapon) {
                    // If its currently equipped, show that
                    invString.append(" (Equipped)");
                }

                invString.append("\n");
            }

            System.out.print(invString);

            System.out.println();

            boolean validString = false;

            while (!validString) {
                // Ask the user for a command
                System.out.println("x Exit, e<num> to equip");
                System.out.print("> ");
                String inputString = Console.readLine();
                inputString = inputString.toLowerCase();

                if (inputString.charAt(0) == 'x') {
                    validString = true;
                    showInv = false;
                } else if (inputString.charAt(0) == 'e' && inputString.length() != 1) {
                    // If the string begins with an 'e' and contains further characters
                    // parse it as an equip request

                    // Remove the leading 'e'
                    String equipString = inputString.substring(1);

                    // Attempt to parse the remainder as an int
                    int newIndex;

                    try {
                        newIndex = Integer.parseInt(equipString);
                    } catch (NumberFormatException e) {
                        System.out.println("! Invalid number !");

                        // Ask for input again
                        continue;
                    }

                    if (newIndex < (inventory.size() + 1) && newIndex > 0) {
                        // Check if it's within range
                        // This doesn't use the dedicated function because there are other criteria
                        // that could cause an input to be invalid

                        // Subtract one because the inventory list starts at 0
                        newIndex--;

                        Item item = inventory.get(newIndex);

                        if (item.isEquippable()) {
                            // Equip as armour
                            iEquippedArmour = newIndex;
                            System.out.println("Equipped!");
                            validString = true;
                        } else if (item.isWeapon()) {
                            iEquippedWeapon = newIndex;
                            System.out.println("Equipped!");
                            validString = true;
                        } else {
                            System.out.println("! Cannot be equipped !");
                        }
                    }
                } else {
                    // Not one of the recognised commands
                    System.out.println("! Unrecognised command !");

                }
            }
        }
    }

    /**
     * Add multiple items to the inventory
     *
     * @param items An ArrayList of items
     */
    public void addItems(ArrayList<Item> items) {
        for (Item item :
                items) {
            item.onItemSelect(); // Execute this item's select event
            inventory.add(item);
        }

        restack();
    }

    /**
     * Stack items in the inventory that can be stacked
     */
    private void restack() {
        // Store the types of each item and the index of where it was first found
        // Primitive ints cannot be used for lists or maps, so the Integer class must be used instead
        Map<String, Integer> typeStrings = new HashMap<>();
        ArrayList<Integer> toRemove = new ArrayList<>();

        for (int i = 0; i < inventory.size(); i++) {
            Item item = inventory.get(i);
            if (item.isStackable() && !typeStrings.containsKey(item.getStackName())) {
                // This is a stackable item that doesn't exist on the types list
                // Add it to the types list
                typeStrings.put(item.getStackName(), i);
            } else if (item.isStackable() && typeStrings.containsKey(item.getStackName())){
                // This is a stackable item and already exists in the types list
                // So stack it with the previous instance
                int firstI = typeStrings.get(item.getStackName());
                Item firstItem = inventory.get(firstI);
                firstItem.quantity += item.quantity;

                // And mark it for removal
                toRemove.add(i);
            }
        }

        // Now remove the entries to be removed
        for (Integer aToRemove : toRemove) {
            // This has to be casted back to a primitive int
            inventory.remove((int) aToRemove);
        }
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

                CaveGame.game.setPlayer(combatManager.getPlayer());

                if (!combatManager.getEnemy().isAlive()) {
                    // If the enemy has died, remove it
                    return;
                }
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

    public void setLocation(WorldCoordinate location) {
        this.location = location;
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
