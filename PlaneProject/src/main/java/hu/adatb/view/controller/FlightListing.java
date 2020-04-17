package hu.adatb.view.controller;

import hu.adatb.App;
import hu.adatb.controller.AirportController;
import hu.adatb.controller.FlightController;
import hu.adatb.controller.PlaneController;
import hu.adatb.model.Airport;
import hu.adatb.model.Flight;
import hu.adatb.model.Plane;
import hu.adatb.utils.Utils;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class FlightListing implements Initializable {

    @FXML
    ComboBox<String> fromAirport;

    @FXML
    ComboBox<String> toAirport;

    @FXML
    DatePicker dateBegin;

    @FXML
    DatePicker dateEnd;

    @FXML
    public TableView<Flight> table = new TableView<>();

    @FXML
    private TableColumn<Flight, String> fromCol;

    @FXML
    private TableColumn<Flight, String> toCol;

    @FXML
    private TableColumn<Flight, String> whenCol;

    @FXML
    private TableColumn<Flight, String> timeCol;

    @FXML
    private TableColumn<Flight, String> withCol;

    @FXML
    private TableColumn<Flight, Integer> seatCol;

    @FXML
    private TableColumn<Flight, Void> actionCol;

    @FXML
    private Button searchButton;

    @FXML
    private Label infoText;

    private List<Flight> flights;
    private List<Airport> airports;
    private static Flight bookedFlight;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        flights = FlightController.getInstance().getAll();
        airports = AirportController.getInstance().getAll();

        for (var airport: airports) {
            fromAirport.getItems().addAll(airport.getName());
            toAirport.getItems().addAll(airport.getName());
        }

        searchButton.disableProperty().bind(fromAirport.valueProperty().isNull()
                .or(toAirport.valueProperty().isNull())
                .or(dateBegin.valueProperty().isNull())
                .or(dateEnd.valueProperty().isNull()));
    }

    private double GetLatitudeByName(String airportName) {
        var airport = airports.stream()
                .filter(airport1 -> airport1.getName().equals(airportName)).collect(Collectors.toList()).get(0);

        return airport.getLatitude();
    }

    private double GetLongitudeByName(String airportName) {
        var airport = airports.stream()
                .filter(airport1 -> airport1.getName().equals(airportName)).collect(Collectors.toList()).get(0);

        return airport.getLongitude();
    }

    @FXML
    public void search(ActionEvent actionEvent) {
        var selectedFromAirport = fromAirport.getSelectionModel().getSelectedItem();
        var selectedToAirport = toAirport.getSelectionModel().getSelectedItem();
        var selectedDateBegin = dateBegin.getValue();
        var selectedDateEnd = dateEnd.getValue();

        List<Flight> filteredFlights;

        filteredFlights = flights.stream()
                .filter(flight-> flight.getFromAirport().equals(selectedFromAirport)
                        && flight.getToAirport().equals(selectedToAirport)
                        && ((flight.getDateTime().toLocalDate()).isAfter(selectedDateBegin)
                            || (flight.getDateTime().toLocalDate()).isEqual(selectedDateBegin))
                        && ((flight.getDateTime().toLocalDate()).isBefore(selectedDateEnd)
                            || (flight.getDateTime().toLocalDate()).isEqual(selectedDateEnd))
                ).collect(Collectors.toList());

        table.setItems(FXCollections.observableList(filteredFlights));

        fromCol.setCellValueFactory(new PropertyValueFactory<>("fromAirport"));
        toCol.setCellValueFactory(new PropertyValueFactory<>("toAirport"));
        whenCol.setCellValueFactory(__-> new SimpleStringProperty(__.getValue().getDateTimeInRightFormat()));
        timeCol.setCellValueFactory(
                __-> new SimpleStringProperty(__.getValue()
                        .getTravelTime(GetLatitudeByName(selectedFromAirport),
                                        GetLongitudeByName(selectedFromAirport),
                                        GetLatitudeByName(selectedToAirport),
                                        GetLongitudeByName(selectedToAirport),
                                        __.getValue().getPlane())));
        withCol.setCellValueFactory(__-> new SimpleStringProperty(__.getValue().getPlane().getName()));
        seatCol.setCellValueFactory(new PropertyValueFactory<>("freeSeats"));


        actionCol.setCellFactory(param ->
                new TableCell<>(){

                    Button bookingButton = new Button("Foglalás");

                    {
                        bookingButton.setOnAction(event -> {
                            bookedFlight = table.getItems().get(getIndex());

                            try {
                                App.DialogDeliver("add_booking.fxml","Foglalás", false);
                            } catch (IOException e) {
                                Utils.showWarning("Nem sikerült megnyitni a foglalás ablakot");
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(bookingButton);
                        }
                    }
                }
        );

        if (filteredFlights.size() > 0) {
            table.setVisible(true);
            infoText.setVisible(false);
        } else {
            table.setVisible(false);
            infoText.setVisible(true);
            infoText.setText("Nincs a szűrőknek megfelelő járat");
        }
    }

    public static Flight getBookedFlight() {
        return bookedFlight;
    }
}
