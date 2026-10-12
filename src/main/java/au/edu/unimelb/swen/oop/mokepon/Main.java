package au.edu.unimelb.swen.oop.mokepon;

import java.io.IOException;
import java.nio.file.Path;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {
  @Override
  public void start(Stage stage) {
    String path = null;
    for (String argument : getParameters().getRaw()) {
      if (argument.startsWith("data=")) {
        path = argument.substring("data=".length());
      }
    }
    if (path == null) {
      System.err.println("Error loading data file: missing data=relativeFilePath argument");
      Platform.exit();
      return;
    }
    try {
      new GameWindow(GameData.load(Path.of(path))).show();
    } catch (IOException exception) {
      System.err.println("Error loading " + path);
      Platform.exit();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
