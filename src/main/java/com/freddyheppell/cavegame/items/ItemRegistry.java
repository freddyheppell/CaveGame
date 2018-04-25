package com.freddyheppell.cavegame.items;

import com.freddyheppell.cavegame.config.Config;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import com.rits.cloning.Cloner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

/**
 * Stores the game's items
 */
public class ItemRegistry {
    private static final Logger logger = LogManager.getLogger();

    /**
     * Store the instance of this class for the singleton pattern
     */
    private static ItemRegistry itemRegistry = new ItemRegistry();

    /**
     * The sword the player starts with
     */
    public SwordItem starterSword = new SwordItem(100, "Wooden", 1);

    /**
     * The armour the player starts with
     */
    public ArmourItem starterArmour = new ArmourItem(100, "Leather", 5);

    /**
     * A list of all the registered items
     */
    private ArrayList<Item> items = new ArrayList<>();

    /**
     * The total weight of all items, used to randomly select according to weighting
     */
    private double totalWeight = 0.0d;

    /**
     * Empty constructor for singleton pattern
     */
    private ItemRegistry() {
    }

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
    private void register(Item item) {
        items.add(item);
    }

    /**
     * Register multiple types of items
     *
     * @param items An array of Items
     */
    private void register(Item[] items) {
        for (Item item :
                items) {
            register(item);
        }
    }

    /**
     * Generate the total weight of the items.
     * Must be run each time the registry is changed before items can be selected
     */
    private void generateWeighting() {
        totalWeight = 0.0d;
        for (Item item : items) {
            totalWeight += item.getDropWeight();
        }
        logger.info("Total item weight {}", totalWeight);
    }

    /**
     * Select an item at random, weighted by each item's value
     *
     * @return The selected Item
     */
    private Item selectItem() {
        Cloner cloner = new Cloner();

        // Weighted selection algorithm
        // Select an item at random in accordance with the specified weights
        double randomVal = Math.random() * totalWeight;

        for (Item item :
                items) {

            randomVal -= item.getDropWeight();

            if (randomVal <= 0.0d) {
                // Clone the item so it is a separate instance in memory
                return cloner.deepClone(item);
            }
        }

        throw new RuntimeException("Failed to pick item");
    }

    /**
     * Get an instance of the type adapter factory for item types
     *
     * @return The instance including known types
     */
    public RuntimeTypeAdapterFactory<Item> getItemAdapterFactory() {
        RuntimeTypeAdapterFactory<Item> adapter = RuntimeTypeAdapterFactory.of(Item.class, "t");

        // Register the item type classes
        adapter.registerSubtype(ArmourItem.class);
        adapter.registerSubtype(GoldItem.class);
        adapter.registerSubtype(SwordItem.class);

        return adapter;
    }

    /**
     * Register the predefined items
     */
    public void doRegister() {
        // Register known types of items
        register(new Item[]{
                // SWORD TIERS
                starterSword,
                new SwordItem(80, "Iron", 5),
                new SwordItem(60, "Steel", 8),
                new SwordItem(10, "Infused", 15),

                // ARMOUR TIERS
                starterArmour,
                new ArmourItem(80, "Chainmail", 8),
                new ArmourItem(60, "Iron", 10),
                new ArmourItem(10, "Infused", 15),

                // Misc
                new GoldItem(100)

        });

        // Then generate the total weighting
        generateWeighting();
    }

    /**
     * Generate a random reward for chests and entities
     *
     * @return the reward to be given
     */
    public ArrayList<Item> generateReward() {
        ArrayList<Item> reward = new ArrayList<>();
        Random random = new Random();

        // nextInt generates from 0 to n, we want 1 to n. By generating from 0 to (n-1) and adding 1, we get from 1 to n
        int itemCount = random.nextInt(Config.getInt("iChestMaxItems") - 1) + 1;

        for (int i = 0; i < itemCount; i++) {
            // Now pick itemCount number of items
            reward.add(ItemRegistry.getInstance().selectItem());
        }

        return reward;
    }
}
