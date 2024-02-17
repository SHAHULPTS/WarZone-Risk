package Controllers;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.Command;
import Views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameEngineController {

    public static void main(String[] p_args) {
        GameEngineController l_game = new GameEngineController();
        l_game.initGamePlay();

        /**
         * This method starts the Command Line Interface to receive commands from the user and associates them with their respective action handlers.
         */
    private void initGamePlay() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                try {
                    System.out.println("Enter Game Commands or type 'exit' for quitting");
                    String commandEntered = reader.readLine();
                    handleCommand(commandEntered);
                } catch (InvalidCommandException | InvalidMapException e) {
                    System.out.println(e.getMessage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void handleCommand(String p_enteredCommand) throws InvalidMap, InvalidCommand, IOException {
        Command l_command = new Command(p_enteredCommand);
        String l_rootCommand = l_command.getRootCommand();
        boolean l_isMapLoaded = d_gameState.getD_map() != null;

        switch (l_rootCommand) {
            case "editmap": {
                performMapEdit(l_command);
                break;
            }
            case "editcontinent": {
                if (!l_isMapLoaded) {
                    System.out.println("Can not Edit Continent, please perform `editmap` first");
                    break;
                }
                performEditContinent(l_command);
                break;
            }
            case "savemap": {
                if (!l_isMapLoaded) {
                    System.out.println("No map found to save, Please `editmap` first");
                    break;
                }

                performSaveMap(l_command);
                break;
            }
            case "loadmap": {
                performLoadMap(l_command);
                break;
            }
            case "validatemap": {
                if (!l_isMapLoaded) {
                    System.out.println("No map found to validate, Please `loadmap` & `editmap` first");
                    break;
                }
                performValidateMap(l_command);
                break;
            }
            case "editcountry": {
                if (!l_isMapLoaded) {
                    System.out.println("Can not Edit Country, please perform `editmap` first");
                    break;
                }
                performEditCountry(l_command);
                break;
            }
            case "editneighbor": {
                if (!l_isMapLoaded) {
                    System.out.println("Can not Edit Neighbors, please perform `editmap` first");
                    break;
                }
                performEditNeighbour(l_command);
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

}
