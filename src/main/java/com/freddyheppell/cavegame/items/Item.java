package com.freddyheppell.cavegame.items;

public class Item {
    /**
     * The relative frequency at which this item should be dropped.
     * This does not need to be serialised so set to transient
     */
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

    /**
     * Get the human readable name of this item
     *
     * @return String name
     */
    public String getDisplayName() {
        return "Generic Item";
    }

    /**
     * Can this item be equipped as a weapon?
     *
     * @return Boolean if it can be equipped
     */
    public boolean isWeapon() {
        return false;
    }

    /**
     * Can this item be equipped as armour?
     *
     * @return Boolean if it can be equipped
     */
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
    public void onItemSelect() {
    }
}
