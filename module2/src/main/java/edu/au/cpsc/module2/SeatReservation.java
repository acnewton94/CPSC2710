import java.time.LocalDate;

public class SeatReservation {
    private String flightDesignator;
    private LocalDate flightDate;
    private String firstName;
    private String lastName;

    public String getFlightDesignator() { return flightDesignator; }

    public void setFlightDesignator(String fd) {
        if (fd == null) {
            throw new IllegalArgumentException("flightDesignator cannot be null");
        }
        int len = fd.length();
        if (len < 4 || len > 6) {
            throw new IllegalArgumentException(
                "flightDesignator length must be between 4 and 6 characters"
            );
        }
        this.flightDesignator = fd;
    }

    public LocalDate getFlightDate() { return flightDate; }
    public void setFlightDate(LocalDate date) { this.flightDate = date; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String fn) { this.firstName = fn; }

    public String getLastName() { return lastName; }
    public void setLastName(String ln) { this.lastName = ln; }

    @Override
    public String toString() {
        String fd  = (flightDesignator == null || flightDesignator.isEmpty()) ? "null" : flightDesignator;
        String dt  = (flightDate == null) ? "null" : flightDate.toString();
        String fn  = (firstName == null || firstName.isEmpty()) ? "null" : firstName;
        String ln  = (lastName == null || lastName.isEmpty()) ? "null" : lastName;

        return "SeatReservation{flightDesignator=" + fd
             + ",flightDate=" + dt
             + ",firstName=" + fn
             + ",lastName=" + ln + "}";
    }
}
