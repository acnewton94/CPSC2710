package edu.au.cpsc.module4;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/*
 Project: Module 4 – Flight Designator App
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-14
 Description: JavaFX controller for table/detail editor with CSV persistence.
*/

public class AirlineDatabase {
    private final ObservableList<ScheduledFlight> flights = FXCollections.observableArrayList();

    public ObservableList<ScheduledFlight> getScheduledFlights() { return flights; }

    public void addScheduledFlight(ScheduledFlight sf) {
        if (sf == null) throw new IllegalArgumentException("ScheduledFlight cannot be null");
        flights.add(sf);
    }
    public void removeScheduledFlight(ScheduledFlight sf) { flights.remove(sf); }

    public void updateScheduledFlight(ScheduledFlight updated) {
        int idx = flights.indexOf(updated);
        if (idx >= 0) flights.set(idx, updated); else flights.add(updated);
    }
}
