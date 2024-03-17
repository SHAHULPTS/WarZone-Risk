package Models;

import Constants.ApplicationConstants;
import Controllers.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.Command;
import Utils.CommonUtil;
import Utils.ExceptionLogHandler;
import Views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

/**
 * Represents the startup phase of the game.
 * This phase includes actions such as loading or editing the map, assigning countries to players, and initializing the game.
 */
public class StartUpPhase extends Phase{

    /**
     * Constructor for the StartUpPhase class.
     *
     * @param p_gameEngine The game engine managing the game.
     * @param p_gameState  The current state of the game.
     */
    public StartUpPhase(GameEngine p_gameEngine, GameState p_gameState){
        super(p_gameEngine, p_gameState);
    }

    /**
     * Performs handling of card-related commands during the startup phase.
     *
     * @param p_enteredCommand The command entered by the player.
     * @param p_player         The player executing the command.
     * @throws IOException Signals that an I/O exception of some sort has occurred.
     */
    @Override
    protected void performCardHandle(String p_enteredCommand, Player p_player) throws IOException {
        printInvalidCommandInState();
    }

    /**
     * Displays the map during the startup phase.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player executing the command.
     */
    @Override
    protected void performShowMap(Command p_command, Player p_player) {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();
    }

    /**
     * Prints an invalid command message for the advance command during the startup phase.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player executing the command.
     */
    @Override
    protected void performAdvance(String p_command, Player p_player) {
        printInvalidCommandInState();
    }

    /**
     * Prints an invalid command message for the create deploy command during the startup phase.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player executing the command.
     */
    @Override
    protected void performCreateDeploy(String p_command, Player p_player) {
        printInvalidCommandInState();
    }

