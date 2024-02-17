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
    public List<Country> parseCountriesMetaData(List<String> p_countriesList) {

        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();
        List<Country> l_countriesList = new ArrayList<Country>();
        for (String country : p_countriesList) {
            String[] l_metaDataCountries = country.split(" ");
            l_countriesList.add(new Country(Integer.parseInt(l_metaDataCountries[0]), l_metaDataCountries[1], Integer.parseInt(l_metaDataCountries[2])));
        }
        return l_countriesList;
    }
    public List<Country> parseBorderMetaData(List<Country> p_countriesList, List<String> p_bordersList) {
        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();

        for (String l_border : p_bordersList) {
            if (null != l_border && !l_border.isEmpty()) {
                ArrayList<Integer> l_neighbours = new ArrayList<Integer>();
                String[] l_splitString = l_border.split(" ");
                for (int i = 1; i <= l_splitString.length - 1; i++)
                {
                    l_neighbours.add(Integer.parseInt(l_splitString[i]));

                }
                l_countryNeighbors.put(Integer.parseInt(l_splitString[0]), l_neighbours);
            }
        }
        for (Country c : p_countriesList) {
            List<Integer> l_adjacentCountries = l_countryNeighbors.get(c.getD_countryId());
            c.setD_adjacentCountryIds(l_adjacentCountries);
        }
        return p_countriesList;
    }
    public List<Continent> linkCountryContinents(List<Country> p_countries, List<Continent> p_continents) {
        for (Country c : p_countries) {
            for (Continent cont : p_continents) {
                if (cont.getD_continentID().equals(c.getD_continentId())) {
                    cont.addCountry(c);
                }
            }
        }
        return p_continents;
    }
    public void editMap(GameState p_gameState, String p_editFilePath) throws IOException
    {

        String l_filePath = CommonUtil.getMapFilePath(p_editFilePath);
        File l_fileToBeEdited = new File(l_filePath);

        if (l_fileToBeEdited.createNewFile())
        {
            System.out.println("The file has been successfully generated.");
            Map l_map = new Map();
            l_map.setD_mapFile(p_editFilePath);
            p_gameState.setD_map(l_map);
        }
        else
        {
            System.out.println("File has already been generated");
            this.loadMap(p_gameState, p_editFilePath);
            if (null == p_gameState.getD_map())
            {
                p_gameState.setD_map(new Map());
            }
            p_gameState.getD_map().setD_mapFile(p_editFilePath);
        }


    }

    public void editContinent(GameState p_gameState, String p_argument, String p_operation) throws IOException, InvalidMap
    {

        String l_mapFileName = p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents()) && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName) : p_gameState.getD_map();

        if(!CommonUtil.isNull(l_mapToBeUpdated))
        {
            Map l_updatedMap = addRemoveContinents(l_mapToBeUpdated, p_operation, p_argument);
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }

    }

    public Map addRemoveContinents(Map p_mapToBeUpdated, String p_operation, String p_argument) throws InvalidMap {

        if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2) {
            p_mapToBeUpdated.addContinent(p_argument.split(" ")[0], Integer.parseInt(p_argument.split(" ")[1]));
        } else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==1) {
            p_mapToBeUpdated.removeContinent(p_argument.split(" ")[0]);
        } else {
            System.out.println("The attempt to add/remove the continent was unsuccessful.The system remains unchanged.");
        }

        return p_mapToBeUpdated;

    }
    public void editCountry(GameState p_gameState, String p_operation, String p_argument) throws InvalidMap
    {
        String l_mapFileName= p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents()) && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName) : p_gameState.getD_map();

        if(!CommonUtil.isNull(l_mapToBeUpdated))
        {
            Map l_updatedMap = addRemoveCountry(l_mapToBeUpdated, p_operation, p_argument);
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }
    public Map addRemoveCountry(Map p_mapToBeUpdated, String p_operation, String p_argument) throws InvalidMap
    {
        if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2)
        {
            p_mapToBeUpdated.addCountry(p_argument.split(" ")[0], p_argument.split(" ")[1]);
        }else if(p_operation.equalsIgnoreCase("remove")&& p_argument.split(" ").length==1)
        {
            p_mapToBeUpdated.removeCountry(p_argument.split(" ")[0]);
        }
        else
        {
            System.out.println("Your changes could not be saved.");
        }
        return p_mapToBeUpdated;
    }
    public void editNeighbour(GameState p_gameState, String p_operation, String p_argument) throws InvalidMap
    {
        String l_mapFileName= p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents()) && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName) : p_gameState.getD_map();

        if(!CommonUtil.isNull(l_mapToBeUpdated))
        {
            Map l_updatedMap = addRemoveNeighbour(l_mapToBeUpdated, p_operation, p_argument);
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }
}
