package au.edu.unimelb.swen.oop.mokepon;

public final class Terrain {
  public static final Terrain BARRIER = new Terrain("BARRIER", 'B', false);
  public static final Terrain FOREST = new Terrain("FOREST", 'F', true);
  public static final Terrain GRASS = new Terrain("GRASS", 'G', true);
  public static final Terrain OCEAN = new Terrain("OCEAN", 'O', false);
  public static final Terrain PATH = new Terrain("PATH", 'P', true);
  public static final Terrain ROCKS = new Terrain("ROCKS", 'R', true);
  public static final Terrain WATER = new Terrain("WATER", 'W', true);

  private final String name;
  private final char symbol;
  private final boolean walkable;

  private Terrain(String name, char symbol, boolean walkable) {
    this.name = name;
    this.symbol = symbol;
    this.walkable = walkable;
  }

  public String getName() {
    return name;
  }

  public static Terrain[] getAll() {
    return new Terrain[] {BARRIER, FOREST, GRASS, OCEAN, PATH, ROCKS, WATER};
  }

  public boolean isWalkable() {
    return walkable;
  }

  public static Terrain fromSymbol(char symbol) {
    for (Terrain terrain : getAll()) {
      if (terrain.symbol == symbol) {
        return terrain;
      }
    }
    throw new IllegalArgumentException("Unknown terrain: " + symbol);
  }
}
