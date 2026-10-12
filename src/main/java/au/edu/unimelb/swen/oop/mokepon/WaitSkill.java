package au.edu.unimelb.swen.oop.mokepon;

import java.util.Random;

public class WaitSkill extends BaseSkill {
  private static final String[] MESSAGES = {
    "They get a little more bored.",
    "They wonder if this is a good strategy.",
    "They think about what they will have for dinner."
  };

  public WaitSkill() {
    super("Wait", -2, false);
  }

  @Override
  protected String apply(Fighter user, Fighter target, Random random) {
    return MESSAGES[random.nextInt(MESSAGES.length)];
  }
}
