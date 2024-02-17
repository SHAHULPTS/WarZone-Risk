package Services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import Constants.ApplicationConstants;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;


public class MapService {


    public Map loadMap(GameState p_gameState, String p_loadFileName) {
        Map l_map = new Map();
        List<String> l_linesOfFile = loadFile(p_loadFileName);

        if (null != l_linesOfFile && !l_linesOfFile.isEmpty()) {
            List<String> l_continentData = getMetaData(l_linesOfFile, "Continent");
            List<Continent> l_continentObjects = parseContinentsMetaData(l_continentData);
            List<String> l_countryData = getMetaData(l_linesOfFile, "Country");
            List<String> l_bordersMetaData = getMetaData(l_linesOfFile, "Border");
            List<Country> l_countryObjects = parseCountriesMetaData(l_countryData);

            l_countryObjects = parseBorderMetaData(l_countryObjects, l_bordersMetaData);
            l_continentObjects = linkCountryContinents(l_countryObjects, l_continentObjects);

            l_map.setD_continents(l_continentObjects);
            l_map.setD_countries(l_countryObjects);
            p_gameState.setD_map(l_map);
        }
        return l_map;
    }
    public List<String> loadFile(String p_loadFileName) {
        String l_filePath = CommonUtil.getMapFilePath(p_loadFileName);
        List<String> l_lineList = new ArrayList<>();

        try (BufferedReader l_reader = new BufferedReader(new FileReader(l_filePath))) {
            l_lineList = l_reader.lines().collect(Collectors.toList());
        } catch (IOException l_e1) {
            System.out.println("File not Found!");
        }
        return l_lineList;
    }

}
