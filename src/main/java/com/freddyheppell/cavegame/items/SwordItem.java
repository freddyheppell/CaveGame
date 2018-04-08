package com.freddyheppell.cavegame.items;

// Overridden methods are documented in the parent class
public class SwordItem extends Item {
    private String tier;
    private int damage;

    /**
     * @param dropWeight The integer value by which selection is biased
     * @param tier       The tier of the item to be displayed to the user
     * @param damage     The amount of damage the sword does to enemies
     */
    public SwordItem(int dropWeight, String tier, int damage) {
        super(dropWeight);
        this.tier = tier;
        this.damage = damage;
    }

    @Override
    public String getDisplayName() {
        return String.format("%s Sword (%d Dmg)", tier, damage);
    }

    @Override
    public boolean isWeapon() {
        return true;
    }
}
