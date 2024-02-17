package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import Constants.ApplicationConstants;
import Utils.Command;
import Utils.CommonUtil;

public class Player {
    private String d_color;
    private String d_name;
    List<Country> d_coutriesOwned = new ArrayList<>(); // Initialize lists to avoid null pointer exceptions
    List<Continent> d_continentsOwned = new ArrayList<>();
    List<Order> d_ordersToExecute = new ArrayList<>();
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
    public List<Country> getD_coutriesOwned() {
        return d_coutriesOwned;
    }


    public void setD_coutriesOwned(List<Country> p_coutriesOwned) {
        this.d_coutriesOwned = p_coutriesOwned;
    }


    public List<Continent> getD_continentsOwned() {
        return d_continentsOwned;
    }


    public void setD_continentsOwned(List<Continent> p_continentsOwned) {
        this.d_continentsOwned = p_continentsOwned;
    }


    public List<Order> getD_ordersToExecute() {
        return d_ordersToExecute;
    }


    public void setD_ordersToExecute(List<Order> p_ordersToExecute) {
        this.d_ordersToExecute = p_ordersToExecute;
    }


    public Integer getD_noOfUnallocatedArmies() {
        return d_noOfUnallocatedArmies;
    }


    public void setD_noOfUnallocatedArmies(Integer p_numberOfArmies) {
        this.d_noOfUnallocatedArmies = p_numberOfArmies;
    }


    public List<String> getCountryNames() {
        List<String> l_countryNames = new ArrayList<>();
        for (Country country : d_coutriesOwned) {
            l_countryNames.add(country.getD_countryName());
        }
        return l_countryNames;
    }

    public List<String> getContinentNames(){
        List<String> l_continentNames = new ArrayList<String>();
        if (d_continentsOwned != null) {
            for(Continent c: d_continentsOwned){
                l_continentNames.add(c.getD_continentName());
            }
            return l_continentNames;
        }
        return null;
    }


    public void issue_order() throws IOException {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.println("\nEnter command for deploying armies for player: " + getPlayerName());
            String commandEntered = reader.readLine();
            Command command = new Command(commandEntered);

            if ("deploy".equalsIgnoreCase(command.getRootCommand()) && commandEntered.split(" ").length == 3) {
                //new PlayerService().createDeployOrder(commandEntered, this);
            } else {
                System.out.println(ApplicationConstants.INVALID_COMMAND_ERROR_DEPLOY_ORDER);
            }
        }
    }
    public Order next_order() {
        return d_ordersToExecute.isEmpty() ? null : d_ordersToExecute.remove(0);
    }
}