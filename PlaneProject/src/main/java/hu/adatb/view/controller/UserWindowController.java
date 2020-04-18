package hu.adatb.view.controller;

import hu.adatb.App;
import hu.adatb.controller.AirportController;
import hu.adatb.controller.FlightController;
import hu.adatb.model.Flight;
import hu.adatb.utils.MainFxmlLoader;
import hu.adatb.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class UserWindowController implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private Button listButton;

    @FXML
    private Button ownButton;

    public UserWindowController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public static void goToMainForUser() {
        try {
            System.out.println("\n"+ App.CurrentTime() + "Destroyed login page");
            Stage stage = App.StageDeliver("user_main_window.fxml", "Főoldal");
            stage.setMaximized(true);
            System.out.println(App.CurrentTime() + "Opened user main page after login");
        } catch (IOException e) {
            System.out.println(App.CurrentTime() + "Cannot open main page");
            Utils.showWarning("Nem sikerült megnyitni a főoldalt");
            e.printStackTrace();
        }
    }

    @FXML
    public void listingFlights(ActionEvent actionEvent) {
        System.out.println(App.CurrentTime() + "Opened flights");
        listButton.setDisable(true);
        ownButton.setDisable(false);
        Pane view = MainFxmlLoader.getPage("flights_list.fxml", false);
        mainPane.setCenter(view);
    }

    @FXML
    public void listOwnFlights(ActionEvent actionEvent) {
        System.out.println(App.CurrentTime() + "Opened own flights");
        ownButton.setDisable(true);
        listButton.setDisable(false);
        Pane view = MainFxmlLoader.getPage("own_flights.fxml", false);
        mainPane.setCenter(view);
    }
}
