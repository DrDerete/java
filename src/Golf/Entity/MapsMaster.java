package Golf.Entity;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.List;


public class MapsMaster {
    // Хранения загруженных карт
    private static final Map<String, GolfMap> MAP_CACHE = new HashMap<>();
    // Путь к папке с картами
    private static final String MAPS_FOLDER = "src/Golf/maps/";

    public static GolfMap loadMap(String mapName) throws IOException {
        if (isMapCached(mapName)) {
            return MAP_CACHE.get(mapName);
        }

        Path filePath = Paths.get(MAPS_FOLDER + mapName + ".txt");
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        // Проверка на пустую карту
        if (lines.isEmpty()) {
            throw new IOException("Карта '" + mapName + "' пуста");
        }

        int rows = lines.size();
        int cols = lines.getFirst().length();
        int[][] grid = new int[rows][cols];
        int wX = 0, wY = 0;

        for (int i = 0; i < rows; i++) {
            String currentLine = lines.get(i);
            if (currentLine.length() != cols) {
                throw new IOException("Несоответствие длины строк в карте '" + mapName + "!!!");
            }
            for (int j = 0; j < cols; j++) {
                grid[i][j] = Character.getNumericValue(currentLine.charAt(j));
                if (currentLine.charAt(j) == '3') {
                    wX = i;
                    wY = j;
                }
            }
        }
        GolfMap golf_map = new GolfMap(mapName, grid, wX, wY);

        // Сохраняем в кэш
        MAP_CACHE.put(mapName, golf_map);
        return golf_map;
    }


//    реализовать генерацию карт
//    FastNoiseLite
//    и Polygon методы

    public static void clearCache() {
        MAP_CACHE.clear();
    }

    public static boolean isMapCached(String mapName) {
        return MAP_CACHE.containsKey(mapName);
    }
}
