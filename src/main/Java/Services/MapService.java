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
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;


public class MapService {
    /**
     * Loads a map from a specified file, parsing continent, country, and border data to create a complete
     * map structure. This method reads the file line by line to extract metadata related to continents,
     * countries, and their borders. It then parses this data to construct a list of Continent and Country
     * objects, establishing the relationships between them, including which countries belong to which
     * continent and the neighboring countries for each country. The constructed map is then set as the
     * current map in the provided game state.
     *
     * @param p_gameState The game state into which the loaded map will be set. This allows the method
     *                    to directly update the game state with the new map, making it immediately
     *                    available for game play or further operations.
     * @param p_loadFileName The name of the file from which to load the map. This should be a path or
     *                       filename that the {@link #loadFile(String)} method can use to locate and
     *                       read the file.
     * @return The loaded and fully constructed Map object, containing all continents, countries, and
     *         border relationships defined in the file.
     * @throws InvalidMap If any errors occur during the file reading process, or if the data in the file
     *                    is not correctly formatted for map creation. This exception is meant to indicate
     *                    that the operation failed due to issues specifically related to map data handling.
     */

    public Map loadMap(GameState p_gameState, String p_loadFileName) throws InvalidMap
    {
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
     * Loads the contents of a specified map file into a list of strings, where each string represents
     * one line from the file. This method is designed to facilitate reading map data from a file for
     * further processing or validation in the context of a game's map editing or loading functionality.
     *
     * @param p_loadFileName The name of the file to be loaded. The method constructs the full file path
     *                       using a common utility method, assuming a standard directory structure or
     *                       file location strategy.
     * @return A list of strings, each containing the contents of one line from the file, in order. This
     *         list is intended for use in further map parsing or validation steps.
     * @throws InvalidMap If the file cannot be found, is inaccessible, or any other IOException occurs
     *                    during the reading process, encapsulated as an InvalidMap exception to indicate
     *                    issues related specifically to map file handling.
     */

    public List<String> loadFile(String p_loadFileName) throws InvalidMap{

        String l_filePath = CommonUtil.getMapFilePath(p_loadFileName);
        List<String> l_lineList = new ArrayList<>();

        BufferedReader l_reader;
        try {
            l_reader = new BufferedReader(new FileReader(l_filePath));
            l_lineList = l_reader.lines().collect(Collectors.toList());
            l_reader.close();
        } catch (IOException l_e1) {
            throw new InvalidMap("The Map File name entered does not exist!");
        }
        return l_lineList;
    }

    /**
     * Prepares a map for editing by attempting to create a new map file if it does not exist or loading
     * an existing map file for editing. This method checks if the specified file exists. If not, it creates
     * a new file and initializes a new map in the game state with the given file path. If the file already
     * exists, it attempts to load the map from this file into the game state for editing.
     *
     * The method logs the outcome of the operation (file creation or existing file loaded) in the game state,
     * providing feedback for the user and for auditing purposes. This is intended to facilitate the map editing
     * process in a game where maps can be created or modified by the user.
     *
     * @param p_gameState The current game state, which will be updated with the new or loaded map and where
     *                    the log entry about the map editing preparation will be recorded.
     * @param p_editFilePath The path to the file to be edited. This file will be created if it does not exist,
     *                       or it will be loaded for editing if it already exists.
     * @throws IOException If an I/O error occurs during the file operation, such as failing to create a new
     *                     file or problems accessing an existing file.
     * @throws InvalidMap If the existing map file is found to be invalid during the loading process. This
     *                    exception is only thrown if the file exists and fails validation when being loaded.
     */

    public void editMap(GameState p_gameState, String p_editFilePath) throws IOException, InvalidMap {

        String l_filePath = CommonUtil.getMapFilePath(p_editFilePath);
        File l_fileToBeEdited = new File(l_filePath);

        if (l_fileToBeEdited.createNewFile()) {
            System.out.println("The File has been successfully created");
            Map l_map = new Map();
            l_map.setD_mapFile(p_editFilePath);
            p_gameState.setD_map(l_map);
            p_gameState.updateLog(p_editFilePath+ " The File has been successfully created for the user to edit", "effect");
        } else {
            System.out.println("File already exists.");
            this.loadMap(p_gameState, p_editFilePath);
            if (null == p_gameState.getD_map()) {
                p_gameState.setD_map(new Map());
            }
            p_gameState.getD_map().setD_mapFile(p_editFilePath);
            p_gameState.updateLog(p_editFilePath+ " The File already exists and is loaded for editing", "effect");
        }
    }

    /**
     * Attempts to save the current map to a file, validating the map and ensuring the file name used
     * for saving matches the one used for editing. This method first checks if the file name provided matches
     * the name of the file associated with the current map in the game state. If not, it sets an error in the
     * game state and returns false. If the file names match, it then validates the map. If the map is valid,
     * it deletes the existing file (if it exists) and writes the continent and country (including borders)
     * metadata to a new file with the specified name.
     *
     * If the map passes validation, the continents and countries metadata are written to the file, and the
     * method returns true, indicating the map was saved successfully. If any part of this process fails,
     * including validation failure, IO issues, or other errors, the method logs the error, updates the game
     * state with the failure, and returns false.
     *
     * @param p_gameState The current game state, containing the map to be saved and used for error logging.
     * @param p_fileName The name of the file to save the map to. This must match the file name used when the
     *                   map was loaded or last saved for edits.
     * @return True if the map is successfully saved, false if the file name does not match, the map fails
     *         validation, or any other error occurs during the save process.
     * @throws InvalidMap If the map is found to be invalid during validation.
     */

    public boolean saveMap(GameState p_gameState, String p_fileName) throws InvalidMap {
        boolean l_flagValidate = false;
        try {

            // Verifies if the file linked to savemap and edited by user are same
            if (!p_fileName.equalsIgnoreCase(p_gameState.getD_map().getD_mapFile())) {
                p_gameState.setError("Please use the same File name for saving that you used for edit");
                return false;
            } else {
                if (null != p_gameState.getD_map()) {
                    Models.Map l_currentMap = p_gameState.getD_map();

                    // Proceeds to save the map if it passes the validation check
                    this.setD_MapServiceLog("Authenticating Map......", p_gameState);
                    //boolean l_mapValidationStatus = l_currentMap.Validate();
                    if (l_currentMap.Validate()) {
                        Files.deleteIfExists(Paths.get(CommonUtil.getMapFilePath(p_fileName)));
                        FileWriter l_writer = new FileWriter(CommonUtil.getMapFilePath(p_fileName));

                        if (null != p_gameState.getD_map().getD_continents()
                                && !p_gameState.getD_map().getD_continents().isEmpty()) {
                            writeContinentMetadata(p_gameState, l_writer);
                        }
                        if (null != p_gameState.getD_map().getD_countries()
                                && !p_gameState.getD_map().getD_countries().isEmpty()) {
                            writeCountryAndBoarderMetaData(p_gameState, l_writer);
                        }
                        p_gameState.updateLog("Map File Saved Successfully", "effect");
                        l_writer.close();
                    }
                } else {
                    p_gameState.updateLog("Failed to Save the Map File due to Unsuccessful Authentication!", "effect");
                    p_gameState.setError("Authentication Failed!");
                    return false;
                }
            }
            return true;
        } catch (IOException | InvalidMap l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
            p_gameState.updateLog("Unable to make changes in the Map File!", "effect");
            p_gameState.setError("There was an issue while saving the Map File");
            return false;
        }
    }

    /**
     * Extracts and returns specific sections of metadata from a list of file lines, based on the specified
     * type of metadata to retrieve. This method can extract metadata related to continents, countries, or
     * borders, depending on the value of the switch parameter provided. It identifies the relevant section
     * in the file lines by looking for predefined application constants that mark the start of each section.
     *
     * @param p_fileLines A list of strings representing the lines of a file from which metadata needs to be
     *                    extracted. This list is expected to include sections marked by specific constants
     *                    indicating the start of continent, country, and border data.
     * @param p_switchParameter A string indicating the type of metadata to extract. Valid values are
     *                          "Continent", "Country", and "Border", corresponding to the different sections
     *                          of metadata that can be extracted.
     *
     * @return A list of strings containing the extracted metadata lines for the specified section. Returns
     *         `null` if the switch parameter does not match any known metadata type.
     */

    public List<String> getMetaData(List<String> p_fileLines, String p_switchParameter) {
        switch (p_switchParameter) {
            case "Continent":
                List<String> l_continentLines = p_fileLines.subList(
                        p_fileLines.indexOf(ApplicationConstants.CONTINENTS) + 1,
                        p_fileLines.indexOf(ApplicationConstants.COUNTRIES) - 1);
                return l_continentLines;
            case "Country":
                List<String> l_countryLines = p_fileLines.subList(p_fileLines.indexOf(ApplicationConstants.COUNTRIES) + 1,
                        p_fileLines.indexOf(ApplicationConstants.BORDERS) - 1);
                return l_countryLines;
            case "Border":
                List<String> l_bordersLines = p_fileLines.subList(p_fileLines.indexOf(ApplicationConstants.BORDERS) + 1,
                        p_fileLines.size());
                return l_bordersLines;
            default:
                return null;
        }
    }

    /**
     * Writes the metadata for continents to a specified FileWriter. This method iterates over the continents
     * in the game state's map, writing each continent's name and control value to the file. Each continent's
     * metadata is written on a new line, preceded by a line specifying the start of the continents section,
     * as indicated by an application constant.
     *
     * This structured approach ensures the file is organized into a clearly defined section for continents,
     * making it easier to read and process. The method is designed to append the continent metadata to the
     * file, assuming that the FileWriter is already open and will be managed (flushed or closed) externally.
     *
     * @param p_gameState The current game state containing the map data, including continents. This is the
     *                    source of the data for writing to the file.
     * @param p_writer    The FileWriter object to which the continent metadata will be written. This FileWriter
     *                    should be open when passed to this method and will need to be managed (flushed or
     *                    closed) by the caller after this method completes.
     * @throws IOException If an I/O error occurs while writing to the FileWriter. This could happen if the
     *                     FileWriter is not properly initialized, if there's a disk space issue, or if
     *                     another I/O issue occurs during writing.
     */

    private void writeContinentMetadata(GameState p_gameState, FileWriter p_writer) throws IOException {
        p_writer.write(System.lineSeparator() + ApplicationConstants.CONTINENTS + System.lineSeparator());
        for (Continent l_continent : p_gameState.getD_map().getD_continents()) {
            p_writer.write(
                    l_continent.getD_continentName().concat(" ").concat(l_continent.getD_continentValue().toString())
                            + System.lineSeparator());
        }
    }

    /**
     * Parses continent metadata from a list of strings to create and return a list of Continent objects.
     * Each string in the provided list is expected to contain the name of the continent and its control value,
     * separated by a space. This method iterates through each string, extracting the name and control value
     * to instantiate new Continent objects. A unique ID is automatically assigned to each continent in the
     * order they are processed.
     *
     * @param p_continentList A list of strings, where each string contains the metadata for a single continent
     *                        in the format "continentName controlValue". The continentName is a string, and
     *                        the controlValue, which represents the value of the continent in the game (e.g.,
     *                        for scoring purposes), is expected to be an integer.
     *
     * @return A list of Continent objects created from the parsed metadata. Each Continent object includes
     *         an auto-generated ID, the name, and the control value as derived from the input list.
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
     * Parses country metadata from a list of strings to create and return a list of Country objects.
     * Each string in the provided list is expected to contain country metadata, including a unique
     * country ID, the country name, and the continent ID to which the country belongs, all separated
     * by spaces. This method iterates through each string, splits it into its constituent parts, and
     * uses them to instantiate a new Country object with the parsed data.
     *
     * @param p_countriesList A list of strings, where each string contains the metadata for a single
     *                        country in the format "countryID countryName continentID". The countryID
     *                        and continentID are expected to be integers, while the countryName is a string.
     *
     * @return A list of Country objects created from the parsed metadata. Each Country object contains
     *         the ID, name, and continent ID as derived from the input list.
     */

    public List<Country> parseCountriesMetaData(List<String> p_countriesList) {

        LinkedHashMap<Integer, List<Integer>> l_countryNeighbors = new LinkedHashMap<Integer, List<Integer>>();
        List<Country> l_countriesList = new ArrayList<Country>();

        for (String country : p_countriesList) {
            String[] l_metaDataCountries = country.split(" ");
            l_countriesList.add(new Country(Integer.parseInt(l_metaDataCountries[0]), l_metaDataCountries[1],
                    Integer.parseInt(l_metaDataCountries[2])));
        }
        return l_countriesList;
    }

    /**
     * Parses border metadata to assign neighboring countries to each country in the provided list.
     * This method takes a list of countries and a list of border data, where each item in the border
     * list represents a country's ID followed by the IDs of its neighboring countries. It updates each
     * country in the provided list with its corresponding neighbors based on this border data.
     *
     * The method first processes the border data to create a mapping of each country to its neighbors.
     * Then, it iterates through the list of countries, using this mapping to set the neighbors for each
     * country. This approach ensures that each country object is updated with a list of its adjacent
     * countries' IDs, reflecting the geographical borders as defined by the input data.
     *
     * @param p_countriesList A list of Country objects that will be updated with their neighboring
     *                        countries' IDs. These objects represent the countries on the game map.
     * @param p_bordersList A list of strings, each containing a country's ID followed by the IDs of
     *                      its neighbors, separated by spaces. This list provides the data needed to
     *                      determine the neighbors for each country in the countries list.
     *
     * @return The updated list of Country objects, each now including a list of its neighbors' IDs.
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
     * Updates the game's map by adding or removing continents, countries, or neighbors. It first checks
     * if the map is already set up; if not, it loads the map. Then, based on the provided parameters,
     * it executes the requested operation (add or remove) on the specified part of the map (continent,
     * country, or neighbor).
     *
     * @param p_gameState The game state, which includes the current map and other game details. This is
     *                    where the map is retrieved from and updated.
     * @param p_argument The name of the element (continent, country, or neighbor) to be added or removed.
     * @param p_operation Specifies the operation to be performed, either "add" or "remove".
     * @param p_switchParameter Indicates the type of edit operation: 1 for continents, 2 for countries,
     *                          and 3 for neighbors.
     *
     * @throws IOException If there's an issue loading the map from a file.
     * @throws InvalidMap If the map file is not valid or can't be parsed.
     * @throws InvalidCommand If the operation or parameters are not valid.
     * @throws IllegalStateException If an unexpected or unknown action is requested.
     */
    public void editFunctions(GameState p_gameState, String p_argument, String p_operation, Integer p_switchParameter) throws IOException, InvalidMap, InvalidCommand {
        Map l_updatedMap;
        String l_mapFileName = p_gameState.getD_map().getD_mapFile();
        Map l_mapToBeUpdated = (CommonUtil.isNull(p_gameState.getD_map().getD_continents()) && CommonUtil.isNull(p_gameState.getD_map().getD_countries())) ? this.loadMap(p_gameState, l_mapFileName) : p_gameState.getD_map();

        // Edit Control Logic for Continent, Country & Neighbor
        if(!CommonUtil.isNull(l_mapToBeUpdated)){
            switch(p_switchParameter){
                case 1:
                    l_updatedMap = addRemoveContinents(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
                    break;
                case 2:
                    l_updatedMap = addRemoveCountry(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
                    break;
                case 3:
                    l_updatedMap = addRemoveNeighbour(p_gameState, l_mapToBeUpdated, p_operation, p_argument);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + p_switchParameter);
            }
            p_gameState.setD_map(l_updatedMap);
            p_gameState.getD_map().setD_mapFile(l_mapFileName);
        }
    }
    /**
     * Adds or removes a continent to/from the specified map in the game state based on the operation
     * specified. If adding, the continent name and control value are expected as arguments separated by a
     * space. If removing, only the continent name is expected.
     *
     * The operation is logged within the game state, indicating whether the addition or removal was
     * successful, or if an error occurred, detailing the issue.
     *
     * @param p_gameState The current state of the game, used for logging the outcome of the operation.
     * @param p_mapToBeUpdated The map object to which the continent will be added or from which it will
     *                         be removed.
     * @param p_operation The operation to perform: "add" to add a new continent or "remove" to remove an
     *                    existing continent.
     * @param p_argument The argument for the operation. For adding, this should be the continent name
     *                   followed by the control value, separated by a space. For removing, just the continent
     *                   name.
     *
     * @return The updated map object after performing the add or remove operation.
     * @throws InvalidMap If the operation or arguments are invalid, or if the continent cannot be
     *                    added or removed for some reason.
     */

    public Map addRemoveContinents(GameState p_gameState, Map p_mapToBeUpdated, String p_operation, String p_argument) throws InvalidMap {

        try {
            if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2) {
                p_mapToBeUpdated.createContinent(p_argument.split(" ")[0], Integer.parseInt(p_argument.split(" ")[1]));
                this.setD_MapServiceLog("Continent "+ p_argument.split(" ")[0]+ " added successfully!", p_gameState);
            } else if (p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==1) {
                p_mapToBeUpdated.deleteContinent(p_argument.split(" ")[0]);
                this.setD_MapServiceLog("Continent "+ p_argument.split(" ")[0]+ " removed successfully!", p_gameState);
            } else {
                throw new InvalidMap("Continent "+p_argument.split(" ")[0]+" couldn't be added/removed. Changes are not made due to Invalid Command Passed.");
            }
        } catch (InvalidMap | NumberFormatException l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
        }
        return p_mapToBeUpdated;
    }

    /**
     * Adds or removes a continent to/from the specified map in the game state based on the operation
     * specified. If adding, the continent name and control value are expected as arguments separated by a
     * space. If removing, only the continent name is expected.
     *
     * The operation is logged within the game state, indicating whether the addition or removal was
     * successful, or if an error occurred, detailing the issue.
     *
     * @param p_gameState The current state of the game, used for logging the outcome of the operation.
     * @param p_mapToBeUpdated The map object to which the continent will be added or from which it will
     *                         be removed.
     * @param p_operation The operation to perform: "add" to add a new continent or "remove" to remove an
     *                    existing continent.
     * @param p_argument The argument for the operation. For adding, this should be the continent name
     *                   followed by the control value, separated by a space. For removing, just the continent
     *                   name.
     *
     * @return The updated map object after performing the add or remove operation.
     * @throws InvalidMap If the operation or arguments are invalid, or if the continent cannot be
     *                    added or removed for some reason.
     */

    public Map addRemoveCountry(GameState p_gameState, Map p_mapToBeUpdated, String p_argument, String p_operation) throws InvalidMap{

        try {
            if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                p_mapToBeUpdated.createCountry(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_MapServiceLog("Country "+ p_argument.split(" ")[0]+ " added successfully!", p_gameState);
            }else if(p_operation.equalsIgnoreCase("remove")&& p_argument.split(" ").length==1){
                p_mapToBeUpdated.deleteCountry(p_argument.split(" ")[0]);
                this.setD_MapServiceLog("Country "+ p_argument.split(" ")[0]+ " removed successfully!", p_gameState);
            }else{
                throw new InvalidMap("Country "+p_argument.split(" ")[0]+" could not be "+ p_operation +"Saved!");
            }
        } catch (InvalidMap l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
        }
        return p_mapToBeUpdated;
    }

    /**
     * Adds or removes a neighboring relationship between two countries on a map. This method takes a pair
     * of country names and either establishes (adds) or severs (removes) their neighboring relationship based
     * on the specified operation. Successful operations are logged in the game state.
     *
     * @param p_gameState The game state object where the map and operation logs are stored. It is used
     *                    to log the result of the neighboring operation.
     * @param p_mapToBeUpdated The map object on which the neighboring relationship will be added or removed.
     *                         This object is modified directly to reflect the changes.
     * @param p_argument The argument specifying the pair of countries to be modified, expected to contain
     *                   exactly two names separated by a space. The first name is considered the 'source'
     *                   country, and the second name the 'target' neighbor.
     * @param p_operation The operation to perform, either "add" to create a new neighboring relationship
     *                    or "remove" to delete an existing one.
     *
     * @return The updated map object after the add or remove operation on the neighbor relationship.
     * @throws InvalidMap If the operation fails due to invalid input arguments or if the operation itself
     *                    is not successfully executed (e.g., trying to add a neighbor relationship that
     *                    already exists or removing one that does not).
     */

    public Map addRemoveNeighbour(GameState p_gameState, Map p_mapToBeUpdated, String p_argument, String p_operation) throws InvalidMap{

        try {
            if (p_operation.equalsIgnoreCase("add") && p_argument.split(" ").length==2){
                p_mapToBeUpdated.addCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_MapServiceLog("Neighbour Pair "+p_argument.split(" ")[0]+" "+p_argument.split(" ")[1]+" added successfully!", p_gameState);
            }else if(p_operation.equalsIgnoreCase("remove") && p_argument.split(" ").length==2){
                p_mapToBeUpdated.deleteCountryNeighbour(p_argument.split(" ")[0], p_argument.split(" ")[1]);
                this.setD_MapServiceLog("Neighbour Pair "+p_argument.split(" ")[0]+" "+p_argument.split(" ")[1]+" removed successfully!", p_gameState);
            }else{
                throw new InvalidMap("Neighbour could not be "+ p_operation +"processed!");
            }
        } catch (InvalidMap l_e) {
            this.setD_MapServiceLog(l_e.getMessage(), p_gameState);
        }
        return p_mapToBeUpdated;
    }
    /**
     * Writes the metadata for countries and their borders to a specified FileWriter. This involves iterating
     * over the countries in the game state's map, writing each country's ID, name, and continent ID to the file,
     * and collecting each country's border data. After processing all countries, it then writes the collected
     * border data to the file, listing all adjacent country IDs for each country.
     *
     * The method first writes a header for the countries section, followed by the country metadata. It then
     * writes a header for the borders section, followed by the border data. This structured approach ensures
     * the file is organized into clearly defined sections for countries and borders, making it easier to
     * read and process.
     *
     * @param p_gameState The current game state containing the map data, including countries and their
     *                    borders. This is the source of the data for writing to the file.
     * @param p_writer    The FileWriter object to which the country and border metadata will be written.
     *                    This FileWriter should be open when passed to this method and will need to be
     *                    managed (flushed or closed) by the caller after this method completes.
     * @throws IOException If an I/O error occurs while writing to the FileWriter. This could happen if the
     *                     FileWriter is not properly initialized, if there's a disk space issue, or if
     *                     another I/O issue occurs during writing.
     */
    private void writeCountryAndBoarderMetaData(GameState p_gameState, FileWriter p_writer) throws IOException {
        String l_countryMetaData = new String();
        String l_bordersMetaData = new String();
        List<String> l_bordersList = new ArrayList<>();

        // Writes Country Objects to File And Organizes Border Data for each of them
        p_writer.write(System.lineSeparator() + ApplicationConstants.COUNTRIES + System.lineSeparator());
        for (Country l_country : p_gameState.getD_map().getD_countries()) {
            l_countryMetaData = new String();
            l_countryMetaData = l_country.getD_countryId().toString().concat(" ").concat(l_country.getD_countryName())
                    .concat(" ").concat(l_country.getD_continentId().toString());
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

        // Writes Border data to the File
        if (null != l_bordersList && !l_bordersList.isEmpty()) {
            p_writer.write(System.lineSeparator() + ApplicationConstants.BORDERS + System.lineSeparator());
            for (String l_borderStr : l_bordersList) {
                p_writer.write(l_borderStr + System.lineSeparator());
            }
        }
    }
    /**
     * Establishes a linkage between countries and continents by adding each country to its corresponding
     * continent based on their IDs. This method iterates through each country in the provided list, matches
     * it with the correct continent by comparing their IDs, and then adds the country to the continent's list
     * of countries.
     *
     * This process ensures that each continent object contains a comprehensive list of all countries that
     * belong to it, facilitating easier access to country information at the continent level.
     *
     * @param p_countries A list of Country objects to be linked to their respective continents. Each Country
     *                    object should have a continent ID that corresponds to the ID of a Continent in the
     *                    provided list of continents.
     * @param p_continents A list of Continent objects that will be updated to include the countries that
     *                     belong to them. Each Continent object should have a unique ID that matches the
     *                     continent ID of Country objects.
     *
     * @return The list of Continent objects, each updated to include a list of Country objects that belong
     *         to it. This reflects the updated state after all countries have been appropriately linked to
     *         their continents.
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
     * Resets the game map to an empty state and logs an error message indicating the provided map is invalid.
     * This method is called when an attempt to load a map fails due to the map being invalid. It informs the
     * user through the console that the provided map cannot be loaded and requires a valid map file. Additionally,
     * it logs this error within the game state for further reference and sets the game state's map to a new,
     * empty map object.
     *
     * @param p_gameState The current game state, which will be updated to reflect the invalid map situation.
     *                    The game state is used here to log the error message and to reset the map to a new
     *                    instance of an empty map.
     * @param p_fileToLoad The name of the map file that was attempted to be loaded and was found invalid.
     *                     This filename is used in the log message to indicate which map file caused the issue.
     */

    public void resetMap(GameState p_gameState, String p_fileToLoad) {
        System.out.println("The Map entered cannot be loaded, It is Invalid. Please provide a Valid Map");
        p_gameState.updateLog(p_fileToLoad+"Invalid Map! Map cannot be loaded", "effect");
        p_gameState.setD_map(new Models.Map());
    }

    /**
     * Logs a message related to map service operations to the console and updates the game state's log.
     * This method is used to provide feedback or report the results of map service operations, such as
     * adding, removing, or modifying map elements. It prints the message to the console for immediate
     * visibility and also updates the game state log with the message and a tag indicating the nature
     * of the log entry (in this case, "effect").
     *
     * @param p_MapServiceLog The message to be logged. This could be any information related to map
     *                        service operations, such as success messages, error messages, or status
     *                        updates.
     * @param p_gameState The current game state, which is updated with the log entry. The game state
     *                    maintains a record of all significant events or operations, allowing for
     *                    auditing or debugging.
     */

    public void setD_MapServiceLog(String p_MapServiceLog, GameState p_gameState){
        System.out.println(p_MapServiceLog);
        p_gameState.updateLog(p_MapServiceLog, "effect");
    }
}


