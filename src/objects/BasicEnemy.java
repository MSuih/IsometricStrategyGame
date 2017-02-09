package objects;

public class BasicEnemy extends Unit implements AIControllable {
    private double healthCurrent;
    private int healthMax;
    private String sprite;

    @Override
    public double getHealth() {
        return healthCurrent;
    }

    @Override
    public double getHealthPercent() {
        return healthCurrent / healthMax;
    }

    @Override
    public boolean isDead() {
        return healthCurrent > 0;
    }

    @Override
    public void damage(int value) {
        healthCurrent -= value;
    }

    @Override
    public void heal(int value) {
        healthCurrent = Math.min(healthMax, healthCurrent + value);
    }

    @Override
    public String getSprite() {
        return sprite;
    }

    @Override
    public boolean canAttack() {
        return true;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public AttackDesire getAttackDesire() {
        return AttackDesire.NORMAL;
    }

    @Override
    public boolean hasOffensiveAOE() {
        return false;
    }

    @Override
    public boolean hasHealingItem() {
        return false;
    }
}
