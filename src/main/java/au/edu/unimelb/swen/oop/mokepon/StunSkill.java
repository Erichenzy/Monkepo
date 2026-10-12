package au.edu.unimelb.swen.oop.mokepon;

import java.util.Random;

public class StunSkill extends BaseSkill {
  public StunSkill() {
    super("Stun", 4, true);
  }

  @Override
  protected String apply(Fighter user, Fighter target, Random random) {
    target.stun();
    return target.getMokepon().getName()
        + " was stunned! Their damage is halved for 3 turns.";
  }
}
