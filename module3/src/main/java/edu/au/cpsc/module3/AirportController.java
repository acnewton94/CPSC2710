package edu.au.cpsc.module3;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.util.List;

public class AirportController {

    @FXML private TextField identField;
    @FXML private TextField iataField;
    @FXML private TextField localField;

    @FXML private TextField typeField;
    @FXML private TextField nameField;
    @FXML private TextField elevationField;
    @FXML private TextField countryField;
    @FXML private TextField regionField;
    @FXML private TextField municipalityField;

    @FXML private Button   searchButton;
    @FXML private WebView  mapView;

    private List<Airport> airports;
    private WebEngine engine;

    @FXML
    public void initialize() {
        // Load CSV into memory once
        try {
            airports = Airport.readAll();
            System.out.println("Loaded airports: " + (airports == null ? 0 : airports.size()));
        } catch (IOException e) {
            System.out.println("Failed to read CSV: " + e.getMessage());
        }

        if (identField  != null) identField.setOnAction(this::handleSearch);
        if (iataField   != null) iataField.setOnAction(this::handleSearch);
        if (localField  != null) localField.setOnAction(this::handleSearch);
        if (searchButton!= null) searchButton.setOnAction(this::handleSearch);

        engine = mapView.getEngine();
        loadWindy(39.0, -96.0, 4); // center US
    }

    @FXML
    private void handleSearch(ActionEvent e) { doSearch(); }

    private void doSearch() {
        if (airports == null || airports.isEmpty()) return;

        String ident = val(identField);
        String iata  = val(iataField);
        String local = val(localField);

        String mode; String key;
        if (!ident.isEmpty())      { mode = "ident"; key = ident; }
        else if (!iata.isEmpty())  { mode = "iata";  key = iata;  }
        else if (!local.isEmpty()) { mode = "local"; key = local; }
        else { clearOutputs(); return; }

        Airport found = findAirport(mode, key);
        if (found == null) { clearOutputs(); return; }

        typeField.setText(s(found.getType()));
        nameField.setText(s(found.getName()));
        elevationField.setText(found.getElevationFt()==null ? "" : found.getElevationFt().toString());
        countryField.setText(s(found.getIsoCountry()));
        regionField.setText(s(found.getIsoRegion()));
        municipalityField.setText(s(found.getMunicipality()));

        String coords = found.getCoordinates();
        if (coords == null || !coords.contains(",")) {
            System.out.println("Missing/invalid coordinates for " + s(found.getIdent()) + ": " + coords);
            return;
        }
        try {
            String[] p = coords.split(",");
            double lon = Double.parseDouble(p[0].trim());
            double lat = Double.parseDouble(p[1].trim());
            loadWindy(lat, lon, 12); // zoom ~ city/airport
        } catch (NumberFormatException ex) {
            System.out.println("Bad numeric coordinates for " + s(found.getIdent()) + ": " + coords);
        }
    }

    /** Find first matching airport by chosen key, preferring rows that have coordinates. */
    private Airport findAirport(String mode, String key) {
        Airport firstWithCoords = null;
        for (Airport a : airports) {
            boolean match =
                    ("ident".equals(mode) && eq(a.getIdent(), key)) ||
                            ("iata".equals(mode)  && eq(a.getIataCode(), key)) ||
                            ("local".equals(mode) && eq(a.getLocalCode(), key));
            if (!match) continue;

            if (hasCoords(a)) return a;
            if (firstWithCoords == null) firstWithCoords = a;
        }
        return firstWithCoords;
    }

    private static boolean hasCoords(Airport a){
        String c = a.getCoordinates();
        if (c == null) return false;
        int i = c.indexOf(',');
        if (i < 0) return false;
        try {
            Double.parseDouble(c.substring(0,i).trim());
            Double.parseDouble(c.substring(i+1).trim());
            return true;
        } catch (Exception e) { return false; }
    }

    /** Build Windy URL and load in WebView. */
    private void loadWindy(double lat, double lon, int zoom){
        String url = "https://www.windy.com/?" + lat + "," + lon + "," + zoom;
        engine.load(url);
    }

    private void clearOutputs() {
        typeField.clear(); nameField.clear(); elevationField.clear();
        countryField.clear(); regionField.clear(); municipalityField.clear();
    }

    // tiny helpers
    private static String val(TextField tf){ return (tf==null||tf.getText()==null) ? "" : tf.getText().trim(); }
    private static boolean eq(String a, String b){ return a!=null && b!=null && a.equalsIgnoreCase(b); }
    private static String s(String v){ return v==null ? "" : v; }
}
