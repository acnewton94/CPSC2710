package edu.au.cpsc.module4;

import java.time.LocalTime;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/*
 Project: Module 4 – Flight Designator App
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-14
 Description: JavaFX controller for table/detail editor with CSV persistence.
*/

public class ScheduledFlight {
    private String flightDesignator;
    private String departureAirportIdent;
    private LocalTime departureTime;
    private String arrivalAirportIdent;
    private LocalTime arrivalTime;
    private Set<Day> daysOfWeek = new HashSet<>();

    // R = Thu, U = Sun
    public enum Day { M, T, W, R, F, U, S }

    public String getFlightDesignator() { return flightDesignator; }
    public void setFlightDesignator(String s) { req(s,"flightDesignator"); this.flightDesignator = s.trim(); }

    public String getDepartureAirportIdent() { return departureAirportIdent; }
    public void setDepartureAirportIdent(String s) { req(s,"departureAirportIdent"); this.departureAirportIdent = s.trim(); }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime t) {
        if (t == null) throw new IllegalArgumentException("departureTime cannot be null");
        this.departureTime = t;
    }

    public String getArrivalAirportIdent() { return arrivalAirportIdent; }
    public void setArrivalAirportIdent(String s) { req(s,"arrivalAirportIdent"); this.arrivalAirportIdent = s.trim(); }

    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime t) {
        if (t == null) throw new IllegalArgumentException("arrivalTime cannot be null");
        this.arrivalTime = t;
    }

    public Set<Day> getDaysOfWeek() { return daysOfWeek; }
    public void setDaysOfWeek(Set<Day> d) {
        if (d == null) throw new IllegalArgumentException("daysOfWeek cannot be null");
        this.daysOfWeek = new HashSet<>(d);
    }

    // Convenience: parse "MTWRFUS" -> set
    public void setDaysFromString(String s) {
        if (s == null) throw new IllegalArgumentException("days string cannot be null");
        EnumSet<Day> out = EnumSet.noneOf(Day.class);
        for (char c : s.toUpperCase().trim().toCharArray()) {
            switch (c) {
                case 'M' -> out.add(Day.M);
                case 'T' -> out.add(Day.T);
                case 'W' -> out.add(Day.W);
                case 'R' -> out.add(Day.R);
                case 'F' -> out.add(Day.F);
                case 'U' -> out.add(Day.U);
                case 'S' -> out.add(Day.S);
                default -> { /* ignore */ }
            }
        }
        this.daysOfWeek = out;
    }

    // Display: ordered one-letter string (MTWRFUS)
    public String daysAsString() {
        String order = "MTWRFUS";
        StringBuilder sb = new StringBuilder();
        for (char c : order.toCharArray()) {
            if (daysOfWeek.contains(Day.valueOf(String.valueOf(c)))) sb.append(c);
        }
        return sb.toString();
    }

    private static void req(String s, String f) {
        if (s == null || s.trim().isEmpty()) throw new IllegalArgumentException(f + " cannot be null or empty");
    }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduledFlight that)) return false;
        return Objects.equals(flightDesignator, that.flightDesignator) &&
                Objects.equals(departureAirportIdent, that.departureAirportIdent) &&
                Objects.equals(departureTime, that.departureTime) &&
                Objects.equals(arrivalAirportIdent, that.arrivalAirportIdent) &&
                Objects.equals(arrivalTime, that.arrivalTime) &&
                Objects.equals(daysOfWeek, that.daysOfWeek);
    }
    @Override public int hashCode() {
        return Objects.hash(flightDesignator, departureAirportIdent, departureTime,
                arrivalAirportIdent, arrivalTime, daysOfWeek);
    }
}
