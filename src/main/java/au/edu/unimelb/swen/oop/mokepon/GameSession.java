package au.edu.unimelb.swen.oop.mokepon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** Mutable adventure state; a new selection always starts a fresh adventure. */
public final class GameSession {
  private final GameData data;
  private final Mokepon player;
  private final Map<Position, MarkerKind> markers;
  private final Map<Position, ItemType> boxes;
  private final Map<ItemType, Integer> inventory = new HashMap<>();
  private Position position;
  private Position previousPosition;
  private int wins;

  public GameSession(GameData data, Mokepon player) {
    this.data = data;
    this.player = player;
    markers = new HashMap<>(data.markers());
    boxes = new HashMap<>(data.boxes());
    position = data.start();
    previousPosition = position;
    for (ItemType type : ItemType.getAll()) {
      inventory.put(type, 0);
    }
  }

  public boolean canMove(int dx, int dy) {
    return Math.abs(dx) + Math.abs(dy) == 1 && data.isWalkable(position.offset(dx, dy));
  }

  public boolean move(int dx, int dy) {
    if (!canMove(dx, dy)) {
      return false;
    }
    previousPosition = position;
    position = position.offset(dx, dy);
    return true;
  }

  public void runAway() {
    position = previousPosition;
  }

  public Mokepon encounter(Random random) {
    MarkerKind kind = markers.get(position);
    List<Mokepon> candidates = new ArrayList<>();
    for (Mokepon mokepon : data.mokepons()) {
      if (kind.accepts(mokepon, data.terrain(position))) {
        candidates.add(mokepon);
      }
    }
    if (candidates.isEmpty()) {
      throw new IllegalStateException("No candidate for " + kind.key()
          + " on " + data.terrain(position).getName());
    }
    return candidates.get(random.nextInt(candidates.size()));
  }

  public ItemType collectBox() {
    ItemType type = boxes.remove(position);
    if (type != null) {
      inventory.put(type, quantity(type) + 1);
    }
    return type;
  }

  public void winBattle() {
    markers.remove(position);
    wins++;
  }

  public int quantity(ItemType type) {
    return inventory.get(type);
  }

  public boolean consume(ItemType type) {
    if (quantity(type) == 0) {
      return false;
    }
    inventory.put(type, quantity(type) - 1);
    return true;
  }

  public GameData data() {
    return data;
  }

  public Mokepon player() {
    return player;
  }

  public Position position() {
    return position;
  }

  public Map<Position, MarkerKind> markers() {
    return new HashMap<>(markers);
  }

  public Map<Position, ItemType> boxes() {
    return new HashMap<>(boxes);
  }

  public int wins() {
    return wins;
  }
}
