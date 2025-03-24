package com.example.springserver.utils;

import com.example.springserver.models.Medicine;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MedicineCSVReader {

    /*private Medicine createMedicineFromData(String[] data, List<Integer> sideEffectIndices) {
        Medicine medicine = new Medicine();
        medicine.setId(data[0]);
        medicine.setName(data[1]);

        // Combine the side effects from the specified columns
        List<String> sideEffects = new ArrayList<>();
        for (Integer index : sideEffectIndices) {
            if (index < data.length) {
                String sideEffect = data[index];
                sideEffects.add(sideEffect);
            }
        }
        medicine.setSideEffects(sideEffects);

        return medicine;
    }

    //gets the indices (number) of the columns named "sideEffectx"
    private List<Integer> findSideEffectIndices(String[] headers) {
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].startsWith("sideEffect")) {
                indices.add(i);
            }
        }
        return indices;
    }

    public Medicine getMedicineByName(String csvFile, String name) throws FileNotFoundException {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> records = reader.readAll();

            // Assuming the first row contains headers
            String[] headers = records.get(0);

            // Find the index of the name column
            int nameIndex = findNameIndex(headers);

            // Find the indices of the side effect columns
            List<Integer> sideEffectIndices = findSideEffectIndices(headers);

            // Start from the second row to skip the headers
            for (int i = 1; i < records.size(); i++) {
                String[] data = records.get(i);

                // Check if the name matches
                if (data.length > nameIndex && data[nameIndex].equalsIgnoreCase(name)) {
                    return createMedicineFromData(data, sideEffectIndices);
                }
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }

        return null; // Medicine not found
    }

    private int findNameIndex(String[] headers) {
        for (int i = 0; i < headers.length; i++) {
            if (headers[i].equalsIgnoreCase("name")) {
                return i;
            }
        }
        return -1; // Name column not found
    }

    public List<Medicine> getMedicineByPartialName(String csvFile, String partialName) throws FileNotFoundException {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> records = reader.readAll();

            // Assuming the first row contains headers
            String[] headers = records.get(0);

            // Find the index of the name column
            int nameIndex = findNameIndex(headers);

            // Find the indices of the side effect columns
            List<Integer> sideEffectIndices = findSideEffectIndices(headers);
            List<Medicine> filteredMedicine = new ArrayList<>();

            // Start from the second row to skip the headers
            for (int i = 1; i < records.size(); i++) {
                String[] data = records.get(i);

                // Check if the name matches
                if (data.length > nameIndex && data[nameIndex].contains(partialName)) {
                    //return createMedicineFromData(data, sideEffectIndices);
                    filteredMedicine.add(createMedicineFromData(data, sideEffectIndices));
                }
                if(filteredMedicine.size()>=3){
                    break;
                }
            }
            if(filteredMedicine.size()>=1) {
                return filteredMedicine;
            }
        } catch (IOException | CsvException e) {
            throw new RuntimeException(e);
        }
        return null;
    }*/
}
