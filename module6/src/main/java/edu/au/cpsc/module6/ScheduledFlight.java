/*
 Project: Module 4 – Flight Designator App
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-14
 Description: JavaFX controller for table/detail editor with CSV persistence.
*/
package edu.au.cpsc.module6;

import java.time.LocalTime;
import java.util.Objects;

public class ScheduledFlight {
    private String designator;
    private String origin;       // same as "departureAirportIdent" in older code
    private String destination;  // same as "arrivalAirportIdent" in older code
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private String days = "";    // raw days string (for CSV IO helpers)

    public ScheduledFlight() { // IO sometimes creates then sets via setters
        this("", "", "", LocalTime.of(0,0), LocalTime.of(0,0));
    }

    public ScheduledFlight(String designator, String origin, String destination,
                           LocalTime departureTime, LocalTime arrivalTime) {
        this.designator = designator;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public static ScheduledFlight fromStrings(String designator, String origin, String destination,
                                              String departHHmm, String arriveHHmm) {
        return new ScheduledFlight(
                designator, origin, destination,
                LocalTime.parse(departHHmm), LocalTime.parse(arriveHHmm)
        );
    }

    public void updateFromStrings(String designator, String origin, String destination,
                                  String departHHmm, String arriveHHmm) {
        this.designator = designator;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = LocalTime.parse(departHHmm);
        this.arrivalTime = LocalTime.parse(arriveHHmm);
    }

    public String getDesignator()                 { return designator; }
    public String getFlightDesignator()           { return designator; }          // alias for IO
    public String getOrigin()                     { return origin; }
    public String getDestination()                { return destination; }
    public String getDepartureAirportIdent()      { return origin; }               // alias for IO
    public String getArrivalAirportIdent()        { return destination; }          // alias for IO
    public LocalTime getDepartureTime()           { return departureTime; }
    public LocalTime getArrivalTime()             { return arrivalTime; }
    public String daysAsString()                  { return days; }                 // simple raw string

    // --- setters expected by IO ---
    public void setFlightDesignator(String v)     { this.designator = v; }
    public void setDepartureAirportIdent(String v){ this.origin = v; }
    public void setArrivalAirportIdent(String v)  { this.destination = v; }
    public void setDepartureTime(LocalTime t)     { this.departureTime = t; }
    public void setArrivalTime(LocalTime t)       { this.arrivalTime = t; }
    public void setDepartureTime(String hhmm)     { this.departureTime = LocalTime.parse(hhmm); }
    public void setArrivalTime(String hhmm)       { this.arrivalTime = LocalTime.parse(hhmm); }
    public void setDaysFromString(String s)       { this.days = (s == null ? "" : s.trim()); }

    @Override public String toString() {
        return designator + " " + origin + "→" + destination + " " + departureTime + "-" + arrivalTime;
    }
    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduledFlight f)) return false;
        return Objects.equals(designator, f.designator) &&
                Objects.equals(departureTime, f.departureTime);
    }
    @Override public int hashCode() { return Objects.hash(designator, departureTime); }
}
