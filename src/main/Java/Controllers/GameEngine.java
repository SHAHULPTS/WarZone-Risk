package Controllers;

import Models.GameState;
import Models.IssueOrderPhase;
import Models.OrderExecutionPhase;
import Models.Phase;
import Models.StartUpPhase;


public class GameEngine {

    GameState d_gameState = new GameState();
    Phase d_currentPhase = new StartUpPhase(this, d_gameState);
    private void setD_CurrentPhase(Phase p_phase){
        d_currentPhase = p_phase;
    }

    public void setIssueOrderPhase(){
        this.setD_gameEngineLog("Issue Order Phase", "phase");
        setD_CurrentPhase(new IssueOrderPhase(this, d_gameState));
        getD_CurrentPhase().initPhase();
    }

    public void setOrderExecutionPhase(){
        this.setD_gameEngineLog("Order Execution Phase", "phase");
        setD_CurrentPhase(new OrderExecutionPhase(this, d_gameState));
        getD_CurrentPhase().initPhase();
    }

    public Phase getD_CurrentPhase(){
        return d_currentPhase;
    }

    public void setD_gameEngineLog(String p_gameEngineLog, String p_logType) {
        d_currentPhase.getD_gameState().updateLog(p_gameEngineLog, p_logType);
        String l_consoleLogger = p_logType.toLowerCase().equals("phase")
                ? "\n************ " + p_gameEngineLog + " ************\n"
                : p_gameEngineLog;
        System.out.println(l_consoleLogger);
    }

    public static void main(String[] p_args) {
        GameEngine l_game = new GameEngine();

        l_game.getD_CurrentPhase().getD_gameState().updateLog("Initializing the Game ......"+System.lineSeparator(), "start");
        l_game.setD_gameEngineLog("Game Startup Phase", "phase");
        l_game.getD_CurrentPhase().initPhase();

    }








}