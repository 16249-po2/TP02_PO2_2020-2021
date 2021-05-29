package pt.ipbeja.estig.boulderdash.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class GetMap {

    private static final String FILE_PATH = "./src/resources/map.txt";
    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler(FILE_PATH);
            fileHandler.getMapData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] mapDimensions() {
        return fileHandler.getSize();
    }

    public static char[][] mapTopography(){
        return fileHandler.getTopography();
    }

    public static int[] playerStart() {
        return fileHandler.getPlayer();
    }

    public static int[] movingEnemyStart() {
        return fileHandler.getMovingEnemy();
    }

    public static int[] staticEnemyStart() {
        return fileHandler.getStaticEnemy();
    }

    public static int[] bonusStart() {
        return fileHandler.getBonus();
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

    public int[] getSize() {
        return Arrays.stream(mapData[0].split(" ")).mapToInt(Integer::parseInt).toArray();
    }

    public char[][] getTopography() {
        int[] mapSize = getSize();
        char[][] topography = new char[mapSize[0]][mapSize[1]];
        for (int i = 0; i < mapSize[0]; i++){
                topography[i] = mapData[i + 1].toCharArray();
        }
        return topography;
    }

    public int[] getPlayer() {
        int[] mapSize = getSize();
        String[] tempPlayer = mapData[mapSize[0] + 1].split(" ");
        int[] player = {Integer.parseInt(tempPlayer[1]), Integer.parseInt(tempPlayer[2])};
        return player;
    }

    public int[] getMovingEnemy() {
        int[] mapSize = getSize();
        String[] tempMovingEnemy = mapData[mapSize[0] + 3].split(" ");
        int[] movingEnemy = {Integer.parseInt(tempMovingEnemy[1]), Integer.parseInt(tempMovingEnemy[2])};
        return movingEnemy;
    }

    public int[] getStaticEnemy() {
        int[] mapSize = getSize();
        String[] tempStaticEnemy = mapData[mapSize[0] + 4].split(" ");
        int[] staticEnemy = {Integer.parseInt(tempStaticEnemy[1]), Integer.parseInt(tempStaticEnemy[2])};
        return staticEnemy;
    }

    public int[] getBonus() {
        int[] mapSize = getSize();
        String[] tempBonus = mapData[mapSize[0] + 5].split(" ");
        int[] bonus = {Integer.parseInt(tempBonus[1]), Integer.parseInt(tempBonus[2])};
        return bonus;
    }

    //TODO diamonds
    //diamonds will need another route to get there

}
