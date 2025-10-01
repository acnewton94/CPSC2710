package edu.au.cpsc.module6;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*
 Project: Module 4 – Flight Designator App
 Author: Alex Newton
 Auburn Email: azn0100@auburn.edu
 Date: 2025-09-14
 Description: JavaFX controller for table/detail editor with CSV persistence.
*/

public class AirlineDatabaseIO {

    public static void save(AirlineDatabase ad, OutputStream strm) throws IOException {
        try (BufferedWriter w = new BufferedWriter(new OutputStreamWriter(strm, StandardCharsets.UTF_8))) {
            w.write("flightDesignator,departureIdent,departureTime,arrivalIdent,arrivalTime,days");
            w.newLine();
            for (ScheduledFlight sf : ad.getScheduledFlights()) {
                w.write(String.join(",",
                        esc(sf.getFlightDesignator()),
                        esc(sf.getDepartureAirportIdent()),
                        esc(sf.getDepartureTime().toString()),
                        esc(sf.getArrivalAirportIdent()),
                        esc(sf.getArrivalTime().toString()),
                        esc(sf.daysAsString())
                ));
                w.newLine();
            }
        }
    }

    public static AirlineDatabase load(InputStream strm) throws IOException {
        AirlineDatabase db = new AirlineDatabase();
        try (BufferedReader r = new BufferedReader(new InputStreamReader(strm, StandardCharsets.UTF_8))) {
            String header = r.readLine();
            String line;
            while ((line = r.readLine()) != null) {
                if (line.isBlank()) continue;
                List<String> c = split(line);
                if (c.size() < 6) continue;
                ScheduledFlight sf = new ScheduledFlight();
                sf.setFlightDesignator(unesc(c.get(0)));
                sf.setDepartureAirportIdent(unesc(c.get(1)));
                sf.setDepartureTime(LocalTime.parse(unesc(c.get(2))));
                sf.setArrivalAirportIdent(unesc(c.get(3)));
                sf.setArrivalTime(LocalTime.parse(unesc(c.get(4))));
                sf.setDaysFromString(unesc(c.get(5)));
                db.addScheduledFlight(sf);
            }
        }
        return db;
    }

    // ---- convenience: relative file "airline-db.csv" ----
    public static Path defaultDbPath() { return Path.of("airline-db.csv"); }

    public static void saveToDefault(AirlineDatabase ad) throws IOException {
        try (OutputStream os = Files.newOutputStream(defaultDbPath())) { save(ad, os); }
    }

    public static AirlineDatabase loadFromDefault() throws IOException {
        Path p = defaultDbPath();
        if (!Files.exists(p)) return new AirlineDatabase();
        try (InputStream is = Files.newInputStream(p)) { return load(is); }
    }

    // ---- CSV helpers ----
    private static String esc(String s) {
        return (s.contains(",") || s.contains("\"")) ? "\"" + s.replace("\"", "\"\"") + "\"" : s;
    }
    private static String unesc(String s) {
        s = s.trim();
        return (s.startsWith("\"") && s.endsWith("\"")) ? s.substring(1, s.length()-1).replace("\"\"", "\"") : s;
    }
    private static List<String> split(String line) {
        ArrayList<String> out = new ArrayList<>();
        boolean inQ = false; StringBuilder cur = new StringBuilder();
        for (int i=0;i<line.length();i++){
            char c=line.charAt(i);
            if (c=='"') { if (inQ && i+1<line.length() && line.charAt(i+1)=='"'){ cur.append('"'); i++; } else inQ=!inQ; }
            else if (c==',' && !inQ) { out.add(cur.toString()); cur.setLength(0); }
            else cur.append(c);
        }
        out.add(cur.toString());
        return out;
    }
}
