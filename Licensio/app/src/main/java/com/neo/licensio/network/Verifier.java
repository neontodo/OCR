package com.neo.licensio.network;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Verifier {
    public static final String CSV_FILE_PATH = "all_countries.csv";

    public static Map<String, String> readCsvFile() {
        Map<String, String> countryMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                String id = data[0].trim();
                String country = data[1].trim();
                countryMap.put(id, country);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return countryMap;
    }
}
