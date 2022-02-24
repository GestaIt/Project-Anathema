package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class LocalFileReader {
    private final String projectDirectory = System.getProperty("user.dir");
    private final String fileDirectory;

    public LocalFileReader(String fileDirectory) {
        this.fileDirectory = fileDirectory;
    }

    public ArrayList<String> readLines() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new java.io.FileReader(this.projectDirectory +
                this.fileDirectory));
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
}