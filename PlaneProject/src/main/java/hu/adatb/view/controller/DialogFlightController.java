package hu.adatb.view.controller;

import hu.adatb.controller.*;
import hu.adatb.model.Airport;
import hu.adatb.model.City;
import hu.adatb.model.Flight;
import hu.adatb.model.Plane;
import hu.adatb.utils.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class DialogFlightController implements Initializable {

    @FXML
    ComboBox<Airport> fromAirport;

    @FXML
    ComboBox<Airport> toAirport;

    @FXML
    DatePicker dateBegin;

    @FXML
    ComboBox<Plane> planes;

    @FXML
    Label errorMsgName;

    @FXML
    Button addButton;

    @FXML
    Button editButton;

    @FXML
    Spinner<Integer> freeSeatsSpinner;

    private Flight flight = new Flight();
    private List<Flight> flights;
    private static Flight selectedFlight;
    private static boolean isAdd;

    public DialogFlightController() {
    }

    @FXML
    private void save(ActionEvent event) {
        flight.setFromAirport(fromAirport.getSelectionModel().getSelectedItem());
        flight.setToAirport(toAirport.getSelectionModel().getSelectedItem());
        flight.setPlane(planes.getSelectionModel().getSelectedItem());
        flight.setDateTime(dateBegin.getValue().atStartOfDay());
        if (FlightController.getInstance().add(flight)) {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            Utils.showWarning("Nem sikerült menteni az új járatot");
        }
    }

    @FXML
    public void edit(ActionEvent event) {
       /* if (FlightController.getInstance().update(flight)) {
            Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
            stage.close();
        } else {
            Utils.showWarning("Nem sikerült menteni a módosított járatot");
        }*/
    }

    @FXML
    private void cancel(ActionEvent event){
        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Plane> planesList = PlaneController.getInstance().getAll();
        ObservableList<Plane> obsPlaneList = FXCollections.observableList(planesList);

        List<Airport> airportList = AirportController.getInstance().getAll();
        ObservableList<Airport> obsAirportList = FXCollections.observableList(airportList);

        freeSeatsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                10, 1000, isAdd ? 10 : selectedFlight.getFreeSeats(), 10));

        planes.getItems().addAll(obsPlaneList);
        fromAirport.getItems().addAll(obsAirportList);
        toAirport.getItems().addAll(obsAirportList);

        Callback<ListView<Plane>, ListCell<Plane>> factoryPlane = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Plane item, boolean empty){
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };

        Callback<ListView<Airport>, ListCell<Airport>> factoryAirport = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Airport item, boolean empty){
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };

        fromAirport.setCellFactory(factoryAirport);
        fromAirport.setButtonCell(factoryAirport.call(null));
        toAirport.setCellFactory(factoryAirport);
        toAirport.setButtonCell(factoryAirport.call(null));

        planes.setCellFactory(factoryPlane);
        planes.setButtonCell(factoryPlane.call(null));

        flights = FlightController.getInstance().getAll();

        flight.vogueProperty().set(0);
        flight.freeSeatsProperty().bind(freeSeatsSpinner.valueProperty());

        //longitudeSpinner.getValueFactory().valueProperty().bindBidirectional(airport.longitudeProperty().asObject());
        //latitudeSpinner.getValueFactory().valueProperty().bindBidirectional(airport.latitudeProperty().asObject());

    }

    private void FieldValidator() {
    }

    private void InitTable() {
        freeSeatsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(
                10, 1000, isAdd ? 10 : selectedFlight.getFreeSeats(), 10));
/*
        flight.nameProperty().bind(nameField.textProperty());
        flight.seatsProperty().bind(seatsSpinner.valueProperty());
        flight.speedProperty().bind(speedSpinner.valueProperty());
        flight.setId(selectedPlane.getId());*/
    }

    public static void setIsAdd(boolean isAdd) {
        DialogFlightController.isAdd = isAdd;
    }

    public static void setSelectedFlight(Flight flight) {
        selectedFlight = flight;
    }

}
