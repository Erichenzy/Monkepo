package au.edu.unimelb.swen.oop.mokepon;

import java.util.ArrayList;
import java.util.List;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** Project 1's selection panel, extended with scrolling and elemental types. */
public class SelectScreen extends GameScreen {
  private static final int ROW_SPACING = 26;
  private final GameData data;
  private final GameWindow window;
  private final List<Button> characterButtons = new ArrayList<>();
  private final Label[] statValues = new Label[8];
  private final ImageView portrait = new ImageView();
  private final Label nameLabel = new Label("Select a Mokepon");
  private final Button chooseButton = new Button("Choose\nMokepon");
  private Mokepon selected;

  public SelectScreen(GameData data, GameWindow window) {
    this.data = data;
    this.window = window;
    Label heading = new Label("Choose your Mokepon");
    heading.setFont(Ui.headingFont(34));
    heading.setEffect(outline());
    place(heading, 40, 40);
    createList();

    Circle circle = new Circle(160, Color.rgb(211, 211, 211, 0.5));
    circle.setStroke(Color.BLACK);
    circle.setStrokeWidth(2);
    place(circle, 800, 200);
    portrait.setFitWidth(Ui.PORTRAIT_SIZE);
    portrait.setFitHeight(Ui.PORTRAIT_SIZE);
    portrait.setSmooth(false);
    place(portrait, 672, 72);

    nameLabel.setFont(Ui.headingFont(30));
    nameLabel.setEffect(outline());
    nameLabel.setAlignment(Pos.CENTER);
    nameLabel.setContentDisplay(ContentDisplay.RIGHT);
    nameLabel.setPrefSize(450, 50);
    place(nameLabel, 575, 365);
    place(Ui.imageView("assets/scroll.png", 450, 300), 575, 425);
    String[] names = {"HP:", "Energy:", "Strength:", "Accuracy:", "Defence:",
      "Dodge:", "Type:", "Skills:"};
    for (int i = 0; i < names.length; i++) {
      Label label = new Label(names[i]);
      place(label, 625, 466 + i * ROW_SPACING);
      statValues[i] = new Label();
      statValues[i].setPrefWidth(245);
      statValues[i].setWrapText(true);
      place(statValues[i], 715, 466 + i * ROW_SPACING);
    }
    chooseButton.setFont(Ui.headingFont(24));
    chooseButton.setPrefSize(160, 160);
    chooseButton.setDisable(true);
    Ui.styleButton(chooseButton, 10);
    chooseButton.setOnAction(event -> window.startAdventure(selected));
    place(chooseButton, 1050, 495);
  }

  private void createList() {
    VBox choices = new VBox(10);
    for (Mokepon mokepon : data.mokepons()) {
      Button button = new Button(mokepon.getName());
      button.setFont(Ui.headingFont(20));
      button.setPrefSize(280, 40);
      button.setMinHeight(40);
      button.setOnAction(event -> select(mokepon, button));
      characterButtons.add(button);
      choices.getChildren().add(button);
      setSelectionStyle(button, false);
    }
    ScrollPane list = new ScrollPane(choices);
    list.setPrefSize(305, 610);
    list.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    list.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    list.setStyle("-fx-background: transparent;-fx-background-color: transparent;");
    place(list, 45, 100);
  }

  private void select(Mokepon mokepon, Button selectedButton) {
    selected = mokepon;
    for (Button button : characterButtons) {
      setSelectionStyle(button, button == selectedButton);
    }
    nameLabel.setText(mokepon.getName());
    nameLabel.setGraphic(mokepon.getType() == MokeponType.NORMAL ? null
        : Ui.imageView(data.typeImage(mokepon.getType()), 30, 30));
    portrait.setImage(Ui.image(mokepon.getImage()));
    Stat[] stats = Stat.getAll();
    for (int i = 0; i < stats.length; i++) {
      statValues[i].setText(Integer.toString(stats[i].baseValue(mokepon)));
    }
    statValues[6].setText(mokepon.getType().getName());
    statValues[7].setText(String.join(", ", mokepon.getSkills()));
    chooseButton.setDisable(false);
  }

  private void setSelectionStyle(Button button, boolean selected) {
    button.setStyle("-fx-background-color: rgba(211,211,211,"
        + (selected ? 0.75 : 0.5) + ");-fx-background-radius:20;-fx-text-fill:black;");
  }

  private DropShadow outline() {
    DropShadow outline = new DropShadow(2, Color.WHITE);
    outline.setSpread(1);
    return outline;
  }
}
