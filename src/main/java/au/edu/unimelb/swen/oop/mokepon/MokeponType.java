package au.edu.unimelb.swen.oop.mokepon;

/** Elemental types and their battle-only terrain relationships. */
public final class MokeponType {
  public static final MokeponType NORMAL = new MokeponType("NORMAL", null, null);
  public static final MokeponType FIRE =
      new MokeponType("FIRE", Terrain.FOREST, Terrain.WATER);
  public static final MokeponType ICE =
      new MokeponType("ICE", Terrain.ROCKS, Terrain.GRASS);
  public static final MokeponType WATER =
      new MokeponType("WATER", Terrain.WATER, Terrain.ROCKS);
  public static final MokeponType LIGHTNING =
      new MokeponType("LIGHTNING", Terrain.GRASS, Terrain.FOREST);

  private final String name;
  private final Terrain strongTerrain;
  private final Terrain weakTerrain;

  private MokeponType(String name, Terrain strongTerrain, Terrain weakTerrain) {
    this.name = name;
    this.strongTerrain = strongTerrain;
    this.weakTerrain = weakTerrain;
  }

  public String getName() {
    return name;
  }

  public static MokeponType[] getAll() {
    return new MokeponType[] {NORMAL, FIRE, ICE, WATER, LIGHTNING};
  }

  public static MokeponType fromName(String name) {
    for (MokeponType type : getAll()) {
      if (type.name.equals(name)) {
        return type;
      }
    }
    throw new IllegalArgumentException("Unknown Mokepon type: " + name);
  }

  public int advantage(Terrain terrain) {
    if (terrain == strongTerrain) {
      return 1;
    }
    if (terrain == weakTerrain) {
      return -1;
    }
    return 0;
  }
}
