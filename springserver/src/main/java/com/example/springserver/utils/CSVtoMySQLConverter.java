package com.example.springserver.utils;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CSVtoMySQLConverter {
    public static void main(String[] args) {
        String csvFile = CSVtoMySQLConverter.class.getClassLoader().getResource("medicine_dataset.csv").getPath();
        String jdbcUrl = "jdbc:mysql://localhost:3306/licensio";
        String username = "root";
        String password = "Mightybeanz.123";

        try (CSVReader reader = new CSVReader(new FileReader(csvFile));
             Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {

            String[] headers = reader.readNext(); // Read the header row

            // Prepare the INSERT statement
            String insertQuery = "INSERT INTO medicine (id, name, substitute0, substitute1, substitute2, substitute3, substitute4, " +
                    "sideEffect0, sideEffect1, sideEffect2, sideEffect3, sideEffect4, sideEffect5, sideEffect6, sideEffect7, sideEffect8, sideEffect9" +
                    ", sideEffect10, sideEffect11, sideEffect12, sideEffect13, sideEffect14, sideEffect15, sideEffect16, sideEffect17, sideEffect18, sideEffect19, sideEffect20," +
                    "sideEffect21, sideEffect22, sideEffect23, sideEffect24, sideEffect25, sideEffect26, sideEffect27, sideEffect28, sideEffect29, sideEffect30, sideEffect31," +
                    "sideEffect32, sideEffect33, sideEffect34, sideEffect35, sideEffect36, sideEffect37, sideEffect38, sideEffect39, sideEffect40, sideEffect41, " +
                    "use0, use1, use2, use3, use4, `Chemical Class`, `Habit Forming`, `Therapeutic Class`, `Action Class`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, " +
                    "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);

            String[] row;
            while ((row = reader.readNext()) != null) {
                // Set values for each column in the prepared statement
                for (int i = 0; i < headers.length; i++) {
                    preparedStatement.setString(i + 1, row[i]);
                }

                // Execute the INSERT statement
                preparedStatement.executeUpdate();
            }

            System.out.println("CSV data imported into MySQL successfully.");
        } catch (IOException | SQLException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}
