package com.freddyheppell.cavegame.items;

import java.util.Random;

/**
 * Represents an amount of gold to be added to the player's current value
 */
public class GoldItem extends Item {

    /**
     * The gold value of this item
     */
    public int value;

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
        return String.format("Gold (%d)", value);
    }

    /**
     * When the gold is selected as a reward, its value must be determined
     */
    @Override
    public void onItemSelect() {
        this.value = new Random().nextInt(10);
    }
}
