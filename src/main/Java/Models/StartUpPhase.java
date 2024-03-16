package Models;

import Constants.ApplicationConstants;
import Controllers.GameEngine;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.Command;
import Utils.ExceptionLogHandler;
import Views.MapView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class StartUpPhase {
    public StartUpPhase(GameEngine p_gameEngine, GameState p_gameState){
        super(p_gameEngine, p_gameState);
    }


    @Override
    protected void performCardHandle(String p_enteredCommand, Player p_player) throws IOException {
        printInvalidCommandInState();
    }

    @Override
    protected void performShowMap(Command p_command, Player p_player) {
        MapView l_mapView = new MapView(d_gameState);
        l_mapView.showMap();
    }

    @Override
    protected void performAdvance(String p_command, Player p_player) {
        printInvalidCommandInState();
    }

    @Override
    protected void performCreateDeploy(String p_command, Player p_player) {
        printInvalidCommandInState();
    }

    /**
     * {@inheritDoc}
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
}
