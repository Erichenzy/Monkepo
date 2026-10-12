package au.edu.unimelb.swen.oop.mokepon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

public class OverworldScreen extends GameScreen {
  private static final int TILE_SIZE = 32;
  private static final int MAP_X = 300;
  private static final int MAP_Y = 45;
  private static final int[][] DIRECTIONS = {{0, -1}, {0, 1}, {-1, 0}, {1, 0}};
  private static final String[] DIRECTION_NAMES = {"up", "down", "left", "right"};
  private final GameSession session;
  private final GameWindow window;
  private final Random random = new Random();
  private final Pane sprites = new Pane();
  private final Label status = new Label();
  private final Label wins = new Label();
  private final List<Label> inventoryValues = new ArrayList<>();
  private final Button[] movement = new Button[DIRECTIONS.length];

  public OverworldScreen(GameSession session, GameWindow window) {
    this.session = session;
    this.window = window;
    createMap();
    createStatistics();
    createMovement();
    status.setPrefSize(960, 28);
    status.setStyle("-fx-background-color:white;-fx-border-color:black;-fx-padding:4 10;");
    place(status, 160, 700);
    addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (event.isControlDown() || event.isAltDown() || event.isMetaDown()) {
        return;
      }
      int direction;
      switch (event.getCode()) {
        case UP:
          direction = 0;
          break;
        case DOWN:
          direction = 1;
          break;
        case LEFT:
          direction = 2;
          break;
        case RIGHT:
          direction = 3;
          break;
        default:
          return;
      }
      move(direction);
      event.consume();
    });
    refresh("");
  }

  private void createMap() {
    GameData data = session.data();
    for (int y = 0; y < GameData.MAP_SIZE; y++) {
      for (int x = 0; x < GameData.MAP_SIZE; x++) {
        Position position = new Position(x, y);
        ImageView tile = Ui.imageView(data.terrainImage(data.terrain(position)),
            TILE_SIZE, TILE_SIZE);
        place(tile, MAP_X + x * TILE_SIZE, MAP_Y + y * TILE_SIZE);
      }
    }
    sprites.setMouseTransparent(true);
    place(sprites, MAP_X, MAP_Y);
  }

  private void createStatistics() {
    place(Ui.imageView("assets/scroll.png", 280, 410), 950, 35);
    Label name = new Label(session.player().getName());
    name.setFont(Font.font("SansSerif", 20));
    name.setMaxWidth(215);
    place(name, 982, 75);
    int row = 0;
    for (Stat stat : Stat.getAll()) {
      addStatistic(stat.label(), Integer.toString(stat.baseValue(session.player())),
          100 + row++ * 24);
    }
    Label fights = new Label("Won fights");
    fights.setFont(Font.font("SansSerif", 14));
    place(fights, 985, 250);
    wins.setFont(Font.font("Monospaced", 15));
    place(wins, 1150, 250);
    Label inventory = new Label("Inventory");
    inventory.setFont(Font.font("SansSerif", 18));
    place(inventory, 985, 282);
    row = 0;
    for (ItemType type : ItemType.getAll()) {
      Label quantity = addStatistic(type.displayName(), "0", 320 + row++ * 25);
      inventoryValues.add(quantity);
    }
  }

  private Label addStatistic(String name, String value, int y) {
    Label label = new Label(name);
    label.setFont(Font.font("SansSerif", 14));
    place(label, 985, y);
    Label quantity = new Label(value);
    quantity.setFont(Font.font("Monospaced", 15));
    place(quantity, 1150, y);
    return quantity;
  }

  private void createMovement() {
    int[][] locations = {{1055, 523}, {1055, 623}, {975, 573}, {1135, 573}};
    for (int i = 0; i < DIRECTIONS.length; i++) {
      final int direction = i;
      Button button = new Button();
      button.setGraphic(Ui.imageView(session.data().value("map.buttons."
          + DIRECTION_NAMES[i]), 38, 38));
      button.setAccessibleText("Move " + DIRECTION_NAMES[i]);
      button.setPrefSize(70, 58);
      Ui.styleButton(button, 10);
      button.setOnAction(event -> move(direction));
      movement[i] = button;
      place(button, locations[i][0], locations[i][1]);
    }
  }

  private void move(int direction) {
    if (window.isModalOpen() || !session.move(DIRECTIONS[direction][0],
        DIRECTIONS[direction][1])) {
      return;
    }
    ItemType item = session.collectBox();
    refresh(item == null ? "" : "You picked up a " + item.displayName() + "!");
    if (session.markers().containsKey(session.position())) {
      showEncounter();
    } else {
      checkExit();
    }
  }

  private void showEncounter() {
    Mokepon enemy = session.encounter(random);
    Terrain terrain = session.data().terrain(session.position());
    String message = advantageMessage("Your mokepon", session.player(), terrain)
        + advantageMessage("Your enemy", enemy, terrain);
    window.showModal("Encountered a " + enemy.getName(), message,
        new String[] {"Fight!", "Run Away!"}, Arrays.asList(
            event -> window.startBattle(enemy, terrain),
            event -> {
              session.runAway();
              refresh("");
            }));
  }

  private String advantageMessage(String owner, Mokepon mokepon, Terrain terrain) {
    int advantage = mokepon.getType().advantage(terrain);
    if (advantage == 0) {
      return "";
    }
    return owner + " will be at " + (advantage > 0 ? "an advantage" : "a disadvantage")
        + " here!\n";
  }

  public void checkExit() {
    if (session.position().equals(session.data().exit())) {
      window.showModal("You finished the game! You won " + session.wins() + " fights!", "",
          new String[] {"Return to Main Menu", "Quit"},
          Arrays.asList(event -> window.showMainMenu(), event -> window.close()));
    }
  }

  public void refresh(String message) {
    sprites.getChildren().clear();
    GameData data = session.data();
    for (Map.Entry<Position, MarkerKind> marker : session.markers().entrySet()) {
      String key = marker.getValue().key();
      addSprite(Ui.imageView(data.value(key + ".image"), TILE_SIZE, TILE_SIZE,
          data.value(key + ".colour")), marker.getKey());
    }
    for (Position position : session.boxes().keySet()) {
      addSprite(Ui.imageView(data.value("map.items.image"), TILE_SIZE, TILE_SIZE), position);
    }
    addSprite(Ui.imageView(data.value("map.exit.image"), TILE_SIZE, TILE_SIZE,
        data.value("map.exit.colour")), data.exit());
    addSprite(Ui.imageView(data.value("map.player.image"), TILE_SIZE, TILE_SIZE),
        session.position());
    for (int i = 0; i < movement.length; i++) {
      movement[i].setDisable(!session.canMove(DIRECTIONS[i][0], DIRECTIONS[i][1]));
    }
    int index = 0;
    for (ItemType type : ItemType.getAll()) {
      inventoryValues.get(index++).setText(Integer.toString(session.quantity(type)));
    }
    wins.setText(Integer.toString(session.wins()));
    status.setText(message);
  }

  private void addSprite(ImageView image, Position position) {
    image.setLayoutX(position.x() * TILE_SIZE);
    image.setLayoutY(position.y() * TILE_SIZE);
    sprites.getChildren().add(image);
  }
}
