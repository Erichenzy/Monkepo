package au.edu.unimelb.swen.oop.mokepon;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Button;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/** Shared assets and visual rules; image paths still come from the data file. */
public final class Ui {
  public static final int WIDTH = 1280;
  public static final int HEIGHT = 768;
  public static final int PORTRAIT_SIZE = 256;
  public static final double MIN_VISIBLE_PROGRESS = 0.03;
  private static final Map<String, Image> IMAGES = new HashMap<>();

  private Ui() {}

  public static Image image(String path) {
    if (!IMAGES.containsKey(path)) {
      IMAGES.put(path, new Image(Path.of(path).toUri().toString()));
    }
    return IMAGES.get(path);
  }

  public static ImageView imageView(String path, double width, double height) {
    ImageView image = new ImageView(image(path));
    image.setFitWidth(width);
    image.setFitHeight(height);
    image.setSmooth(false);
    return image;
  }

  public static ImageView imageView(String path, double width, double height, String colour) {
    ImageView image = imageView(path, width, height);
    Blend blend = new Blend();
    blend.setMode(BlendMode.SRC_ATOP);
    blend.setTopInput(new ColorInput(0, 0, width, height, Color.web(colour)));
    image.setEffect(blend);
    return image;
  }

  public static Font headingFont(double size) {
    return Font.loadFont(Path.of("assets/GrenzeGotisch-Black.ttf").toUri().toString(), size);
  }

  public static double progress(int current, int maximum) {
    return current == 0 ? 0 : Math.max(MIN_VISIBLE_PROGRESS, (double) current / maximum);
  }

  public static void styleButton(Button button, int radius) {
    updateButton(button, radius);
    button.disabledProperty().addListener((observable, oldValue, newValue) ->
        updateButton(button, radius));
    button.pressedProperty().addListener((observable, oldValue, newValue) ->
        updateButton(button, radius));
  }

  private static void updateButton(Button button, int radius) {
    double opacity = button.isDisabled() ? 0.2 : button.isPressed() ? 0.75 : 0.5;
    button.setStyle("-fx-background-color: rgba(211,211,211," + opacity + ");"
        + "-fx-background-radius: " + radius + ";-fx-text-fill: black;"
        + "-fx-text-alignment: center;-fx-opacity: 1;");
  }
}
