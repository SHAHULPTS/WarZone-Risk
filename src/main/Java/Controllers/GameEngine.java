package Controllers;
// commit check
import Models.GameState;
import Models.IssueOrderPhase;
import Models.OrderExecutionPhase;
import Models.Phase;
import Models.StartUpPhase;

/**
 * This class represents the game engine responsible for managing the phases of the game.
 */
public class GameEngine {

    /** The current game state. */
    private GameState d_gameState;

    /** The current phase of the game. */
    private Phase d_currentPhase;

    /**
     * Constructs a new GameEngine object with an initial game state and startup phase.
     */
    public GameEngine() {
        d_gameState = new GameState();
        d_currentPhase = new StartUpPhase(this, d_gameState);
    }

    /**
     * Sets the current phase to the Issue Order Phase and initializes it.
     */
    public void setIssueOrderPhase() {
        setD_gameEngineLog("Issue Order Phase", "phase");
        setD_CurrentPhase(new IssueOrderPhase(this, d_gameState));
        getD_CurrentPhase().initPhase();
    }

    /**
     * Sets the current phase to the Order Execution Phase and initializes it.
     */
    public void setOrderExecutionPhase() {
        setD_gameEngineLog("Order Execution Phase", "phase");
        setD_CurrentPhase(new OrderExecutionPhase(this, d_gameState));
        getD_CurrentPhase().initPhase();
    }

    /**
     * Retrieves the current phase of the game.
     * @return The current phase of the game.
     */
    public Phase getD_CurrentPhase() {
        return d_currentPhase;
    }

    /**
     * Sets the current phase to the provided phase.
     * @param p_phase The phase to set as the current phase.
     */
    private void setD_CurrentPhase(Phase p_phase) {
        d_currentPhase = p_phase;
    }

    /**
     * Updates the game engine log with the provided message and log type.
     * @param p_gameEngineLog The message to be logged.
     * @param p_logType The type of log (e.g., "start" for game startup, "phase" for phase change).
     */
    public void setD_gameEngineLog(String p_gameEngineLog, String p_logType) {
        d_currentPhase.getD_gameState().updateLog(p_gameEngineLog, p_logType);
        String l_consoleLogger = p_logType.toLowerCase().equals("phase")
                ? "\n************ " + p_gameEngineLog + " ************\n"
                : p_gameEngineLog;
        System.out.println(l_consoleLogger);
    }

    /**
     * The main method of the GameEngine class.
     * @param p_args The command-line arguments passed to the program.
     */
    public static void main(String[] p_args) {
        GameEngine l_game = new GameEngine();

        l_game.getD_CurrentPhase().getD_gameState().updateLog("Initializing the Game ......"+System.lineSeparator(), "start");
        l_game.setD_gameEngineLog("Game Startup Phase", "phase");
        l_game.getD_CurrentPhase().initPhase();
    }
}
