package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

public class Monster extends Entity implements AIEntity {

    /**
     * The Entity class should not be directly instantiated
     */
    public Monster() {
        super();
        resetStats();
    }

    @Override
    public void onAttackZoneEnter(Player player) {

    }

    @Override
    public int getViewDistance() {
        return 3;
    }

    @Override
    public String toString() {
        return "M";
    }

    @Override
    public float getStartingHealth() {
        return 25f;
    }

    @Override
    public int getAttackDamage() {
        return 100;
    }

    @Override
    public int getStartingArmour() {
        return 0;
    }
}
