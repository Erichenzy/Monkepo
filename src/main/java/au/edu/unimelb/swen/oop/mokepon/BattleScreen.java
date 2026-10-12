package au.edu.unimelb.swen.oop.mokepon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/** Project 1's battle layout; BattleEngine now owns the rules and mutable statistics. */
public class BattleScreen extends GameScreen {
  private static final int BAR_WIDTH = 256;
  private static final int PLAYER_BAR_HEIGHT = 30;
  private final GameSession session;
  private final GameWindow window;
  private final BattleEngine battle;
  private final List<Button> actionButtons = new ArrayList<>();
  private final Map<ItemType, Button> itemButtons = new HashMap<>();
  private final Label playerHpText = new Label();
  private final Label playerEnergyText = new Label();
  private final TextArea battleLog = new TextArea();
  private ProgressBar playerHpBar;
  private ProgressBar playerEnergyBar;
  private ProgressBar enemyHpBar;
  private ProgressBar enemyEnergyBar;
  private ImageView playerScroll;
  private ImageView enemyScroll;
  private int displayedLogLength;
  private boolean outcomeShown;

  public BattleScreen(GameSession session, Mokepon enemy, Terrain terrain, GameWindow window,
      Random random) {
    this.session = session;
    this.window = window;
    battle = new BattleEngine(session, enemy, terrain, random);
    createPlayerPanel();
    createEnemyPanel();
    battleLog.setEditable(false);
    battleLog.setWrapText(true);
    battleLog.setPrefSize(560, 220);
    place(battleLog, 660, 500);
    updateDisplay();
    addEventFilter(KeyEvent.KEY_PRESSED, event -> {
      if (event.isControlDown() || event.isAltDown() || event.isMetaDown()
          || window.isModalOpen()) {
        return;
      }
      int index;
      switch (event.getCode()) {
        case DIGIT1:
        case NUMPAD1:
          index = 0;
          break;
        case DIGIT2:
        case NUMPAD2:
          index = 1;
          break;
        case DIGIT3:
        case NUMPAD3:
          index = 2;
          break;
        case DIGIT4:
        case NUMPAD4:
          index = 3;
          break;
        default:
          return;
      }
      if (index < actionButtons.size() && !actionButtons.get(index).isDisabled()) {
        actionButtons.get(index).fire();
      }
      event.consume();
    });
  }

  private void createPlayerPanel() {
    playerScroll = Ui.imageView("assets/scroll.png", 575, 500);
    place(playerScroll, 45, 230);
    Label name = heading(session.player().getName(), 28);
    place(name, 80, 270);
    place(Ui.imageView(session.player().getImage(), Ui.PORTRAIT_SIZE, Ui.PORTRAIT_SIZE),
        80, 320);
    playerHpBar = labelledBar(playerHpText, "limegreen", 80, 580);
    playerEnergyBar = labelledBar(playerEnergyText, "royalblue", 80, 620);
    place(heading("Actions", 24), 380, 320);
    int index = 0;
    for (String nameOfSkill : session.player().getSkills()) {
      Skill skill = Skills.get(nameOfSkill);
      Button button = new Button(skill.getName() + " [" + skill.getCost() + "]");
      button.setFont(Ui.headingFont(18));
      button.setPrefSize(180, 40);
      Ui.styleButton(button, 20);
      button.setOnAction(event -> {
        battle.playTurn(skill.getName());
        updateDisplay();
      });
      place(button, 370, 365 + index++ * 55);
      actionButtons.add(button);
    }
    index = 0;
    for (ItemType type : ItemType.getAll()) {
      Button button = new Button();
      button.setGraphic(Ui.imageView(session.data().value(type.imageKey()), 30, 30));
      button.setContentDisplay(ContentDisplay.TOP);
      button.setFont(Ui.headingFont(24));
      button.setAccessibleText(type.displayName());
      button.setPrefSize(60, 76);
      Ui.styleButton(button, 10);
      button.setOnAction(event -> {
        battle.useItem(type);
        updateDisplay();
      });
      place(button, 190 + index++ * 75, 145);
      itemButtons.put(type, button);
    }
  }

