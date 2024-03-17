package Models;

import java.io.IOException;

import Controllers.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Services.MapService;
import Services.PlayerService;
import Utils.Command;

/**
 * Phase class represents an abstract phase in the game flow.
 * This class provides methods to handle different commands during the game phases.
 */
public abstract class Phase {
    GameState d_gameState;
    GameEngine d_gameEngine;
    MapService d_mapService = new MapService();
    PlayerService d_playerService = new PlayerService();
    boolean l_isMapLoaded;

    /**
     * Constructor for Phase.
     *
     * @param p_gameEngine The game engine instance.
     * @param p_gameState  The current game state.
     */
    public Phase(GameEngine p_gameEngine, GameState p_gameState){
        d_gameEngine = p_gameEngine;
        d_gameState = p_gameState;
    }

    /**
     * Retrieves the current game state.
     *
     * @return The current game state.
     */
    public GameState getD_gameState() {
        return d_gameState;
    }

    /**
     * Sets the current game state.
     *
     * @param p_gameState The game state to be set.
     */
    public void setD_gameState(GameState p_gameState) {
        d_gameState = p_gameState;
    }

    /**
     * Handles the command entered during the phase.
     *
     * @param p_enteredCommand The command entered by the player.
     * @throws InvalidMap     If the map is invalid.
     * @throws InvalidCommand If the command is invalid.
     * @throws IOException    If an I/O error occurs.
     */
    public void handleCommand(String p_enteredCommand) throws InvalidMap, InvalidCommand, IOException {
        commandHandler(p_enteredCommand, null);
    }

    /**
     * Handles the command entered during the phase for a specific player.
     *
     * @param p_enteredCommand The command entered by the player.
     * @param p_player         The player who entered the command.
     * @throws InvalidMap     If the map is invalid.
     * @throws InvalidCommand If the command is invalid.
     * @throws IOException    If an I/O error occurs.
     */
    public void handleCommand(String p_enteredCommand, Player p_player) throws InvalidMap, InvalidCommand, IOException {
        commandHandler(p_enteredCommand, p_player);
    }

    /**
     * Handles the command entered during the phase.
     *
     * @param p_enteredCommand The command entered by the player.
     * @param p_player         The player who entered the command.
     * @throws InvalidMap     If the map is invalid.
     * @throws InvalidCommand If the command is invalid.
     * @throws IOException    If an I/O error occurs.
     */
    private void commandHandler(String p_enteredCommand, Player p_player) throws InvalidMap, InvalidCommand, IOException {
        Command l_command = new Command(p_enteredCommand);
        String l_rootCommand = l_command.getRootCommand();
        l_isMapLoaded = d_gameState.getD_map() != null;

        d_gameState.updateLog(l_command.getD_command(), "command");

        switch (l_rootCommand) {
            case "editmap": {
                performMapEdit(l_command, p_player);
                break;
            }
            case "editcontinent": {
                performEditContinent(l_command, p_player);
                break;
            }
            case "savemap": {
                performSaveMap(l_command, p_player);
                break;
            }
            case "loadmap": {
                performLoadMap(l_command, p_player);
                break;
            }
            case "validatemap": {
                performValidateMap(l_command, p_player);
                break;
            }
            case "editcountry": {
                performEditCountry(l_command, p_player);
                break;
            }
            case "editneighbor": {
                performEditNeighbour(l_command, p_player);
                break;
            }
            case "gameplayer": {
                createPlayers(l_command, p_player);
                break;
            }
            case "assigncountries": {
                performAssignCountries(l_command, p_player);
                break;
            }
            case "showmap": {
                performShowMap(l_command, p_player);
                break;
            }
            case "deploy": {
                performCreateDeploy(p_enteredCommand, p_player);
                break;
            }
            case "advance": {
                performAdvance(p_enteredCommand, p_player);
                break;
            }
            case "airlift":
            case "blockade":
            case "negotiate":
            case "bomb":
            {
                performCardHandle(p_enteredCommand, p_player);
                break;
            }

            case "exit": {
                d_gameEngine.setD_gameEngineLog("Exit Command Entered, Game Ends!", "effect");
                System.exit(0);
                break;
            }
            default: {
                d_gameEngine.setD_gameEngineLog("Invalid Command", "effect");
                break;
            }
        }
    }

