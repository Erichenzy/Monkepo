package au.edu.unimelb.swen.oop.mokepon;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/** Owns screen transitions, adventure lifetime and modal interaction. */
public class GameWindow extends Stage {
  private static final int SELECT = 0;
  private static final int OVERWORLD = 1;
  private static final int BATTLE = 2;

  private final GameData data;
  private final Random random;
  private final VBox content = new VBox();
  private final StackPane root = new StackPane(content);
  private final MenuItem runAway = new MenuItem("Run away");
  private final MenuItem mainMenu = new MenuItem("Main menu");
  private GameSession session;
  private OverworldScreen overworld;
  private int state;
  private StackPane modal;

  public GameWindow(GameData data) {
    this(data, new Random());
  }

  public GameWindow(GameData data, Random random) {
    this.data = data;
    this.random = random;
    setTitle("Mokepon");
    setResizable(false);
    content.addEventFilter(InputEvent.ANY, event -> {
      if (isModalOpen()) {
        event.consume();
      }
    });
    content.getChildren().add(createMenuBar());
    setScene(new Scene(root));
    showMainMenu();
    sizeToScene();
  }

  private MenuBar createMenuBar() {
    runAway.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
    runAway.setOnAction(event -> {
      if (state == BATTLE && !isModalOpen()) {
        session.runAway();
        showOverworld("");
      }
    });
    mainMenu.setAccelerator(new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN));
    mainMenu.setOnAction(event -> {
      if (!isModalOpen()) {
        showMainMenu();
      }
    });
    MenuItem exit = new MenuItem("Exit");
    exit.setAccelerator(new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN));
    exit.setOnAction(event -> close());
    Menu menu = new Menu("Game");
    menu.getItems().addAll(runAway, mainMenu, exit);
    return new MenuBar(menu);
  }

  public void showMainMenu() {
    session = null;
    overworld = null;
    setScreen(new SelectScreen(data, this), SELECT);
  }

  public void startAdventure(Mokepon player) {
    session = new GameSession(data, player);
    overworld = new OverworldScreen(session, this);
    showOverworld("");
  }

  private void setScreen(GameScreen screen, int state) {
    this.state = state;
    if (content.getChildren().size() > 1) {
      content.getChildren().remove(1);
    }
    content.getChildren().add(screen);
    runAway.setDisable(state != BATTLE);
    mainMenu.setDisable(state == SELECT);
    screen.setFocusTraversable(true);
    screen.requestFocus();
  }

  private void showOverworld(String message) {
    overworld.refresh(message);
    setScreen(overworld, OVERWORLD);
  }

  public void startBattle(Mokepon enemy, Terrain terrain) {
    setScreen(new BattleScreen(session, enemy, terrain, this, random), BATTLE);
  }

  public void battleFinished(boolean playerWon, String enemyName) {
    if (playerWon) {
      showModal("You Win", "", new String[] {"Back to it!"}, Arrays.asList(event -> {
        session.winBattle();
        showOverworld("You beat the enemy " + enemyName + "!");
        overworld.checkExit();
      }));
    } else {
      showModal("Game Over", "", new String[] {"Return to Main Menu", "Quit"},
          Arrays.asList(event -> showMainMenu(), event -> close()));
    }
  }

  public boolean isModalOpen() {
    return modal != null;
  }

  public void showModal(String title, String message, String[] buttons,
      List<EventHandler<ActionEvent>> actions) {
    modal = new StackPane();
    modal.setStyle("-fx-background-color:rgba(0,0,0,0.45);");
    Label heading = new Label(title);
    heading.setFont(Ui.headingFont(32));
    heading.setWrapText(true);
    heading.setStyle("-fx-text-fill:black;");
    heading.setAlignment(Pos.CENTER);
    heading.setMaxWidth(820);
    Label body = new Label(message.trim());
    body.setWrapText(true);
    body.setMaxWidth(820);
    body.setStyle("-fx-font-size:18;-fx-text-fill:black;");
    HBox choices = new HBox(20);
    choices.setAlignment(Pos.CENTER);
    for (int i = 0; i < buttons.length; i++) {
      EventHandler<ActionEvent> action = actions.get(i);
      Button button = new Button(buttons[i]);
      button.setFont(Ui.headingFont(24));
      button.setOnAction(event -> {
        root.getChildren().remove(modal);
        modal = null;
        action.handle(event);
      });
      choices.getChildren().add(button);
    }
    VBox dialog = new VBox(24);
    dialog.setAlignment(Pos.CENTER);
    dialog.setPadding(new Insets(32));
    dialog.setMinWidth(300);
    dialog.setMaxSize(VBox.USE_PREF_SIZE, VBox.USE_PREF_SIZE);
    dialog.setStyle("-fx-background-color:white;-fx-border-color:black;-fx-border-width:2;");
    dialog.getChildren().add(heading);
    if (!message.trim().isEmpty()) {
      dialog.getChildren().add(body);
    }
    dialog.getChildren().add(choices);
    modal.getChildren().add(dialog);
    root.getChildren().add(modal);
    choices.getChildren().get(0).requestFocus();
  }
}
