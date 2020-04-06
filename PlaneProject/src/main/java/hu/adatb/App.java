package hu.adatb;

import hu.adatb.utils.Utils;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {
    private static Stage primaryStage;

    public static String CurrentTime() {
        return "[" + java.time.LocalDateTime.now() + "] ";
    }

    public static Stage StageDeliver(String fileName, String title, String styleName) throws IOException {
        Parent root = FXMLLoader.load(App.class.getResource("/fxmlView/" + fileName));
        var scene = new Scene(root);

        if (styleName != "") {
            scene.getStylesheets().add(App.class.getResource("/css/" + styleName).toExternalForm());
        }

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.show();

        return primaryStage;
    }

    public static Stage StageDeliver(String fileName, String title) throws IOException {
        return StageDeliver(fileName, title, "");
    }

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        try {
            System.out.println("\n"+ App.CurrentTime() + "Opened application");
            StageDeliver("reg_or_login.fxml", "Repülőgép");
        } catch (IOException e) {
            Utils.showWarning("Nem sikerült megnyitni a főablakot");
            Platform.exit();
        }
    }

    public static void main(String[] args) {
        launch();
    }

}