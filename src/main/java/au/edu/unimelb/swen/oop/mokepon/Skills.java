package au.edu.unimelb.swen.oop.mokepon;

/** The case-sensitive names in the data file select a reusable skill strategy. */
public final class Skills {
  private static final Skill[] SKILLS = {
    new DamageSkill("Bite", 1, 1),
    new DamageSkill("Tackle", 5, 2),
    new DamageSkill("Lance", 4, 0.5, true),
    new BoostSkill("Bunker", Stat.DEFENCE),
    new BoostSkill("Aim", Stat.ACCURACY),
    new BoostSkill("Swole", Stat.STRENGTH),
    new BoostSkill("Nimble", Stat.DODGE),
    new StunSkill(), new BlindSkill(), new WaitSkill()
  };

  private Skills() {}

  public static Skill get(String name) {
    for (Skill skill : SKILLS) {
      if (skill.getName().equals(name)) {
        return skill;
      }
    }
    throw new IllegalArgumentException("Unknown skill: " + name);
  }
}
