package com.freddyheppell.cavegame.items;

public class ArmourItem extends Item {
    private String tier;
    private int shielding;

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
}
