package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.config.Config;

import java.util.Random;

/**
 * Simulate combat between the player and an enemy entity
 */
public class CombatManager {
    /**
     * The instance of the player
     */
    private Player player;

    /**
     * The instance of the enemy
     */
    private Entity enemy;


    /**
     * The precomputed multiplier, this is "em" in the equation
     */
    private float overallMultiplier = Config.getFloat("fArmourProtection") * Config.getFloat("fDamageMultiplier");

    /**
     * An enum representing the current player
     */
    private enum Turn {
        PLAYER, ENEMY
    }

    /**
     * An instance of Random to use to calculate stuns
     */
    private Random random = new Random();


    /**
     * If the current turn is an extra turn, or the player's initial turn
     */
    private boolean extraTurn = false;

    public CombatManager(Player player, Entity enemy) {
        this.player = player;
        this.enemy = enemy;
    }

    private Turn getFirstTurn() {
        if (random.nextBoolean()) {
            System.out.println("Player goes first");
            return Turn.PLAYER;
        }
        System.out.println("Enemy goes first.");
        return Turn.ENEMY;
    }

    private Turn getOtherTurn(Turn currentTurn) {
        if (random.nextFloat() <= Config.getFloat("fCombatStunProbability")) {
            extraTurn = true;
            if (currentTurn == Turn.PLAYER) {
                System.out.println("You stunned the enemy and may attack again");
                return currentTurn;
            } else {
                System.out.println("You were stunned! The enemy may attack again");
                return currentTurn;
            }
        }

        if (currentTurn == Turn.ENEMY) {
            return Turn.PLAYER;
        }

        return Turn.ENEMY;
    }

    private int calculateUsedArmour(int attackDamage, int playerShielding) {
        return Math.min(attackDamage, playerShielding);
    }

    private float calculateDamage(int attackDamage, int usedArmour) {
        return attackDamage - (overallMultiplier * usedArmour);
    }

    private void doTurn(Entity attacker, Entity defender, String attackerDesc, String defenderDesc) {
        StringBuilder output = new StringBuilder();

        // Give the entity armour back for this turn
        if (!extraTurn) {
            // If this is an extra turn, the defender has been stunned so don't give them armour back
            defender.giveBackArmour();
        } else {
            extraTurn = false;
        }

        int attackDamage = attacker.getAttackDamage();
        int defenderShielding = defender.getArmour();

        int usedArmour = calculateUsedArmour(attackDamage, defenderShielding);
        float damageDone = calculateDamage(attackDamage, usedArmour);

        // Subtract health from the defending entity
        defender.doDamage(damageDone);
        // Subtract armour from the defending entity
        defender.removeArmour(usedArmour);

        output.append(String.format("%s attacks %s for %.2f damage", attackerDesc, defenderDesc, damageDone));

        if (usedArmour > 0) {
            // If armour was used note this
            output.append(String.format(", using %d armour (%d remaining).", usedArmour, defender.getArmour()));
        }

        if (defender.getClass() == Player.class) {
            // If the player was attacked, show their new health
            output.append(" Health: ").append(String.format("%.2f", defender.getHealth()));
        }

        System.out.println(output);

        try {
            Thread.sleep(Config.getLong("lTurnSleepTime"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Simulate comabt between two entities
     */
    public void simulate() {
        // Select which of the player or entity should go first
        Turn currentTurn = getFirstTurn();

        // Reset the health and armour of both to full
        player.resetStats();
        enemy.resetStats();

        // While both the player and the enemy is alive
        while (player.isAlive() && enemy.isAlive()) {
            if (currentTurn == Turn.PLAYER) {
                doTurn(player, enemy, "Player", "Enemy");
            } else {
                doTurn(enemy, player, "Enemy", "Player");
            }

            // Swap the turns
            currentTurn = getOtherTurn(currentTurn);
            System.out.print("\n");
        }

        // Display a message depending on which of the entity or player died
        if (player.isAlive()) {
            enemy.alive = false;
            System.out.println("You have killed the monster!");
        } else {
            player.alive = false;
            System.out.println("You have been killed!");
        }

        player.resetStats();
        enemy.resetStats();
    }

    /**
     * @return the modified player instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * @return the modified entity instance
     */
    public Entity getEnemy() {
        return enemy;
    }
}
