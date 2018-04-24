package com.freddyheppell.cavegame.items;

import java.util.Random;

// Overridden methods are documented in the parent class
public class GoldItem extends Item {

    /**
     * Item with an unspecified name, to be used with custom display name generators
     *
     * @param dropWeight Integer weight for random selection
     */
    public GoldItem(int dropWeight) {
        super(dropWeight);
    }

    @Override
    public String getDisplayName() {
        return String.format("Gold (%d G)", quantity);
    }

    /**
     * When the gold is selected as a reward, its value must be determined
     * TODO implement this
     */
    @Override
    public void onItemSelect() {
        this.quantity = new Random().nextInt(10);
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}
