package au.edu.unimelb.swen.oop.mokepon;

import java.util.Random;

public class DamageSkill extends BaseSkill {
  private final double multiplier;
  private final boolean causesBleeding;

  public DamageSkill(String name, int cost, double multiplier) {
    this(name, cost, multiplier, false);
  }

  public DamageSkill(String name, int cost, double multiplier, boolean causesBleeding) {
    super(name, cost, true);
    this.multiplier = multiplier;
    this.causesBleeding = causesBleeding;
  }

  @Override
  protected String apply(Fighter user, Fighter target, Random random) {
    int damage = user.attackDamage(target, multiplier);
    target.damage(damage);
    String message = "Hit " + target.getMokepon().getName() + " for " + damage + " damage!";
    if (causesBleeding) {
      target.startBleeding(user);
      message += " They started bleeding!";
    }
    return message;
  }
}
