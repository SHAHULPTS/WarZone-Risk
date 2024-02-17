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

        try (BufferedReader l_reader = new BufferedReader(new FileReader(l_filePath)))
        {
            l_lineList = l_reader.lines().collect(Collectors.toList());
        } catch (IOException l_e1)
        {
            System.out.println("Unable to locate the file entered. Please try again");
        }
        return l_lineList;
    }
    public List<String> getMetaData(List<String> p_fileLines, String p_switchParameter) {
        switch (p_switchParameter) {
            case "Continent":
                List<String> l_continentLines = p_fileLines.subList(p_fileLines.indexOf(ApplicationConstants.CONTINENTS) + 1, p_fileLines.indexOf(ApplicationConstants.COUNTRIES) - 1);
                return l_continentLines;
            case "Country":
                List<String> l_countryLines = p_fileLines.subList(p_fileLines.indexOf(ApplicationConstants.COUNTRIES) + 1, p_fileLines.indexOf(ApplicationConstants.BORDERS) - 1);
                return l_countryLines;
            case "Border":
                List<String> l_bordersLines = p_fileLines.subList(p_fileLines.indexOf(ApplicationConstants.BORDERS) + 1, p_fileLines.size());
                return l_bordersLines;
            default:
                return null;
        }
    }
    public List<Continent> parseContinentsMetaData(List<String> p_continentList) {
        int l_continentId = 1;
        List<Continent> l_continents = new ArrayList<Continent>();
        for (String cont : p_continentList) {
            String[] l_metaData = cont.split(" ");
            l_continents.add(new Continent(l_continentId, l_metaData[0], Integer.parseInt(l_metaData[1])));
            l_continentId++;
        }
        return l_continents;
    }
}