  private void createEnemyPanel() {
    Fighter enemy = battle.getEnemy();
    enemyScroll = Ui.imageView("assets/scroll.png", 420, 430);
    place(enemyScroll, 800, 30);
    place(heading(enemy.getMokepon().getName(), 28), 860, 60);
    place(Ui.imageView(enemy.getMokepon().getImage(), Ui.PORTRAIT_SIZE, Ui.PORTRAIT_SIZE),
        880, 110);
    enemyHpBar = createBar("limegreen", 20);
    enemyEnergyBar = createBar("royalblue", 20);
    place(enemyHpBar, 880, 380);
    place(enemyEnergyBar, 880, 410);
  }

  private Label heading(String text, int size) {
    Label label = new Label(text);
    label.setFont(Ui.headingFont(size));
    label.setTextFill(Color.BLACK);
    return label;
  }

  private ProgressBar createBar(String colour, int height) {
    ProgressBar bar = new ProgressBar();
    bar.setPrefSize(BAR_WIDTH, height);
    bar.setMinSize(BAR_WIDTH, height);
    bar.setMaxSize(BAR_WIDTH, height);
    bar.setStyle("-fx-accent:" + colour + ";");
    return bar;
  }

  private ProgressBar labelledBar(Label text, String colour, int x, int y) {
    ProgressBar bar = createBar(colour, PLAYER_BAR_HEIGHT);
    text.setFont(Font.font("Monospaced", FontWeight.BOLD, 20));
    text.setTextFill(Color.BLACK);
    text.setMouseTransparent(true);
    StackPane display = new StackPane(bar, text);
    display.setPrefSize(BAR_WIDTH, PLAYER_BAR_HEIGHT);
    place(display, x, y);
    return bar;
  }

  private void updateDisplay() {
    Fighter player = battle.getPlayer();
    Fighter enemy = battle.getEnemy();
    playerHpText.setText("HP: " + player.getHp() + "/" + player.getStat(Stat.HP));
    playerEnergyText.setText("Energy: " + player.getEnergy() + "/"
        + player.getStat(Stat.ENERGY));
    playerHpBar.setProgress(Ui.progress(player.getHp(), player.getStat(Stat.HP)));
    playerEnergyBar.setProgress(Ui.progress(player.getEnergy(), player.getStat(Stat.ENERGY)));
    enemyHpBar.setProgress(Ui.progress(enemy.getHp(), enemy.getStat(Stat.HP)));
    enemyEnergyBar.setProgress(Ui.progress(enemy.getEnergy(), enemy.getStat(Stat.ENERGY)));
    for (int i = 0; i < actionButtons.size(); i++) {
      actionButtons.get(i).setDisable(!battle.canUse(session.player().getSkills().get(i)));
    }
    for (ItemType type : ItemType.getAll()) {
      Button button = itemButtons.get(type);
      button.setText(Integer.toString(session.quantity(type)));
      button.setDisable(battle.isOver() || session.quantity(type) == 0);
    }
    String log = battle.getLog();
    battleLog.appendText(log.substring(displayedLogLength));
    displayedLogLength = log.length();
    battleLog.positionCaret(log.length());
    battleLog.setScrollTop(Double.MAX_VALUE);
    if (battle.isOver() && !outcomeShown) {
      outcomeShown = true;
      ImageView scroll = battle.hasWon() ? enemyScroll : playerScroll;
      Blend tint = new Blend();
      tint.setMode(BlendMode.SRC_ATOP);
      tint.setTopInput(new ColorInput(0, 0, scroll.getFitWidth(), scroll.getFitHeight(),
          Color.rgb(180, 30, 30, 0.35)));
      scroll.setEffect(tint);
      window.battleFinished(battle.hasWon(), enemy.getMokepon().getName());
    }
  }
}
