package main.java.playtech;

import main.java.playtech.service.DataProcessor;

public class Main {
    public static void main(String[] args) {
        DataProcessor dataProcessor = new DataProcessor();
        dataProcessor.processDataAndGenerateResult();
    }
}
