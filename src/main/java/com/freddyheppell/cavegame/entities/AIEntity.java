package com.freddyheppell.cavegame.entities;

public interface AIEntity {
    /**
     * Event method to be run when the player enters an entity's area of control
     *
     * @param player The player that has entered the player
     */
    void onAttackZoneEnter(Player player);
}
