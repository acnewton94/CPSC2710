package edu.au.cpsc.module7;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a reading plan configuration and schedule.
 */
public class ReadingPlanConfig {

    private final LocalDate startDate;
    private final LocalDate endDate;
    private final int totalPages;
    private final int daysPerWeek;
    private final boolean includeWeekends;

    private final List<LocalDate> scheduledDays;

    public ReadingPlanConfig(LocalDate startDate, LocalDate endDate,
                             int totalPages, int daysPerWeek,
                             boolean includeWeekends) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalPages = totalPages;
        this.daysPerWeek = daysPerWeek;
        this.includeWeekends = includeWeekends;
        this.scheduledDays = new ArrayList<>();
        generatePlan();
    }

    private void generatePlan() {
        LocalDate current = startDate;

        while (!current.isAfter(endDate)) {
            if (isValidReadingDay(current)) {
                scheduledDays.add(current);
            }
            current = current.plusDays(1);
        }
    }

    private boolean isValidReadingDay(LocalDate date) {
        switch (date.getDayOfWeek()) {
            case SATURDAY:
            case SUNDAY:
                return includeWeekends;
            default:
                return true;
        }
    }

    public List<LocalDate> getScheduledDays() {
        return scheduledDays;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getPagesPerDay() {
        return (int) Math.ceil((double) totalPages / scheduledDays.size());
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
