package au.edu.unimelb.swen.oop.mokepon;

import java.util.Random;

public abstract class BaseSkill implements Skill {
  private final String name;
  private final int cost;
  private final boolean targeted;

  protected BaseSkill(String name, int cost, boolean targeted) {
    this.name = name;
    this.cost = cost;
    this.targeted = targeted;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public int getCost() {
    return cost;
  }

  @Override
  public final String use(Fighter user, Fighter target, Random random) {
    if (user.getEnergy() < cost) {
      throw new IllegalStateException("Insufficient energy for " + name);
    }
    user.spendEnergy(cost);
    if (targeted) {
      double accuracy = user.getStat(Stat.ACCURACY);
      double dodge = target.getStat(Stat.DODGE);
      double chance = accuracy * accuracy / (accuracy * accuracy + dodge * dodge);
      if (random.nextDouble() >= chance
          || (user.getBlindTurns() > 0 && random.nextDouble() < 0.5)) {
        return "But they missed.";
      }
    }
    return apply(user, target, random);
  }

  protected abstract String apply(Fighter user, Fighter target, Random random);
}
