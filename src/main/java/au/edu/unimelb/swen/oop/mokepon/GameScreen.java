package au.edu.unimelb.swen.oop.mokepon;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

/** All screens share the specified dimensions and background. */
public abstract class GameScreen extends Pane {
  protected GameScreen() {
    setPrefSize(Ui.WIDTH, Ui.HEIGHT);
    setMinSize(Ui.WIDTH, Ui.HEIGHT);
    setMaxSize(Ui.WIDTH, Ui.HEIGHT);
    getChildren().add(Ui.imageView("assets/bg.png", Ui.WIDTH, Ui.HEIGHT));
  }

  protected void place(Node node, double x, double y) {
    node.setLayoutX(x);
    node.setLayoutY(y);
    getChildren().add(node);
  }
}
