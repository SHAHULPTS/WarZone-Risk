package Controllers;

import Constants.ApplicationConstants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.Player;
import Services.MapService;
import Utils.Command;
import Utils.CommonUtil;
import Views.MapView;
import Models.GameState;
import Services.PlayerService;
import Models.Order;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * The GameEngineController class manages the game engine and handles user commands through a Command Line Interface.
 */
public class GameEngine {

    /** The current state of the game. */
    GameState d_gameState = new GameState();

    /** Service for managing map-related operations. */
    MapService d_mapService = new MapService();

    /** Service for managing player-related operations. */
    PlayerService d_playerService = new PlayerService();

    /**
     * Gets the current game state.
     *
     * @return The current GameState object.
     */
    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * The entry point for the game engine.
     *
     * @param p_args Command-line arguments.
     */
    public static void main(String[] p_args) {
        GameEngine l_game = new GameEngine();
        l_game.initGamePlay();
    }

    /**
     * This method starts the Command Line Interface to receive commands from the user and associates them with their respective action handlers.
     */

    private void initGamePlay() {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.println("Enter Game Commands or type 'exit' for quitting");
                String l_commandEntered = l_reader.readLine();
                handleCommand(l_commandEntered);
            } catch (InvalidCommand | InvalidMap l_exception) {
                System.out.println(l_exception.getMessage());
            } catch (IOException l_ioException) {
                l_ioException.printStackTrace();
            }
        }
    }
    /**
     * Handles the user command.
     *
     * @param p_enteredCommand The command entered by the user.
     * @throws InvalidMap       If an invalid map is encountered.
     * @throws InvalidCommand   If an invalid command is entered.
     * @throws IOException      If an I/O error occurs.
     */
    public void handleCommand(String p_enteredCommand) throws InvalidMap, InvalidCommand, IOException {
        Command l_command = new Command(p_enteredCommand);
        String l_rootCommand = l_command.getRootCommand();
        boolean l_isMapLoaded = d_gameState.getD_map() != null;

        switch (l_rootCommand) {
            case "editmap": {
                MapEdit(l_command);
                break;
            }
            case "editcontinent": {
                if (!l_isMapLoaded) {
                    System.out.println("Can not Edit Continent, please perform `editmap` first");
                    break;
                }
                EditContinent(l_command);
                break;
            }
            case "savemap": {
                if (!l_isMapLoaded) {
                    System.out.println("No map found to save, Please `editmap` first");
                    break;
                }

                SaveMap(l_command);
                break;
            }
            case "loadmap": {
                LoadMap(l_command);
                break;
            }
            case "validatemap": {
                if (!l_isMapLoaded) {
                    System.out.println("No map found to validate, Please `loadmap` & `editmap` first");
                    break;
                }
                ValidateMap(l_command);
                break;
            }
            case "editcountry": {
                if (!l_isMapLoaded) {
                    System.out.println("Can not Edit Country, please perform `editmap` first");
                    break;
                }
                EditCountry(l_command);
                break;
            }
            case "editneighbor": {
                if (!l_isMapLoaded) {
                    System.out.println("Can not Edit Neighbors, please perform `editmap` first");
                    break;
                }
                EditNeighbour(l_command);
                break;
            }
            case "gameplayer": {
                if (!l_isMapLoaded) {
                    System.out.println("No map found, Please `loadmap` before adding game players");
                    break;
                }
                createPlayers(l_command);
                break;
            }
            case "assigncountries": {
                assignCountries(l_command);
                break;
            }
            case "showmap": {
                MapView l_mapView = new MapView(d_gameState);
                l_mapView.showMap();
                break;
            }
            case "exit": {
                System.out.println("Exit Command Entered");
                System.exit(0);
                break;
            }
            default: {
                System.out.println("Invalid Command");
                break;
            }
        }
    }

    /**
     * Performs the loading of the map.
     *
     * @param p_command The command containing map loading information.
     * @throws InvalidCommand If an invalid command is encountered.
     */
    private void LoadMap(Command p_command) throws InvalidCommand {
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_LOADMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.hasRequiredKeys(ApplicationConstants.ARGUMENTS, l_map)) {
                    try {

                        // Loads the map if it is valid or resets the game state
                        Models.Map l_mapToLoad = d_mapService.loadMap(d_gameState,
                                l_map.get(ApplicationConstants.ARGUMENTS));
                        if (l_mapToLoad.Validate()) {
                            System.out.println("Map has been loaded successfully. \n");
                        } else {
                            d_mapService.resetMap(d_gameState);
                        }
                    } catch (InvalidMap l_e) {
                        d_mapService.resetMap(d_gameState);
                    }
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_LOADMAP);
                }
            }
        }
    }


    /**
     * Performs editing of continent.
     *
     * @param p_command The command containing continent editing information.
     * @throws IOException      If an I/O error occurs.
     * @throws InvalidCommand   If an invalid command is encountered.
     * @throws InvalidMap       If an invalid map is encountered.
     */
    public void EditContinent(Command p_command) throws IOException, InvalidCommand, InvalidMap {
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCONTINENT);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.hasRequiredKeys(ApplicationConstants.ARGUMENTS, l_map)
                        && p_command.hasRequiredKeys(ApplicationConstants.OPERATION, l_map)) {
                    d_mapService.editContinent(d_gameState, l_map.get(ApplicationConstants.ARGUMENTS),
                            l_map.get(ApplicationConstants.OPERATION));
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCONTINENT);
                }
            }
        }
    }

    /**
     * Performs saving of the map.
     *
     * @param p_command The command containing map saving information.
     * @throws InvalidCommand If an invalid command is encountered.
     * @throws InvalidMap     If an invalid map is encountered.
     */
    public void SaveMap(Command p_command) throws InvalidCommand, InvalidMap {
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_SAVEMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.hasRequiredKeys(ApplicationConstants.ARGUMENTS, l_map)) {
                    boolean l_fileUpdateStatus = d_mapService.saveMap(d_gameState,l_map.get(ApplicationConstants.ARGUMENTS));
                    if (l_fileUpdateStatus)
                        System.out.println("Required changes has been done in map file");
                    else
                        System.out.println(d_gameState.getError());
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_SAVEMAP);
                }
            }
        }
    }

    /**
     * Performs map validation.
     *
     * @param p_command The command containing map validation information.
     * @throws InvalidMap     If an invalid map is encountered.
     * @throws InvalidCommand If an invalid command is encountered.
     */
    private void ValidateMap(Command p_command) throws InvalidMap, InvalidCommand {
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            Models.Map l_currentMap = d_gameState.getD_map();
            if (l_currentMap == null) {
                throw new InvalidMap(ApplicationConstants.INVALID_MAP_ERROR_EMPTY);
            } else {
                if (l_currentMap.Validate()) {
                    System.out.println(ApplicationConstants.VALID_MAP);
                } else {
                    System.out.println("Failed to Validate map!");
                }
            }
        } else {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_VALIDATEMAP);
        }
    }

    /**
     * Performs editing of country.
     *
     * @param p_command The command containing country editing information.
     * @throws InvalidCommand If an invalid command is encountered.
     * @throws InvalidMap     If an invalid map is encountered.
     */
    public void EditCountry(Command p_command) throws InvalidCommand, InvalidMap {
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCOUNTRY);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.hasRequiredKeys(ApplicationConstants.ARGUMENTS, l_map)
                        && p_command.hasRequiredKeys(ApplicationConstants.OPERATION, l_map)) {
                    d_mapService.editCountry(d_gameState, l_map.get(ApplicationConstants.OPERATION),
                            l_map.get(ApplicationConstants.ARGUMENTS));
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCOUNTRY);
                }
            }
        }
    }

    /**
     * Performs editing of neighbors for a country.
     *
     * @param p_command The command containing neighbor editing information.
     * @throws InvalidCommand If an invalid command is encountered.
     * @throws InvalidMap     If an invalid map is encountered.
     */
    public void EditNeighbour(Command p_command) throws InvalidCommand, InvalidMap {
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITNEIGHBOUR);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.hasRequiredKeys(ApplicationConstants.ARGUMENTS, l_map)
                        && p_command.hasRequiredKeys(ApplicationConstants.OPERATION, l_map)) {
                    d_mapService.editNeighbour(d_gameState, l_map.get(ApplicationConstants.OPERATION),
                            l_map.get(ApplicationConstants.ARGUMENTS));
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITNEIGHBOUR);
                }
            }
        }
    }

    /**
     * Creates players for the game.
     *
     * @param p_command The command containing player creation information.
     * @throws InvalidCommand If an invalid command is encountered.
     */
    private void createPlayers(Command p_command) throws InvalidCommand {
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
        if (CommonUtil.isCollectionEmpty(l_operations_list)) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_GAMEPLAYER);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.hasRequiredKeys(ApplicationConstants.ARGUMENTS, l_map)
                        && p_command.hasRequiredKeys(ApplicationConstants.OPERATION, l_map)) {
                    d_playerService.updatePlayers(d_gameState, l_map.get(ApplicationConstants.OPERATION),
                            l_map.get(ApplicationConstants.ARGUMENTS));
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_GAMEPLAYER);
                }
            }
        }
    }

    /**
     * Assigns countries to players and initiates the game loop.
     *
     * @param p_command The command containing country assignment information.
     * @throws InvalidCommand If an invalid command is encountered.
     * @throws IOException    If an I/O error occurs.
     */
    void assignCountries(Command p_command) throws InvalidCommand, IOException {
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
        if (CommonUtil.isCollectionEmpty(l_operations_list)) {
            d_playerService.assignCountries(d_gameState);
            d_playerService.assignColors(d_gameState);

            while (!CommonUtil.isCollectionEmpty(d_gameState.getD_players())) {
                System.out.println("\n********Starting Main Game Loop***********\n");


                d_playerService.assignArmies(d_gameState); // assigning armies to players


                while (d_playerService.unassignedArmiesExists(d_gameState.getD_players())) {
                    for (Player l_player : d_gameState.getD_players()) {
                        if (l_player.getD_noOfUnallocatedArmies() != null && l_player.getD_noOfUnallocatedArmies() != 0) {
                            l_player.issue_order();
                        }
                    }
                }

                // Executing the orders
                while (d_playerService.unexecutedOrdersExists(d_gameState.getD_players())) {
                    for (Player l_player : d_gameState.getD_players()) {
                        Order l_order = l_player.next_order();
                        if (l_order != null) {
                            l_order.execute(d_gameState, l_player);
                        }
                    }
                }

                MapView l_map_view = new MapView(d_gameState, d_gameState.getD_players());
                l_map_view.showMap();

                System.out.println("Press Y/y if you want to continue for next turn or else press N/n");
                BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
                String l_continue = l_reader.readLine();
                if (l_continue.equalsIgnoreCase("N"))
                    break;
            }
        } else {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_ASSIGNCOUNTRIES);
        }

    }

    /**
     * Performs map editing.
     *
     * @param p_command The command containing map editing information.
     * @throws IOException      If an I/O error occurs.
     * @throws InvalidCommand   If an invalid command is encountered.
     */
    public void MapEdit(Command p_command) throws IOException, InvalidCommand {
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.hasRequiredKeys(ApplicationConstants.ARGUMENTS, l_map)) {
                    d_mapService.editMap(d_gameState, l_map.get(ApplicationConstants.ARGUMENTS));
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_MAP_LOADED);
                }
            }
        }
    }

}