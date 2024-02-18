package Services;

import java.io.BufferedReader;
import java.io.File;
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
    /**
 * Loads a map into the game state from a file.
 *
 * @param p_gameState     The current game state.
 * @param p_loadFileName  The name of the file to load the map from.
 * @return                The loaded map.
 */
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

    /**
     * Loads the content of a file into a list of strings.
     *
     * @param p_loadFileName The name of the file to be loaded.
     * @return A list of strings containing the lines of the file, or an empty list if the file couldn't be read.
     */
    public List<String> loadFile(String p_loadFileName) {
        String l_filePath = CommonUtil.getMapFilePath(p_loadFileName);
        List<String> l_lineList = new ArrayList<>();

        try (BufferedReader l_reader = new BufferedReader(new FileReader(l_filePath))) {
            l_lineList = l_reader.lines().collect(Collectors.toList());
        } catch (IOException l_e1) {
            System.out.println("Unable to locate the file entered. Please try again");
        }
        return l_lineList;
    }

    /**
     * Edits the map file associated with the game state.
     * If the file doesn't exist, it creates a new file.
     * If the file already exists, it loads the existing map from the file.
     *
     * @param p_gameState    The current game state.
     * @param p_editFilePath The file path for the map to be edited or created.
     * @throws IOException   If an I/O error occurs when creating the file.
     */
    public void editMap(GameState p_gameState, String p_editFilePath) throws IOException {

        String l_filePath = CommonUtil.getMapFilePath(p_editFilePath);
        File l_fileToBeEdited = new File(l_filePath);

        if (l_fileToBeEdited.createNewFile()) {
            System.out.println("The file has been successfully generated.");
            Map l_map = new Map();
            l_map.setD_mapFile(p_editFilePath);
            p_gameState.setD_map(l_map);
        } else {
            System.out.println("File has already been generated");
            this.loadMap(p_gameState, p_editFilePath);
            if (null == p_gameState.getD_map()) {
                p_gameState.setD_map(new Map());
            }
            p_gameState.getD_map().setD_mapFile(p_editFilePath);
        }


    }

    /**
     * Saves the current map state to a file with the specified filename.
     *
     * @param p_gameState The current game state containing the map to be saved.
     * @param p_fileName  The filename for the map file to be saved.
     * @return            True if the map was successfully saved, false otherwise.
     * @throws InvalidMap If the map is found to be invalid during the save process.
     */
    public boolean saveMap(GameState p_gameState, String p_fileName) throws InvalidMap {
        try {
            if (!p_fileName.equalsIgnoreCase(p_gameState.getD_map().getD_mapFile())) {
                p_gameState.setError("Please ensure the filename used for save matches the one used for edit");
                return false;
            } else {
                if (null != p_gameState.getD_map()) {
                    Models.Map l_currentMap = p_gameState.getD_map();
                    System.out.println("Authenticating Map......");
                    boolean l_mapValidationStatus = l_currentMap.Validate();
                    if (l_mapValidationStatus) {
                        Files.deleteIfExists(Paths.get(CommonUtil.getMapFilePath(p_fileName)));
                        FileWriter l_writer = new FileWriter(CommonUtil.getMapFilePath(p_fileName));

                        if (null != p_gameState.getD_map().getD_continents() && !p_gameState.getD_map().getD_continents().isEmpty()) {
                            writeContinentMetadata(p_gameState, l_writer);
                        }
                        if (null != p_gameState.getD_map().getD_countries() && !p_gameState.getD_map().getD_countries().isEmpty()) {
                            writeCountryAndBoarderMetaData(p_gameState, l_writer);
                        }
                        l_writer.close();
                    }
                } else {
                    p_gameState.setError("Failed to Authenticate the Map");
                    return false;
                }
            }
            return true;

        } catch (IOException l_e) {
            l_e.printStackTrace();
            p_gameState.setError("There was a problem saving the map file.");
            return false;
        }
    }

    /**
     * Extracts metadata lines from the given list of file lines based on the provided switch parameter.
     *
     * @param p_fileLines       The list of lines from the file.
     * @param p_switchParameter The parameter indicating which type of metadata to extract ("Continent", "Country", or "Border").
     * @return                  A list of strings containing the metadata lines corresponding to the specified parameter,
     *                          or null if the parameter is invalid.
     */
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

    /**
     * Writes continent metadata to the provided FileWriter.
     *
     * @param p_gameState The current game state containing the map and its continents.
     * @param p_writer    The FileWriter object to write continent metadata to.
     * @throws IOException If an I/O error occurs while writing to the FileWriter.
     */
    private void writeContinentMetadata(GameState p_gameState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + ApplicationConstants.CONTINENTS + System.lineSeparator());
        for (Continent l_continent : p_gameState.getD_map().getD_continents()) {
            p_writer.write(l_continent.getD_continentName().concat(" ").concat(l_continent.getD_continentValue().toString()) + System.lineSeparator());
        }
    }

    /**
     * Parses the provided list of continent metadata strings into a list of Continent objects.
     *
     * @param p_continentList The list of strings containing continent metadata.
     * @return                A list of Continent objects parsed from the continent metadata strings.
     */
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

    /**
     * Parses the provided list of country metadata strings into a list of Country objects.
     *
     * @param p_countriesList The list of strings containing country metadata.
     * @return                 A list of Country objects parsed from the country metadata strings.
     */
    public List<Country> parseCountriesMetaData(List<String> p_countriesList) {

        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();
        List<Country> l_countriesList = new ArrayList<Country>();
        for (String country : p_countriesList) {
            String[] l_metaDataCountries = country.split(" ");
            l_countriesList.add(new Country(Integer.parseInt(l_metaDataCountries[0]), l_metaDataCountries[1], Integer.parseInt(l_metaDataCountries[2])));
        }
        return l_countriesList;
    }

    /**
     * Parses the provided list of border metadata strings into adjacency information for countries.
     *
     * @param p_countriesList The list of countries to which the adjacency information will be applied.
     * @param p_bordersList   The list of strings containing border metadata, indicating which countries are adjacent to each other.
     * @return                The list of countries with updated adjacency information based on the border metadata.
     */
    public List<Country> parseBorderMetaData(List<Country> p_countriesList, List<String> p_bordersList) {
        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();

        for (String l_border : p_bordersList) {
            if (null != l_border && !l_border.isEmpty()) {
                ArrayList<Integer> l_neighbours = new ArrayList<Integer>();
                String[] l_splitString = l_border.split(" ");
                for (int i = 1; i <= l_splitString.length - 1; i++) {
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


    /**
     * Edits the continents of the map according to the specified operation and argument.
     *
     * @param p_gameState The current game state.
     * @param p_argument  The argument specifying the continent to be added or removed.
     * @param p_operation The operation to perform: "add" to add a continent, "remove" to remove a continent.
     * @throws IOException  If an I/O error occurs while loading the map.
     * @throws InvalidMap   If the edited map is found to be invalid.
     */
    public void editContinent(GameState p_gameState, String p_argument, String p_operation) throws IOException, InvalidMap {

        String l_mapFileName = p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents()) && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName) : p_gameState.getD_map();

        if (!CommonUtil.isNull(l_mapToBeUpdated)) {
            Map l_updatedMap = addRemoveContinents(l_mapToBeUpdated, p_operation, p_argument);
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }

    }
    /**
     * Adds or removes a continent from the provided map based on the specified operation and argument.
     *
     * @param p_mapToBeUpdated The map to be updated.
     * @param p_operation       The operation to perform: "add" to add a continent, "remove" to remove a continent.
     * @param p_argument        The argument specifying the continent to be added or removed, along with its value (for "add").
     *                          For "add" operation: "<continent_name> <continent_value>"
     *                          For "remove" operation: "<continent_name>"
     * @return                  The updated map after performing the add or remove operation on the continent.
     * @throws InvalidMap       If the map becomes invalid after performing the operation.
     */
    public Map addRemoveContinents(Map p_mapToBeUpdated, String p_operation, String p_argument) throws InvalidMap {

        if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length == 2) {
            p_mapToBeUpdated.createContinent(p_argument.split(" ")[0], Integer.parseInt(p_argument.split(" ")[1]));
        } else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length == 1) {
            p_mapToBeUpdated.deleteContinent(p_argument.split(" ")[0]);
        } else {
            System.out.println("The attempt to add/remove the continent was unsuccessful.The system remains unchanged.");
        }

        return p_mapToBeUpdated;

    }
    /**
     * Edits the countries of the map according to the specified operation and argument.
     *
     * @param p_gameState The current game state.
     * @param p_operation The operation to perform: "add" to add a country, "remove" to remove a country.
     * @param p_argument  The argument specifying the country to be added or removed.
     * @throws InvalidMap If the edited map is found to be invalid.
     */
    public void editCountry(GameState p_gameState, String p_operation, String p_argument) throws InvalidMap {
        String l_mapFileName = p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents()) && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName) : p_gameState.getD_map();

        if (!CommonUtil.isNull(l_mapToBeUpdated)) {
            Map l_updatedMap = addRemoveCountry(l_mapToBeUpdated, p_operation, p_argument);
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }
    /**
     * Adds or removes a country from the provided map based on the specified operation and argument.
     *
     * @param p_mapToBeUpdated The map to be updated.
     * @param p_operation       The operation to perform: "add" to add a country, "remove" to remove a country.
     * @param p_argument        The argument specifying the country to be added or removed.
     *                          For "add" operation: "<country_name> <continent_name>"
     *                          For "remove" operation: "<country_name>"
     * @return                  The updated map after performing the add or remove operation on the country.
     * @throws InvalidMap       If the map becomes invalid after performing the operation.
     */
    public Map addRemoveCountry(Map p_mapToBeUpdated, String p_operation, String p_argument) throws InvalidMap {
        if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length == 2) {
            p_mapToBeUpdated.createCountry(p_argument.split(" ")[0], p_argument.split(" ")[1]);
        } else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length == 1) {
            p_mapToBeUpdated.deleteCountry(p_argument.split(" ")[0]);
        } else {
            System.out.println("Your changes could not be saved.");
        }
        return p_mapToBeUpdated;
    }
    /**
     * Edits the neighbors of a country in the map according to the specified operation and argument.
     *
     * @param p_gameState The current game state.
     * @param p_operation The operation to perform: "add" to add a neighbor, "remove" to remove a neighbor.
     * @param p_argument  The argument specifying the neighbors to be added or removed.
     *                    For "add" operation: "<country_name> <neighbor1> <neighbor2> ..."
     *                    For "remove" operation: "<country_name> <neighbor1> <neighbor2> ..."
     * @throws InvalidMap If the edited map is found to be invalid.
     */
    public void editNeighbour(GameState p_gameState, String p_operation, String p_argument) throws InvalidMap {
        String l_mapFileName = p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents()) && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName) : p_gameState.getD_map();

        if (!CommonUtil.isNull(l_mapToBeUpdated)) {
            Map l_updatedMap = addRemoveNeighbour(l_mapToBeUpdated, p_operation, p_argument);
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }


    }
    /**
     * Adds or removes a neighbor from a country in the provided map based on the specified operation and argument.
     *
     * @param p_mapToBeUpdated The map to be updated.
     * @param p_operation       The operation to perform: "add" to add a neighbor, "remove" to remove a neighbor.
     * @param p_argument        The argument specifying the neighbor to be added or removed, along with the country.
     *                          For "add" operation: "<country_name> <neighbor_name>"
     *                          For "remove" operation: "<country_name> <neighbor_name>"
     * @return                  The updated map after performing the add or remove operation on the neighbor.
     * @throws InvalidMap       If the map becomes invalid after performing the operation.
     */
    public Map addRemoveNeighbour(Map p_mapToBeUpdated, String p_operation, String p_argument) throws InvalidMap {
        if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length == 2) {
            p_mapToBeUpdated.addCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
        } else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length == 2) {
            p_mapToBeUpdated.deleteCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
        } else {
            System.out.println("Your changes could not be saved.");
        }
        return p_mapToBeUpdated;
    }


    /**
     * Writes country and border metadata to the provided FileWriter.
     *
     * @param p_gameState The current game state containing the map and its countries.
     * @param p_writer    The FileWriter object to write country and border metadata to.
     * @throws IOException If an I/O error occurs while writing to the FileWriter.
     */
    private void writeCountryAndBoarderMetaData(GameState p_gameState, FileWriter p_writer) throws IOException {
        String l_countryMetaData = new String();
        String l_bordersMetaData = new String();
        List<String> l_bordersList = new ArrayList<>();

        p_writer.write(System.lineSeparator() + ApplicationConstants.COUNTRIES + System.lineSeparator());
        for (Country l_country : p_gameState.getD_map().getD_countries()) {
            l_countryMetaData = new String();
            l_countryMetaData = l_country.getD_countryId().toString().concat(" ").concat(l_country.getD_countryName()).concat(" ").concat(l_country.getD_continentId().toString());
            p_writer.write(l_countryMetaData + System.lineSeparator());

            if (null != l_country.getD_adjacentCountryIds() && !l_country.getD_adjacentCountryIds().isEmpty()) {
                l_bordersMetaData = new String();
                l_bordersMetaData = l_country.getD_countryId().toString();
                for (Integer l_adjCountry : l_country.getD_adjacentCountryIds()) {
                    l_bordersMetaData = l_bordersMetaData.concat(" ").concat(l_adjCountry.toString());
                }
                l_bordersList.add(l_bordersMetaData);
            }
        }
        if (null != l_bordersList && !l_bordersList.isEmpty()) {
            p_writer.write(System.lineSeparator() + ApplicationConstants.BORDERS + System.lineSeparator());
            for (String l_borderStr : l_bordersList) {
                p_writer.write(l_borderStr + System.lineSeparator());
            }
        }
    }
    /**
     * Links countries to their respective continents by updating the continent objects with country references.
     *
     * @param p_countries  The list of countries to be linked to continents.
     * @param p_continents The list of continents to which countries will be linked.
     * @return             The updated list of continents with linked countries.
     */
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
    /**
     * Resets the map in the game state to an empty map.
     *
     * @param p_gameState The game state whose map will be reset.
     */
    public void resetMap(GameState p_gameState) {
        System.out.println("The system failed to load the map because it is invalid. We kindly request a valid map.");
        p_gameState.setD_map(new Models.Map());
    }
}


