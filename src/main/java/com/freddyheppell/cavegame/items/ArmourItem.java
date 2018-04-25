package com.freddyheppell.cavegame.items;

/**
 * Represents an item that gives the player armour
 * Overridden methods are documented in the parent class
 */
public class ArmourItem extends Item {
    /**
     * The tier of the item to be displayed to the player
     */
    private String tier;

    /**
     * The amount of shielding that the user provides
     */
    private int shielding = 0;

    /**
     * @param dropWeight The integer value by which selection is biased
     * @param tier       The tier to be displayed to the user
     * @param shielding  The shielding the armour provides
     */
    public ArmourItem(int dropWeight, String tier, int shielding) {
        super(dropWeight);
        this.tier = tier;
        this.shielding = shielding;
    }

    @Override
    public String getDisplayName() {
        return String.format("%s Armour (%d Prot)", tier, shielding);
    }

    @Override
    public boolean isEquippable() {
        return true;
    }

    /**
     * @return the amount of shielding
     */
    public int getShielding() {
        return shielding;
    }
}
