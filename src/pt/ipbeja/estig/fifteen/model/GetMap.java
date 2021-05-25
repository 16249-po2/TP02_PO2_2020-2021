package pt.ipbeja.estig.fifteen.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class GetMap {

    private static final String FILE_PATH = "./src/resources/map.txt";
    public static void GetMap() throws IOException {
        FileHandler fileHandler = new FileHandler(FILE_PATH);
        //System.out.println("Map size: " + fileHandler.getMapData());
        System.out.println("Map size: " + fileHandler.getSize());
    }
}

class FileHandler {
    static BufferedReader reader = null;
    static String[] mapData = new String[50];

    public FileHandler(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        reader = new BufferedReader(input);
    }

    public static String[] getMapData() throws IOException {
        int iter = 0;
        while((mapData[iter] = reader.readLine()) != null) {
            if (mapData[iter].contains(" %")){
                String tempPart[] = mapData[iter].split(" %");
                mapData[iter] = tempPart[0];
            }
            iter++;
        }

        //Array cleanup solution as per https://stackoverflow.com/a/4150305
        mapData = Arrays.stream(mapData)
                .filter(s -> (s != null && s.length() > 0))
                .toArray(String[]::new);

        return mapData;
    }

    public String getSize() {
        try {
            getMapData();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapData[0];
    }
}

//CURRENT OBJECTIVE MAKE THIS DATA AVAILABLE ELSEWHERE