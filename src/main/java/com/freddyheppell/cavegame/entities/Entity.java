package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.CaveGame;
import com.freddyheppell.cavegame.save.SaveManager;
import com.freddyheppell.cavegame.world.World;
import com.freddyheppell.cavegame.world.cells.Cell;
import com.freddyheppell.cavegame.world.coord.Transform;
import com.freddyheppell.cavegame.world.coord.WorldCoordinate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Entity {
    protected transient List<WorldCoordinate> visibleCells;
    private float health;
    public boolean alive = true;
    private Integer armour;
    private Queue<Integer> armourChange;
    private static final Logger logger = LogManager.getLogger();


    /**
     * The Entity class should not be directly instantiated
     */
    public Entity() {
        armourChange = new LinkedList<>();
    }

    public void resetStats() {
        health = getStartingHealth();
        armour = getStartingArmour();
        armourChange.clear();
        armourChange.add(0);
        armourChange.add(0);
    }

    /**
     * Get the radial view distance that the entity can see within
     *
     * @return The radial view distance
     */
    public abstract int getViewDistance();

    /**
     * Get the cells that are visible to the entity
     *
     * @param location The current location of the entity
     */
    public void calculateVisibleCells(WorldCoordinate location) {
        this.visibleCells = new ArrayList<>();
        int r = getViewDistance();
        int rSq = r*r;
        float correction = r * 0.8f;
        float limit = rSq + correction;


        for (int y = -r; y <= r; y++) {
            for (int x = -r; x <= r; x++) {
                if ((x*x) + (y*y) <= limit) {
                    // Add the origin coordinates
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

        for (WorldCoordinate visibleCell:
             visibleCells) {
            CaveGame.game.registerEvent(location, visibleCell);
        }


    }

    public List<WorldCoordinate> getVisibleCells() {
        return visibleCells;
    }

    /**
     * Event function to be executed after the entity moves
     *
     * @param newCoordinate The location of the player now that the move has been performed
     * @param world         The world instance the move took place in
     */
    public void afterMove(WorldCoordinate newCoordinate, World world) {

    }

    public float getHealth() {
        return health;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public void doDamage(float damageAmount) {
        this.health = Math.max(this.health - damageAmount, 0);
    }

    public void removeArmour(int lostArmour) {
        armourChange.add(lostArmour);
        int newArmour = armour - lostArmour;
        // The entity's armour can't be lower than 0
        armour = Math.max(newArmour, 0);
    }

    public void giveBackArmour() {
        if (armourChange.peek() != null) {
            System.out.println(armourChange.peek() + " Armour Restored");
            armour += armourChange.poll();
        }
    }

    public int getArmour() {
        return armour;
    }

    /**
     * @return The string representation of the entity
     */
    public abstract String toString();

    public abstract float getStartingHealth();

    public abstract int getAttackDamage();

    public abstract int getStartingArmour();
}
