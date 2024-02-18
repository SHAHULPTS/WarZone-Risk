package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import Constants.ApplicationConstants;
import Services.PlayerService;
import Utils.Command;
import Utils.CommonUtil;


/**
 * Represents a player in the game.
 */
public class Player {
    /**
     * The colour associated with the player.
     */
    private String d_color;

    /**
     * The name of the player.
     */
    private String d_name;

    /**
     * List of Countries under the player control.
     */
    List<Country> d_coutriesOwned;

    /**
     * List of Countries under the player control.
     */
    List<Continent> d_continentsOwned;

    /**
     * List of orders to be executed by the player.
     */
    List<Order> d_ordersToExecute;

    /**
     * Integer of unallocated armmies for the player.
     */
    Integer d_noOfUnallocatedArmies;

    /**
     * Parameterized constructor constructs a player with a specified name and
     * initializes d_noOfUnallocatedArmies to zero.
     * @param p_playerName Player name .
     */
    public Player(String p_playerName) {
        this.d_name = p_playerName;
        this.d_noOfUnallocatedArmies = 0;
        this.d_ordersToExecute = new ArrayList<>();
    }

    /**
     * Default constructor for player.
     */
    public Player() {

    }

    /**
     * Returns the player's name.
     * @return The player's name.
     */
    public String getPlayerName() {
        return d_name;
    }

    /**
     * Sets the player's name.
     * @param p_name the player's name.
     */
    public void setPlayerName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Returns the player's colour.
     * @return The player's colour.
     */
    public String getD_color() {
        return d_color;
    }

    /**
     * Sets the player's colour.
     * @param p_color The player's colour.
     */
    public void setD_color(String p_color) {
        d_color = p_color;
    }

    /**
     * Returns the list of countries owned by the player.
     * @return List of countries owned by the player.
     */
    public List<Country> getD_coutriesOwned() {
        return d_coutriesOwned;
    }

    /**
     * Sets the list of countries owned by the player.
     * @param p_coutriesOwned Countries owned by the player.
     */
    public void setD_coutriesOwned(List<Country> p_coutriesOwned) {
        this.d_coutriesOwned = p_coutriesOwned;
    }

    /**
     * Returns the list of continents owned by the player.
     * @return List of continent owned by the player
     */
    public List<Continent> getD_continentsOwned() {
        return d_continentsOwned;
    }

    /**
     * Sets the list of continents owned by the player.
     * @param p_continentsOwned Continents owned by the player.
     */
    public void setD_continentsOwned(List<Continent> p_continentsOwned) {
        this.d_continentsOwned = p_continentsOwned;
    }

    /**
     * Returns the list of orders to executed by the player.
     * @return List of orders to executed by the player.
     */
    public List<Order> getD_ordersToExecute() {
        return d_ordersToExecute;
    }

    /**
     * Sets the list of orders to executed by the player.
     * @param p_ordersToExecute List of orders to executed by the player.
     */
    public void setD_ordersToExecute(List<Order> p_ordersToExecute) {
        this.d_ordersToExecute = p_ordersToExecute;
    }

    /**
     * Returns the number of unallocated armies available to the player.
     * @return number of unallocated armies available to the player.
     */
    public Integer getD_noOfUnallocatedArmies() {
        return d_noOfUnallocatedArmies;
    }

    /**
     * Sets the number of unallocated armies available to the player.
     * @param p_numberOfArmies number of unallocated armies available to the player.
     */
    public void setD_noOfUnallocatedArmies(Integer p_numberOfArmies) {
        this.d_noOfUnallocatedArmies = p_numberOfArmies;
    }

    /**
     * Returns the list of countries which player owned by player.
     * @return List of countries which player owned by player.
     */
    public List<String> getCountryNames(){
        List<String> l_countryNames=new ArrayList<String>();
        for(Country c: d_coutriesOwned){
            l_countryNames.add(c.getD_countryName());
        }
        return l_countryNames;
    }

    /**
     * Returns the list of continents which player owned by player.
     * @return List of continents which player owned by player.
     */
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

    /**
     * Issues an order for the player
     * @throws IOException If an I/O error occurs.
     */
    public void issue_order() throws IOException {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        PlayerService l_playerService = new PlayerService();
        System.out.println("\nPlease enter command to deploy reinforcement armies on the map for player : "
                + this.getPlayerName());
        String l_commandEntered = l_reader.readLine();
        Command l_command = new Command(l_commandEntered);

        if (l_command.getRootCommand().equalsIgnoreCase("deploy") && l_commandEntered.split(" ").length == 3) {
            l_playerService.createDeployOrder(l_commandEntered, this);
        } else {
            System.out.println(ApplicationConstants.INVALID_COMMAND_ERROR_DEPLOY_ORDER);;
        }
    }

    /**
     * Returns the next order to be executed by the player.
     * @return The next order, or null if no orders are pending.
     */

    public Order next_order() {
        if (CommonUtil.isCollectionEmpty(this.d_ordersToExecute)) {
            return null;
        }
        Order l_order = this.d_ordersToExecute.get(0);
        this.d_ordersToExecute.remove(l_order);
        return l_order;
    }
}