    /**
     * Performs handling of card-related commands.
     *
     * @param p_enteredCommand The command entered by the player.
     * @param p_player         The player who entered the command.
     * @throws IOException If an I/O error occurs.
     */
    protected abstract void performCardHandle(String p_enteredCommand, Player p_player) throws IOException;

    /**
     * Performs displaying the map.
     *
     * @param p_command The command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws InvalidCommand If the command is invalid.
     * @throws IOException    If an I/O error occurs.
     * @throws InvalidMap     If the map is invalid.
     */
    protected abstract void performShowMap(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap;

    /**
     * Performs the advance command during the game phase.
     *
     * @param p_command The advance command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws IOException If an I/O error occurs.
     */
    protected abstract void performAdvance(String p_command, Player p_player) throws IOException;

    /**
     * Initializes the phase.
     */
    public abstract void initPhase();

    /**
     * Performs the create deploy command during the game phase.
     *
     * @param p_command The create deploy command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws IOException If an I/O error occurs.
     */
    protected abstract void performCreateDeploy(String p_command, Player p_player) throws IOException;

    /**
     * Prints an invalid command message in the current state.
     */
    public void printInvalidCommandInState(){
        d_gameEngine.setD_gameEngineLog("Invalid Command in Current State", "effect");
    }

    /**
     * Performs the assign countries command during the game phase.
     *
     * @param p_command The assign countries command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws InvalidCommand If the command is invalid.
     * @throws IOException    If an I/O error occurs.
     * @throws InvalidMap     If the map is invalid.
     */
    protected abstract void performAssignCountries(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap;

    /**
     * Creates players during the game phase.
     *
     * @param p_command The command to create players entered by the player.
     * @param p_player  The player who entered the command.
     * @throws InvalidCommand If the command is invalid.
     * @throws IOException    If an I/O error occurs.
     * @throws InvalidMap     If the map is invalid.
     */
    protected abstract void createPlayers(Command p_command, Player p_player) throws InvalidCommand, IOException, InvalidMap;

    /**
     * Performs the edit neighbor command during the game phase.
     *
     * @param p_command The edit neighbor command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws InvalidCommand If the command is invalid.
     * @throws InvalidMap     If the map is invalid.
     * @throws IOException    If an I/O error occurs.
     */
    protected abstract void performEditNeighbour(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException;

    /**
     * Performs the edit country command during the game phase.
     *
     * @param p_command The edit country command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws InvalidCommand If the command is invalid.
     * @throws InvalidMap     If the map is invalid.
     * @throws IOException    If an I/O error occurs.
     */
    protected abstract void performEditCountry(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException;

    /**
     * Performs the validate map command during the game phase.
     *
     * @param p_command The validate map command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws InvalidMap     If the map is invalid.
     * @throws InvalidCommand If the command is invalid.
     * @throws IOException    If an I/O error occurs.
     */
    protected abstract void performValidateMap(Command p_command, Player p_player) throws InvalidMap, InvalidCommand, IOException;

    /**
     * Performs the load map command during the game phase.
     *
     * @param p_command The load map command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws InvalidCommand If the command is invalid.
     * @throws InvalidMap     If the map is invalid.
     * @throws IOException    If an I/O error occurs.
     */
    protected abstract void performLoadMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException;

    /**
     * Performs the save map command during the game phase.
     *
     * @param p_command The save map command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws InvalidCommand If the command is invalid.
     * @throws InvalidMap     If the map is invalid.
     * @throws IOException    If an I/O error occurs.
     */
    protected abstract void performSaveMap(Command p_command, Player p_player) throws InvalidCommand, InvalidMap, IOException;

    /**
     * Performs the edit continent command during the game phase.
     *
     * @param p_command The edit continent command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws IOException    If an I/O error occurs.
     * @throws InvalidCommand If the command is invalid.
     * @throws InvalidMap     If the map is invalid.
     */
    protected abstract void performEditContinent(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap;

    /**
     * Performs the map edit command during the game phase.
     *
     * @param p_command The map edit command entered by the player.
     * @param p_player  The player who entered the command.
     * @throws IOException    If an I/O error occurs.
     * @throws InvalidCommand If the command is invalid.
     * @throws InvalidMap     If the map is invalid.
     */

    protected abstract void performMapEdit(Command p_command, Player p_player) throws IOException, InvalidCommand, InvalidMap;
}
