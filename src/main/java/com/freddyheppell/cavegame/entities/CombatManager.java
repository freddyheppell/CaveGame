package com.freddyheppell.cavegame.entities;

import com.freddyheppell.cavegame.config.Config;

import java.util.Random;

public class CombatManager {
    private Player player;
    private Entity enemy;

    private float overallMultiplier = Config.getFloat("fArmourProtection") * Config.getFloat("fDamageMultiplier");

    private enum Turn {PLAYER, ENEMY}

    private Random random = new Random();

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
        defender.giveBackArmour();
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
        }
    }
}
