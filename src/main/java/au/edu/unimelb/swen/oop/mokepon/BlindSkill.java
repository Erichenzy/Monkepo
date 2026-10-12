package au.edu.unimelb.swen.oop.mokepon;

import java.util.Random;

public class BlindSkill extends BaseSkill {
  public BlindSkill() {
    super("Pocket Sand", 4, true);
  }

  @Override
  protected String apply(Fighter user, Fighter target, Random random) {
    target.blind();
    return "Pocket sand blinded " + target.getMokepon().getName()
        + "!\nTheir targeted skills may miss for 3 turns.";
  }
}
