package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.config.Config;

import java.util.Random;

public class CombatManager {
    private Player player;
    private Entity enemy;

    private float overallMultiplier = Config.getFloat("fArmourProtection") * Config.getFloat("fDamageMultiplier");

    private enum Turn {PLAYER, ENEMY}

    private Random random = new Random();

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

        output.append(String.format("%s attacks %s for %f damage", attackerDesc, defenderDesc, damageDone));

        if (usedArmour > 0) {
            // If armour was used note this
            output.append(String.format(", using %d armour (%d remaining).", usedArmour, defender.getArmour()));
        }

        if (defender.getClass() == Player.class) {
            // If the player was attacked, show their new health
            output.append(" Health: ").append(defender.getHealth());
        }

        System.out.println(output);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void simulate() {
        Turn currentTurn;
        currentTurn = getFirstTurn();

        player.resetStats();
        enemy.resetStats();

        while (player.isAlive() && enemy.isAlive()) {
            if (currentTurn == Turn.PLAYER) {
                doTurn(player, enemy, "Player", "Enemy");
            } else {
                doTurn(enemy, player, "Enemy", "Player");
            }

            currentTurn = getOtherTurn(currentTurn);
            System.out.print("\n");
        }

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
}
