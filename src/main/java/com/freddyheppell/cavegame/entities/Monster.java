package com.freddyheppell.cavegame.entities;

/**
 * A basic monster with small view distance and low attack damage
 * Overridden functions are documented in the parent class
 */
public class Monster extends Entity {

    public Monster() {
        super();
        resetStats();
    }

    @Override
    public int getViewDistance() {
        return 3;
    }

    @Override
    public String toString() {
        return "M";
    }

    @Override
    public float getStartingHealth() {
        return 25f;
    }

    @Override
    public int getAttackDamage() {
        return 3;
    }

    @Override
    public int getStartingArmour() {
        return 0;
    }
}
