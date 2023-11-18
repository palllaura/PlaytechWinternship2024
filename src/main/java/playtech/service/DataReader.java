package main.java.playtech.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataReader {
    private static final String PLAYER_DATA_FILE = "src/main/resources/player_data.txt";
    private static final String MATCH_DATA_FILE = "src/main/resources/match_data.txt";

    public List<String[]> readPlayerData() {
        return getData(PLAYER_DATA_FILE);
    }

    public List<String[]> readMatchData() {
        return getData(MATCH_DATA_FILE);
    }

    private List<String[]> getData(String dataFile) {
        List<String[]> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] row = line.split(",");
                data.add(row);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
