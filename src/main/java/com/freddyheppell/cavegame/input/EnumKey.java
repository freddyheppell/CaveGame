package com.freddyheppell.cavegame.input;

import java.util.HashMap;
import java.util.Map;

public enum EnumKey {
    // MOVEMENT
    DIR_NORTH('w'),
    DIR_SOUTH('s'),
    DIR_EAST('d'),
    DIR_WEST('a'),

    // INVENTORY
    OPEN_INV('i'),

    // MISC
    QUIT('x'),

    // UNKNOWN
    // There's no character that is actually of this value
    UNKNOWN(Character.MIN_VALUE);

    /**
     * The integer representation of the character
     */
    private final int charInt;

    /**
     * A HashMap to allow lookup by character
     */
    private static Map<Integer, EnumKey> map = new HashMap<>();

    static {
        for (EnumKey entry : EnumKey.values()) {
            map.put(entry.charInt, entry);
        }
    }

    /**
     * @param key The key associated with the command
     */
    EnumKey(final char key) {
        this.charInt = (int) key;
    }

    /**
     * Get the command associated with the key
     *
     * @param key The integer value of the key to lookup
     * @return The corresponding command
     */
    public static EnumKey valueOf(int key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }

        return UNKNOWN;
    }
}
