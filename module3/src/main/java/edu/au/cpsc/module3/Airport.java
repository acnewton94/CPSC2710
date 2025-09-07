package edu.au.cpsc.module3;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Airport {
    private final String ident;
    private final String type;
    private final String name;
    private final Integer elevationFt;
    private final String isoCountry;
    private final String isoRegion;
    private final String municipality;
    private final String gpsCode;
    private final String iataCode;
    private final String localCode;

    private final String coordinates;

    public Airport(String ident, String type, String name, Integer elevationFt,
                   String isoCountry, String isoRegion, String municipality,
                   String gpsCode, String iataCode, String localCode, String coordinates) {
        this.ident = ident; this.type = type; this.name = name; this.elevationFt = elevationFt;
        this.isoCountry = isoCountry; this.isoRegion = isoRegion; this.municipality = municipality;
        this.gpsCode = gpsCode; this.iataCode = iataCode; this.localCode = localCode; this.coordinates = coordinates;
    }

    public String getIdent()        { return ident; }
    public String getType()         { return type; }
    public String getName()         { return name; }
    public Integer getElevationFt() { return elevationFt; }
    public String getIsoCountry()   { return isoCountry; }
    public String getIsoRegion()    { return isoRegion; }
    public String getMunicipality() { return municipality; }
    public String getGpsCode()      { return gpsCode; }
    public String getIataCode()     { return iataCode; }
    public String getLocalCode()    { return localCode; }
    public String getCoordinates()  { return coordinates; }

    /** Load from classpath: /edu/au/cpsc/module3/airport-codes.csv */
    public static List<Airport> readAll() throws IOException {
        String resource = "/edu/au/cpsc/module3/airport-codes.csv";
        try (java.io.BufferedReader br = new java.io.BufferedReader(
                new java.io.InputStreamReader(
                        java.util.Objects.requireNonNull(Airport.class.getResourceAsStream(resource),
                                "CSV not found on classpath: " + resource),
                        java.nio.charset.StandardCharsets.UTF_8))) {

            String header = br.readLine();
            if (header == null) return java.util.List.of();

            java.util.List<String> head = parseCsv(header);
            java.util.Map<String,Integer> idx = new java.util.HashMap<>();
            for (int i = 0; i < head.size(); i++) idx.put(head.get(i).trim().toLowerCase(), i);

            int iIdent        = col(idx, "ident");
            int iType         = col(idx, "type");
            int iName         = col(idx, "name");
            int iElev         = col(idx, "elevation_ft");
            int iIsoCountry   = col(idx, "iso_country");
            int iIsoRegion    = col(idx, "iso_region");
            int iMunicipality = col(idx, "municipality");
            int iGpsCode      = col(idx, "gps_code");
            int iIata         = col(idx, "iata_code");
            int iLocal        = col(idx, "local_code");
            int iCoords       = col(idx, "coordinates"); // last column in this dataset

            java.util.List<Airport> out = new java.util.ArrayList<>(60000);
            String line;
            while ((line = br.readLine()) != null) {
                java.util.List<String> row = parseCsv(line);

                String coords = get(row, iCoords);
                if ((coords != null && !coords.contains(","))
                        && iCoords == head.size() - 1
                        && row.size() > head.size()) {
                    StringBuilder sb = new StringBuilder(coords);
                    for (int k = iCoords + 1; k < row.size(); k++) {
                        String extra = row.get(k);
                        if (extra != null && !extra.isEmpty()) {
                            sb.append(',').append(extra.trim());
                        }
                    }
                    coords = sb.toString();
                }

                String ident      = get(row, iIdent);
                String type       = get(row, iType);
                String name       = get(row, iName);
                Integer elev      = parseInt(get(row, iElev));
                String isoCountry = get(row, iIsoCountry);
                String isoRegion  = get(row, iIsoRegion);
                String muni       = get(row, iMunicipality);
                String gps        = get(row, iGpsCode);
                String iata       = get(row, iIata);
                String local      = get(row, iLocal);

                out.add(new Airport(ident, type, name, elev, isoCountry, isoRegion,
                        muni, gps, iata, local, coords));
            }
            return out;
        }
    }

    private static int col(java.util.Map<String,Integer> idx, String name) { return idx.getOrDefault(name, -1); }
    private static String get(java.util.List<String> row, int i) { return (i>=0 && i<row.size()) ? row.get(i) : ""; }
    private static Integer parseInt(String s) {
        try { return (s==null || s.isBlank()) ? null : Integer.valueOf(s.trim()); }
        catch (Exception e) { return null; }
    }

    private static java.util.List<String> parseCsv(String line) {
        java.util.List<String> out = new java.util.ArrayList<>();
        if (line == null) return out;
        StringBuilder sb = new StringBuilder();
        boolean inQ = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQ) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') { sb.append('"'); i++; }
                    else { inQ = false; }
                } else sb.append(c);
            } else {
                if (c == ',') { out.add(sb.toString()); sb.setLength(0); }
                else if (c == '"') { inQ = true; }
                else sb.append(c);
            }
        }
        out.add(sb.toString());
        return out;
    }
}
