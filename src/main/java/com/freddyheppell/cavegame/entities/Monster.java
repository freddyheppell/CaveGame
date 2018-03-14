package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.world.coord.WorldCoordinate;

public class Monster extends Entity implements AIEntity {

    /**
     * The Entity class should not be directly instantiated
     *
     * @param worldCoordinate The location of the entity
     */
    public Monster(WorldCoordinate worldCoordinate) {
        super(worldCoordinate);
        calculateVisibleCells();
    }

    @Override
    public void onAttackZoneEnter(Player player) {

    }
}
