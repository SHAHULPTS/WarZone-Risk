package Controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GameEngineController {

    public static void main(String[] p_args) {
        GameEngineController l_game = new GameEngineController();

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

    private static void handleCommand(String commandEntered) {

    }
}
