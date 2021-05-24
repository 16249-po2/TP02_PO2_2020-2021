package pt.ipbeja.estig.fifteen.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class GetMap {

    private static final String FILE_PATH = "./src/resources/map.txt";
    public static void GetMap() throws IOException {
        FileHandler fileHandler = new FileHandler(FILE_PATH);
        System.out.println("No. of characters in file: " + fileHandler.getCharCount());
    }
}

class FileHandler {
    static BufferedReader reader = null;

    public FileHandler(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        reader = new BufferedReader(input);
    }

    public static int getCharCount() throws IOException {
        int charCount = 0;
        String data;
        while((data = reader.readLine()) != null) {
            charCount += data.length();
        }
        return charCount;
    }
}