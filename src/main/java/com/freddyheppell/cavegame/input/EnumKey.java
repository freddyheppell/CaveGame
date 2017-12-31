package com.freddyheppell.cavegame.input;

import java.util.HashMap;
import java.util.Map;

public enum EnumKey {
    DIR_NORTH ('w'),
    DIR_SOUTH ('s'),
    DIR_EAST ('d'),
    DIR_WEST ('a'),
    QUIT ('q');

    private final int charInt;

    private static Map<Integer, EnumKey> map = new HashMap<>();

    static {
        for (EnumKey legEnum : EnumKey.values()) {
            map.put(legEnum.charInt, legEnum);
        }
    }

    EnumKey(final char key) { charInt = (int)key; }

    public static EnumKey valueOf(int key) {
        return map.get(key);
    }
}
