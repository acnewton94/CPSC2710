/**
 * Alex Newton
 * azn0100@auburn.edu
 * September 2, 2025
 * Description: This class represents a seat reservation with passenger info and options.
 */
package edu.au.cpsc.module2;

import java.time.LocalDate;

public class SeatReservation {
    private String flightDesignator;
    private LocalDate flightDate;
    private String firstName;
    private String lastName;

    private int numberOfBags;
    private boolean flyingWithInfant;
    private boolean flyingWithTravelInsurance;

    private int numberOfPassengers;

    public String getFlightDesignator() {
        return flightDesignator;
    }
    public void setFlightDesignator(String fd) {
        if (fd == null) {
            throw new IllegalArgumentException("Flight designator cannot be null");
        }
        int len = fd.length();
        if (len < 4 || len > 6) {
            throw new IllegalArgumentException("Flight designator length must be between 4 and 6 characters");
        }
        this.flightDesignator = fd;
    }

    public LocalDate getFlightDate() {
        return flightDate;
    }
    public void setFlightDate(LocalDate date) {
        this.flightDate = date;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String fn) {
        if (fn == null) {
            throw new IllegalArgumentException("First name cannot be null");
        }
        int len = fn.length();
        if (len < 2 || len > 15) {
            throw new IllegalArgumentException("First name must be between 2 and 15 characters");
        }
        this.firstName = fn;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String ln) {
        if (ln == null) {
            throw new IllegalArgumentException("Last name cannot be null");
        }
        int len = ln.length();
        if (len < 2 || len > 15) {
            throw new IllegalArgumentException("Last name must be between 2 and 15 characters");
        }
        this.lastName = ln;
    }

    public int getNumberOfBags() {
        return numberOfBags;
    }
    public void setNumberOfBags(int numberOfBags) {
        if (numberOfBags < 0) {
            throw new IllegalArgumentException("Number of bags cannot be negative");
        }
        this.numberOfBags = numberOfBags;
    }

    public int getNumberOfPassengers() {
        return numberOfPassengers;
    }
    public void setNumberOfPassengers(int numberOfPassengers) {
        if (numberOfPassengers < 0) {
            throw new IllegalArgumentException("Number of passengers cannot be negative");
        }
        this.numberOfPassengers = numberOfPassengers;
    }

    // --- Flying with infant ---
    public boolean isFlyingWithInfant() {
        return flyingWithInfant;
    }
    public void makeFlyingWithInfant() {
        this.flyingWithInfant = true;
    }
    public void makeNotFlyingWithInfant() {
        this.flyingWithInfant = false;
    }

    public boolean hasTravelInsurance() {
        return flyingWithTravelInsurance;
    }
    public void makeFlyingWithTravelInsurance() {
        this.flyingWithTravelInsurance = true;
    }
    public void makeNotFlyingWithTravelInsurance() {
        this.flyingWithTravelInsurance = false;
    }

    @Override
    public String toString() {
        return "SeatReservation{" +
                "flightDesignator='" + flightDesignator + '\'' +
                ", flightDate=" + flightDate +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", numberOfBags=" + numberOfBags +
                ", numberOfPassengers=" + numberOfPassengers +
                ", flyingWithInfant=" + flyingWithInfant +
                ", flyingWithTravelInsurance=" + flyingWithTravelInsurance +
                '}';
    }
}

