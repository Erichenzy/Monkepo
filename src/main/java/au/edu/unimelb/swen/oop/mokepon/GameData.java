package au.edu.unimelb.swen.oop.mokepon;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/** Immutable definitions loaded from the selected UTF-8 properties file. */
public final class GameData {
  public static final int MAP_SIZE = 20;

  private final Properties properties;
  private final List<Mokepon> mokepons;
  private final Terrain[][] tiles = new Terrain[MAP_SIZE][MAP_SIZE];
  private final Map<Position, MarkerKind> markers = new HashMap<>();
  private final Map<Position, ItemType> boxes = new HashMap<>();

  public static GameData load(Path path) throws IOException {
    Properties properties = new Properties();
    try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
      properties.load(reader);
    }
    return new GameData(properties);
  }

  public GameData(Properties properties) {
    this.properties = new Properties();
    this.properties.putAll(properties);
    List<Mokepon> loaded = new ArrayList<>();
    for (int i = 0; properties.containsKey("mokepon." + i + ".name"); i++) {
      String prefix = "mokepon." + i + ".";
      loaded.add(new Mokepon(value(prefix + "name"), value(prefix + "image"),
          number(prefix + "hp"), number(prefix + "energy"),
          number(prefix + "strength"), number(prefix + "accuracy"),
          number(prefix + "defence"), number(prefix + "dodge"),
          Arrays.asList(value(prefix + "skills").split("\\|")),
          MokeponType.fromName(value(prefix + "type"))));
    }
    mokepons = loaded;
    for (int y = 0; y < MAP_SIZE; y++) {
      String row = value("map.tiles." + y);
      for (int x = 0; x < MAP_SIZE; x++) {
        tiles[y][x] = Terrain.fromSymbol(row.charAt(x));
      }
    }
    for (MarkerKind kind : MarkerKind.getAll()) {
      for (int i = 0; properties.containsKey(kind.key() + "." + i); i++) {
        markers.put(Position.parse(value(kind.key() + "." + i)), kind);
      }
    }
    for (int i = 0; properties.containsKey("map.items." + i + ".type"); i++) {
      String prefix = "map.items." + i + ".";
      boxes.put(Position.parse(value(prefix + "location")),
          ItemType.fromName(value(prefix + "type")));
    }
  }

  public String value(String key) {
    return properties.getProperty(key).trim();
  }

  private int number(String key) {
    return Integer.parseInt(value(key));
  }

  public List<Mokepon> mokepons() {
    return new ArrayList<>(mokepons);
  }

  public Terrain terrain(Position position) {
    return tiles[position.y()][position.x()];
  }

  public boolean isWalkable(Position position) {
    return position.x() >= 0 && position.x() < MAP_SIZE
        && position.y() >= 0 && position.y() < MAP_SIZE
        && terrain(position).isWalkable();
  }

  public Position start() {
    return Position.parse(value("map.player.startingPos"));
  }

  public Position exit() {
    return Position.parse(value("map.exit.location"));
  }

  public Map<Position, MarkerKind> markers() {
    return new HashMap<>(markers);
  }

  public Map<Position, ItemType> boxes() {
    return new HashMap<>(boxes);
  }

  public String terrainImage(Terrain terrain) {
    return value("map.tiles.images." + terrain.getName().toLowerCase(Locale.ROOT));
  }

  public String typeImage(MokeponType type) {
    return value("mokepon.type." + type.getName().toLowerCase(Locale.ROOT));
  }
}
