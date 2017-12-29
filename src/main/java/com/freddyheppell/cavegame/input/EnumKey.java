package com.freddyheppell.cavegame.input;

import java.util.HashMap;
import java.util.Map;

public enum EnumKey {
    DIR_NORTH ('n'),
    DIR_SOUTH ('s'),
    DIR_EAST ('e'),
    DIR_WEST ('w'),
    QUIT ('q');

    private final int charInt;

    private static Map<Integer, EnumKey> map = new HashMap<>();

    static {
        for (EnumKey legEnum : EnumKey.values()) {
            map.put(legEnum.charInt, legEnum);
        }
    }

    private EnumKey(final char key) { charInt = (int)key; }

    public static EnumKey valueOf(int key) {
        System.out.println("Requesting" + (char)key);
        return map.get(key);
    }
}
