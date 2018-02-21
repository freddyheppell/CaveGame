package com.freddyheppell.cavegame.items;

import com.rits.cloning.Cloner;

import java.util.ArrayList;

public class ItemRegistry {
    private static ItemRegistry itemRegistry = new ItemRegistry();
    private ArrayList<Item> items = new ArrayList<>();
    private double totalWeight = 0.0d;

    /**
     * Empty constructor for singleton pattern
     */
    private ItemRegistry(){}

    /**
     * Get the instance of the singleton class
     *
     * @return The instance of ItemRegistry
     */
    public static ItemRegistry getInstance() {
        return itemRegistry;
    }

    /**
     * Register one type of item
     *
     * @param item An instance of Item
     */
    public void register(Item item) {
        items.add(item);
    }

    /**
     * Register multiple types of items
     *
     * @param items An array of Items
     */
    public void register(Item[] items) {
        for (Item item:
             items) {
            register(item);
        }
    }

    /**
     * Generate the total weight of the items.
     * Must be run each time the registry is changed before items can be selected
     */
    public void generateWeighting() {
        for (Item item: items) {
            this.totalWeight += item.getDropWeight();
        }
    }

    /**
     * Select an item at random, weighted by each item's value
     *
     * @return The selected Item
     */
    public Item selectItem() {
        Cloner cloner = new Cloner();
        double randomVal = Math.random() * totalWeight;

        for (Item item:
             items) {

            randomVal -= item.getDropWeight();

            if (randomVal <= 0.0d) {
                return cloner.deepClone(item);
            }
        }

        throw new RuntimeException("Failed to pick item");
    }

    /**
     * Register the predefined items
     */
    public static void doRegister() {
        ItemRegistry.getInstance().register(new Item[] {
                // SWORD TIERS
                new SwordItem(100, "Wooden", 1),
                new SwordItem(80, "Iron", 5),
                new SwordItem(60, "Steel", 8),
                new SwordItem(10, "Legendary", 15),

                // ARMOUR TIERS
                new ArmourItem(100, "Leather", 5),
                new ArmourItem(80, "Chainmail", 8),
                new ArmourItem(60, "Iron", 10),
                new ArmourItem(10, "Legendary", 15),

        });

        // Then generate the total weighting
        ItemRegistry.getInstance().generateWeighting();
    }
}
