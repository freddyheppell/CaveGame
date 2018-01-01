package com.freddyheppell.cavegame.input;

import java.util.HashMap;
import java.util.Map;

public enum EnumKey {
    // MOVEMENT
    DIR_NORTH('w'),
    DIR_SOUTH('s'),
    DIR_EAST('d'),
    DIR_WEST('a'),

    // MISC
    QUIT('q');

    /**
     * The integer representation of the character
     */
    private final int charInt;

    /**
     * A HashMap to allow lookup by character
     */
    private static Map<Integer, EnumKey> map = new HashMap<>();

    static {
        for (EnumKey legEnum : EnumKey.values()) {
            map.put(legEnum.charInt, legEnum);
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

        throw new RuntimeException("Key not found!");
    }
}
