package au.edu.unimelb.swen.oop.mokepon;

import java.util.Random;

public class BoostSkill extends BaseSkill {
  private static final int ENERGY_COST = 3;
  private static final int MAX_INCREASE = 10;
  private final Stat stat;

  public BoostSkill(String name, Stat stat) {
    super(name, ENERGY_COST, false);
    this.stat = stat;
  }

  @Override
  protected String apply(Fighter user, Fighter target, Random random) {
    int increase = user.raise(stat, random.nextInt(MAX_INCREASE) + 1);
    return "They raised their " + stat.label() + " by " + increase + " points!";
  }
}
