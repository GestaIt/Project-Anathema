package com.company;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class LocalFileReader {
    private final BufferedReader bufferedReader;

    public LocalFileReader(String fileDirectory) throws FileNotFoundException {
        String projectDirectory = System.getProperty("user.dir");
        this.bufferedReader = new BufferedReader(new java.io.FileReader(projectDirectory + fileDirectory));
    }

    public ArrayList<String> readLines() throws IOException {
        ArrayList<String> result = new ArrayList<>();

        try {
            String currentLine = bufferedReader.readLine();

            while (currentLine != null) {
                result.add(currentLine);
                currentLine = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bufferedReader.close();
        }

        return result;
    }

    public BufferedReader getBufferedReader() {
        return this.bufferedReader;
    }
}