    /**
     * Performs map editing based on the provided command during the startup phase.
     *
     * @param p_command The command containing map editing operations.
     * @param p_player  The player executing the command.
     * @throws IOException       Signals that an I/O exception has occurred.
     * @throws InvalidCommand   Thrown to indicate that a method has been passed an illegal or inappropriate argument during map editing.
     * @throws InvalidMap       Thrown to indicate that an error occurred while validating or loading the map.
     */
    public void performMapEdit(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        List<java.util.Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));

        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)) {
                    d_mapService.editMap(d_gameState, l_map.get(ApplicationConstants.ARGUMENTS));
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITMAP);
                }
            }
        }
    }

    /**
     * Performs map loading based on the provided command during the startup phase.
     *
     * @param p_command The command containing map loading operations.
     * @param p_player  The player executing the command.
     * @throws InvalidCommand Thrown to indicate that a method has been passed an illegal or inappropriate argument during map loading.
     * @throws InvalidMap     Thrown to indicate that an error occurred while validating or loading the map.
     */
    public void performLoadMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap {
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();
        boolean l_flagValidate = false;

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_LOADMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)) {
                    // Loads the map if it is valid or resets the game state
                    Models.Map l_mapToLoad = d_mapService.loadMap(d_gameState,
                            l_map.get(ApplicationConstants.ARGUMENTS));
                    if (l_mapToLoad.Validate()) {
                        l_flagValidate = true;
                        d_gameState.setD_loadCommand();
                        d_gameEngine.setD_gameEngineLog(l_map.get(ApplicationConstants.ARGUMENTS)+ " has been loaded to start the game", "effect" );
                    } else {
                        d_mapService.resetMap(d_gameState, l_map.get(ApplicationConstants.ARGUMENTS));
                    }
                    if(!l_flagValidate){
                        d_mapService.resetMap(d_gameState, l_map.get(ApplicationConstants.ARGUMENTS));
                    }
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_LOADMAP);
                }
            }
        }
    }

    /**
     * Performs editing of continents based on the provided command during the startup phase.
     *
     * @param p_command The command containing continent editing operations.
     * @param p_player  The player executing the command.
     * @throws IOException       Signals that an I/O exception has occurred.
     * @throws InvalidCommand   Thrown to indicate that a method has been passed an illegal or inappropriate argument during continent editing.
     * @throws InvalidMap       Thrown to indicate that an error occurred while validating or loading the map.
     */
    public void performEditContinent(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Continent, please perform `editmap` first", "effect");
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (l_operations_list == null || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCONTINENT);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)
                        && p_command.checkRequiredKeysPresent(ApplicationConstants.OPERATION, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstants.ARGUMENTS),
                            l_map.get(ApplicationConstants.OPERATION), 1);
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCONTINENT);
                }
            }
        }
    }

    /**
     * Performs saving of the map based on the provided command during the startup phase.
     *
     * @param p_command The command containing map saving operations.
     * @param p_player  The player executing the command.
     * @throws InvalidCommand Thrown to indicate that a method has been passed an illegal or inappropriate argument during map saving.
     * @throws InvalidMap     Thrown to indicate that an error occurred while validating or loading the map.
     */
    public void performSaveMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found to save, Please `editmap` first", "effect");
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_SAVEMAP);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)) {
                    boolean l_fileUpdateStatus = d_mapService.saveMap(d_gameState,
                            l_map.get(ApplicationConstants.ARGUMENTS));
                    if (l_fileUpdateStatus) {
                        d_gameEngine.setD_gameEngineLog("Required changes have been made in map file", "effect");
                    } else
                        System.out.println(d_gameState.getError());
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_SAVEMAP);
                }
            }
        }
    }

    /**
     * Performs validation of the map based on the provided command during the startup phase.
     *
     * @param p_command The command containing map validation operations.
     * @param p_player  The player executing the command.
     * @throws InvalidMap       Thrown to indicate that the map is invalid.
     * @throws InvalidCommand   Thrown to indicate that a method has been passed an illegal or inappropriate argument during map validation.
     */
    public void performValidateMap(Command p_command, Player p_player) throws InvalidMap, InvalidCommand {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found to validate, Please `loadmap` & `editmap` first", "effect");
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            Models.Map l_currentMap = d_gameState.getD_map();
            if (l_currentMap == null) {
                throw new InvalidMap(ApplicationConstants.INVALID_MAP_ERROR_EMPTY);
            } else {
                if (l_currentMap.Validate()) {
                    d_gameEngine.setD_gameEngineLog(ApplicationConstants.VALID_MAP, "effect");
                } else {
                    throw new InvalidMap("Failed to Validate map!");
                }
            }
        } else {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_VALIDATEMAP);
        }
    }

    /**
     * Performs editing of countries based on the provided command during the startup phase.
     *
     * @param p_command The command containing country editing operations.
     * @param p_player  The player executing the command.
     * @throws InvalidCommand Thrown to indicate that a method has been passed an illegal or inappropriate argument during country editing.
     * @throws InvalidMap     Thrown to indicate that an error occurred while validating or loading the map.
     * @throws IOException    Signals that an I/O exception has occurred.
     */
    public void performEditCountry(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Country, please perform `editmap` first", "effect");
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCOUNTRY);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)
                        && p_command.checkRequiredKeysPresent(ApplicationConstants.OPERATION, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstants.OPERATION),
                            l_map.get(ApplicationConstants.ARGUMENTS), 2);
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCOUNTRY);
                }
            }
        }
    }

    /**
     * Performs editing of neighbors based on the provided command during the startup phase.
     *
     * @param p_command The command containing neighbor editing operations.
     * @param p_player  The player executing the command.
     * @throws InvalidCommand Thrown to indicate that a method has been passed an illegal or inappropriate argument during neighbor editing.
     * @throws InvalidMap     Thrown to indicate that an error occurred while validating or loading the map.
     * @throws IOException    Signals that an I/O exception has occurred.
     */
    public void performEditNeighbour(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("Can not Edit Neighbors, please perform `editmap` first", "effect");
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (null == l_operations_list || l_operations_list.isEmpty()) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCOUNTRY);
        } else {
            for (Map<String, String> l_map : l_operations_list) {
                if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)
                        && p_command.checkRequiredKeysPresent(ApplicationConstants.OPERATION, l_map)) {
                    d_mapService.editFunctions(d_gameState, l_map.get(ApplicationConstants.OPERATION),
                            l_map.get(ApplicationConstants.ARGUMENTS), 3);
                } else {
                    throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_EDITCOUNTRY);
                }
            }
        }
    }

    /**
     * Creates players based on the provided command during the startup phase.
     *
     * @param p_command The command containing player creation operations.
     * @param p_player  The player executing the command.
     * @throws InvalidCommand Thrown to indicate that a method has been passed an illegal or inappropriate argument during player creation.
     */
    public void createPlayers(Command p_command, Player p_player) throws InvalidCommand {
        if (!l_isMapLoaded) {
            d_gameEngine.setD_gameEngineLog("No map found, Please `loadmap` before adding game players", "effect");
            return;
        }

        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (CommonUtil.isCollectionEmpty(l_operations_list)) {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_GAMEPLAYER);
        } else {
            if (d_gameState.getD_loadCommand()) {
                for (Map<String, String> l_map : l_operations_list) {
                    if (p_command.checkRequiredKeysPresent(ApplicationConstants.ARGUMENTS, l_map)
                            && p_command.checkRequiredKeysPresent(ApplicationConstants.OPERATION, l_map)) {
                        d_playerService.updatePlayers(d_gameState, l_map.get(ApplicationConstants.OPERATION),
                                l_map.get(ApplicationConstants.ARGUMENTS));
                    } else {
                        throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_GAMEPLAYER);
                    }
                }
            } else {
                d_gameEngine.setD_gameEngineLog("Please load a valid map first via loadmap command!", "effect");
            }
        }
    }

    /**
     * Initializes the startup phase of the game engine.
     * Allows players to enter commands until the startup phase is completed.
     */
    public void initPhase()  {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));

        while (d_gameEngine.getD_CurrentPhase() instanceof StartUpPhase) {
            try {
                System.out.println("Enter Game Commands or type 'exit' for quitting");
                String l_commandEntered = l_reader.readLine();

                handleCommand(l_commandEntered);
            } catch (InvalidCommand | InvalidMap | IOException l_exception) {
                d_gameEngine.setD_gameEngineLog(l_exception.getMessage(), "effect");
            }
        }
    }

    /**
     * Performs the assignment of countries to players based on the provided command during the startup phase.
     *
     * @param p_command The command containing country assignment operations.
     * @param p_player  The player executing the command.
     * @throws InvalidCommand Thrown to indicate that a method has been passed an illegal or inappropriate argument during country assignment.
     */
    public void performAssignCountries(Command p_command, Player p_player) throws InvalidCommand{
        List<Map<String, String>> l_operations_list = p_command.getOperationsAndArguments();

        Thread.setDefaultUncaughtExceptionHandler(new ExceptionLogHandler(d_gameState));
        if (CommonUtil.isCollectionEmpty(l_operations_list)) {
            d_playerService.assignCountries(d_gameState);
            d_playerService.assignColors(d_gameState);
            d_playerService.assignArmies(d_gameState);
            d_gameEngine.setIssueOrderPhase();
        } else {
            throw new InvalidCommand(ApplicationConstants.INVALID_COMMAND_ERROR_ASSIGNCOUNTRIES);
        }
    }
}
