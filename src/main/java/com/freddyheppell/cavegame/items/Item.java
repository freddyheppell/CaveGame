package com.freddyheppell.cavegame.items;

public class Item {

    private transient int dropWeight;


    /**
     * Item with an unspecified name, to be used with custom display name generators
     *
     * @param dropWeight Integer weight for random selection
     */
    public Item(int dropWeight) {
        this.dropWeight = dropWeight;
    }

    public int getDropWeight() {
        return dropWeight;
    }

    public String getDisplayName() {
        return "Generic Item";
    }

    public boolean isWeapon() {
        return false;
    }

    public boolean isEquippable() {
        return false;
    }

    /**
     * Get a representation of the item's name so that items of the same type can be stacked
     *
     * @return The class that the item is an instance of
     */
    public String getStackName() {
        return this.getClass().getCanonicalName();
    }

    /**
     * An event called when the item is selected to for a loot drop
     */
    public void onItemSelect() { }
}
