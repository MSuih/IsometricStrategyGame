package objects;

public interface AIControllable {
    public boolean canAttack();
    public boolean canMove();

    public static enum AttackDesire {
        PASSIVE, DEFENSIVE, NORMAL, AGRESSIVE, RECKLESS;
    }
    public AttackDesire getAttackDesire();
    public boolean hasOffensiveAOE();
    public boolean hasHealingItem();
}
