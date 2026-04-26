package com.sms.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InputReader {

    private static InputReader instance;
    private BufferedReader reader;

    
    private InputReader() {
        try {
            reader = new BufferedReader(
                    new FileReader("src/main/resources/Input.txt"));
        } catch (IOException e) {
            throw new RuntimeException("File not found", e);
        }
    }

    
    public static InputReader get() {
        if (instance == null) {
            synchronized (InputReader.class) {
                if (instance == null) {
                    instance = new InputReader();
                }
            }
        }
        return instance;
    }

    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException("Error reading file", e);
        }
    }
}