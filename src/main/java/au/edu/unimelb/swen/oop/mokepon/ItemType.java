package au.edu.unimelb.swen.oop.mokepon;

/** The three item definitions allowed by the specification. */
public final class ItemType {
  public static final ItemType HEALTH = new ItemType("HEALTH", "Health Tonic", "heal");
  public static final ItemType COFFEE = new ItemType("COFFEE", "Coffee", "coffee");
  public static final ItemType POISON = new ItemType("POISON", "Poison", "poison");

  private final String name;
  private final String displayName;
  private final String imageKey;

  private ItemType(String name, String displayName, String imageKey) {
    this.name = name;
    this.displayName = displayName;
    this.imageKey = imageKey;
  }

  public static ItemType[] getAll() {
    return new ItemType[] {HEALTH, COFFEE, POISON};
  }

  public String getName() {
    return name;
  }

  public static ItemType fromName(String name) {
    for (ItemType type : getAll()) {
      if (type.name.equals(name)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown item type: " + name);
  }

  public String displayName() {
    return displayName;
  }

  public String imageKey() {
    return "items." + imageKey;
  }
}
