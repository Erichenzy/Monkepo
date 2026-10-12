package au.edu.unimelb.swen.oop.mokepon;

/** Immutable grid coordinate; x is the column and y is the row. */
public final class Position {
  private final int x;
  private final int y;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public static Position parse(String value) {
    String[] coordinates = value.split(",");
    return new Position(Integer.parseInt(coordinates[0].trim()),
        Integer.parseInt(coordinates[1].trim()));
  }

  public int x() {
    return x;
  }

  public int y() {
    return y;
  }

  public Position offset(int dx, int dy) {
    return new Position(x + dx, y + dy);
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof Position)) {
      return false;
    }
    Position position = (Position) other;
    return x == position.x && y == position.y;
  }

  @Override
  public int hashCode() {
    return 31 * x + y;
  }
}
