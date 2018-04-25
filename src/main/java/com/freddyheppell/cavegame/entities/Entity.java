package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.CaveGame;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Abstract class describing the behaviour of an entity
 */
public abstract class Entity {
    /**
     * Cells that are visible to the entity
     */
    private transient List<WorldCoordinate> visibleCells;

    /**
     * The entity's health
     */
    private float health;

    /**
     * If the entity is currently alive
     * This is safe to use outside of combat
     */
    public boolean alive = true;

    /**
     * The maximum amount of armour the player has
     */
    private Integer armour;

    /**
     * The list of armour changes to apply to the player
     */
    private Queue<Integer> armourChange;

    private static final Logger logger = LogManager.getLogger();


    /**
     * The Entity class should not be directly instantiated
     */
    public Entity() {
        armourChange = new LinkedList<>();
    }

    /**
     * Reset the entity's armour and health
     */
    public void resetStats() {
        health = getStartingHealth();
        armour = getStartingArmour();

        // Reset the armour change queue and re-add the initial two zeros
        armourChange.clear();
        armourChange.add(0);
        armourChange.add(0);
    }

    /**
     * Get the radial view distance that the entity can see within
     *
     * @return The radial view distance
     */
    protected abstract int getViewDistance();

    /**
     * Get the cells that are visible to the entity
     *
     * @param location The current location of the entity
     */
    public void calculateVisibleCells(WorldCoordinate location) {
        this.visibleCells = new ArrayList<>();
        // Get the radius
        int r = getViewDistance();
        // Pre-compute r^2
        int rSq = r * r;
        // Make the acceptable radius slightly bigger to create a better circle shape
        float correction = r * 0.8f;
        float limit = rSq + correction;

        // Iterate through a square that surrounds the circle
        for (int y = -r; y <= r; y++) {
            for (int x = -r; x <= r; x++) {
                // If this coordinate is within the circle
                if ((x * x) + (y * y) <= limit) {
                    // ... add it to the list of visible cells
                    // Add the coordinates of the entity to transform the circle to the correct place
                    visibleCells.add(new WorldCoordinate(location.wx + x, location.wy + y));
                }
            }
        }

        registerVisibleCells(location);

        CaveGame.game.world.getRegionManager().saveAllRegions();
    }

    /**
     * Register events for the visible cells
     *
     * @param location The location of this entity
     */
    private void registerVisibleCells(WorldCoordinate location) {
        logger.debug("Registering " + visibleCells.size() + " visible cells");

        for (WorldCoordinate visibleCell :
                visibleCells) {
            // Register each visible cell to set up the event
            CaveGame.game.registerEvent(location, visibleCell);
        }
    }

    /**
     * @return the entity's visible cells
     */
    public List<WorldCoordinate> getVisibleCells() {
        return visibleCells;
    }

    /**
     * @return the entity's health
     */
    public float getHealth() {
        return health;
    }

    /**
     * Get whether the health of the entity has enough health to be alive
     * Note that this should only be used within combat, as health is reset afterwards
     *
     * @return if the health of the entity is greater than zero
     */
    public boolean isAlive() {
        return health > 0;
    }

    /**
     * Do damage to the entity
     *
     * @param damageAmount The amount of damage to be done
     */
    public void doDamage(float damageAmount) {
        this.health = Math.max(this.health - damageAmount, 0);
    }

    /**
     * Remove armour from the entity
     *
     * @param lostArmour the amount of armour to remove
     */
    public void removeArmour(int lostArmour) {
        // Add this to the queue
        armourChange.add(lostArmour);
        int newArmour = armour - lostArmour;
        // The entity's armour can't be lower than 0
        armour = Math.max(newArmour, 0);
    }

    /**
     * Give the entity some armour back for this turn
     */
    public void giveBackArmour() {
        if (armourChange.peek() != null) {
            if (armourChange.peek() != 0) {
                // Alert the player if a nonzero amount of armour is restored
                System.out.println(armourChange.peek() + " Armour Restored");
            }
            // Get and remove the head of the queue
            armour += armourChange.poll();
        }
    }

    /**
     * @return the entity's current armour value
     */
    public int getArmour() {
        return armour;
    }

    /**
     * @return The string representation of the entity
     */
    public abstract String toString();

    // The following functions should be implemented on any child types to create the different types of entity
    // Note that these are functions because properties can't be overridden in Java

    /**
     * @return the starting health of the entity
     */
    protected abstract float getStartingHealth();

    /**
     * @return the attack damage of the entity
     */
    public abstract int getAttackDamage();

    /**
     * @return the starting armour of the entity
     */
    protected abstract int getStartingArmour();
}
