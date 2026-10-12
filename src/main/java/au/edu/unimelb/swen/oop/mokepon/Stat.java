package au.edu.unimelb.swen.oop.mokepon;

public final class Stat {
  public static final Stat HP = new Stat("HP", 1000);
  public static final Stat ENERGY = new Stat("Energy", 100);
  public static final Stat STRENGTH = new Stat("strength", 100);
  public static final Stat ACCURACY = new Stat("accuracy", 100);
  public static final Stat DEFENCE = new Stat("defence", 100);
  public static final Stat DODGE = new Stat("dodge", 100);

  private final String label;
  private final int maximum;

  private Stat(String label, int maximum) {
    this.label = label;
    this.maximum = maximum;
  }

  public static Stat[] getAll() {
    return new Stat[] {HP, ENERGY, STRENGTH, ACCURACY, DEFENCE, DODGE};
  }

  public String label() {
    return label;
  }

  public int maximum() {
    return maximum;
  }

  public int baseValue(Mokepon mokepon) {
    if (this == HP) {
      return mokepon.getHp();
    } else if (this == ENERGY) {
      return mokepon.getEnergy();
    } else if (this == STRENGTH) {
      return mokepon.getStrength();
    } else if (this == ACCURACY) {
      return mokepon.getAccuracy();
    } else if (this == DEFENCE) {
      return mokepon.getDefence();
    } else {
      return mokepon.getDodge();
    }
  }
}
