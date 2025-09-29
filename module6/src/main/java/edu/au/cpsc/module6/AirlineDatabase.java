/*
 Project: Module 4 – Flight Designator App
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-14
 Description: JavaFX controller for table/detail editor with CSV persistence.
*/
package edu.au.cpsc.module6;

import java.util.ArrayList;
import java.util.List;

public class AirlineDatabase {
    private final List<ScheduledFlight> flights = new ArrayList<>();

    // used by controller
    public List<ScheduledFlight> getAll() { return flights; }
    public void add(ScheduledFlight f)    { if (f != null) flights.add(f); }
    public void remove(ScheduledFlight f) { flights.remove(f); }
    public void save() { /* no-op for this assignment */ }

    // aliases used by older IO code
    public List<ScheduledFlight> getScheduledFlights() { return flights; }
    public void addScheduledFlight(ScheduledFlight f)  { add(f); }
}
