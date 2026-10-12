package au.edu.unimelb.swen.oop.mokepon;

public final class MarkerKind {
  public static final MarkerKind RANDOM = new MarkerKind("map.mokeponRandom", 0);
  public static final MarkerKind STRONG = new MarkerKind("map.mokeponStrong", 1);
  public static final MarkerKind WEAK = new MarkerKind("map.mokeponWeak", -1);

  private final String key;
  private final int advantage;

  private MarkerKind(String key, int advantage) {
    this.key = key;
    this.advantage = advantage;
  }

  public static MarkerKind[] getAll() {
    return new MarkerKind[] {RANDOM, STRONG, WEAK};
  }

  public String key() {
    return key;
  }

  public boolean accepts(Mokepon mokepon, Terrain terrain) {
    return this == RANDOM || mokepon.getType().advantage(terrain) == advantage;
  }
}
