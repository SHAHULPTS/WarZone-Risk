package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import Constants.ApplicationConstants;
import Utils.Command;
import Utils.CommonUtil;

public class Player{
    private String d_color;
    private String d_name;
    List<Country> d_coutriesOwned = new ArrayList<>(); // Initialize lists to avoid null pointer exceptions
    List<Continent> d_continentsOwned = new ArrayList<>();
    //List<Order> d_ordersToExecute = new ArrayList<>();
    Integer d_noOfUnallocatedArmies = 0;

    public Player(String p_playerName) {
        this.d_name = p_playerName;
    }

    public String getPlayerName() {
        return d_name;
    }

    public void setPlayerName(String p_name) {
        this.d_name = p_name;
    }

    public String getD_color() {
        return d_color;
    }

    public void setD_color(String p_color) {
        d_color = p_color;
    }

    public List<String> getCountryNames() {
        List<String> countryNames = new ArrayList<>();
        for (Country country : d_coutriesOwned) {
            countryNames.add(country.getD_countryName());
        }
        return countryNames;
    }


    public void issue_order() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("\nEnter command for deploying armies for player: " + getPlayerName());
            String commandEntered = reader.readLine();
            Command command = new Command(commandEntered);

            if ("deploy".equalsIgnoreCase(command.getRootCommand()) && commandEntered.split(" ").length == 3) {
                new PlayerService().createDeployOrder(commandEntered, this);
            } else {
                System.out.println(ApplicationConstants.INVALID_COMMAND_ERROR_DEPLOY_ORDER);
            }
        }


    

}