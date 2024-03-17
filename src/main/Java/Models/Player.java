package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import Constants.ApplicationConstants;
import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Utils.CommonUtil;

/**
 * Represents a player in the game, encapsulating all relevant attributes such as name, color, owned countries and continents,
 * a list of orders, number of unallocated armies, card ownership, negotiation states, and more.
 * This class provides functionalities for managing player actions, orders issuance, card usage, and game state interactions.
 */
public class Player {
    private String d_color;
    private String d_name;
    List<Country> d_coutriesOwned;
    List<Continent> d_continentsOwned;
    List<Order> order_list;
    Integer d_noOfUnallocatedArmies;
    boolean d_moreOrders;
    boolean d_oneCardPerTurn = false;
    String d_playerLog;
    List<String> d_cardsOwnedByPlayer = new ArrayList<String>();
    List<Player> d_negotiatedWith = new ArrayList<Player>();

    /**
     * Constructs a Player with a specified name. Initializes the player with no allocated armies,
     * empty lists for countries owned, continents owned, orders, and cards. Sets the flag for more orders to true.
     *
     * @param p_playerName The name of the player.
     */
    public Player(String p_playerName) {
        this.d_name = p_playerName;
        this.d_noOfUnallocatedArmies = 0;
        this.d_coutriesOwned = new ArrayList<Country>();
        this.order_list = new ArrayList<Order>();
        this.d_moreOrders = true;
    }

    /**
     * Default constructor for Player, initializing with default values.
     */
    public Player() {
    }

    /**
     * Gets the player's name.
     *
     * @return The name of the player.
     */
    public String getPlayerName() {
        return d_name;
    }

    /**
     * Sets the player's name.
     *
     * @param p_name The new name to be set for the player.
     */
    public void setPlayerName(String p_name) {
        this.d_name = p_name;
    }

    /**
     * Gets the player's color.
     *
     * @return The color of the player.
     */
    public String getD_color() {
        return d_color;
    }

    /**
     * Sets the player's color.
     *
     * @param p_color The color to be set for the player.
     */
    public void setD_color(String p_color) {
        d_color = p_color;
    }

    /**
     * Gets the list of countries owned by the player.
     *
     * @return A list of countries owned by the player.
     */
    public List<Country> getD_coutriesOwned() {
        return d_coutriesOwned;
    }

    /**
     * Sets the list of countries owned by the player.
     *
     * @param p_coutriesOwned The list of countries to be owned by the player.
     */
    public void setD_coutriesOwned(List<Country> p_coutriesOwned) {
        this.d_coutriesOwned = p_coutriesOwned;
    }

    /**
     * Gets the list of continents owned by the player.
     *
     * @return A list of continents owned by the player.
     */
    public List<Continent> getD_continentsOwned() {
        return d_continentsOwned;
    }

    /**
     * Sets the list of continents owned by the player.
     *
     * @param p_continentsOwned The list of continents to be owned by the player.
     */
    public void setD_continentsOwned(List<Continent> p_continentsOwned) {
        this.d_continentsOwned = p_continentsOwned;
    }

    /**
     * Gets the list of orders to execute.
     *
     * @return A list of orders the player has issued.
     */
    public List<Order> getD_ordersToExecute() {
        return order_list;
    }

    /**
     * Sets the list of orders for the player to execute.
     *
     * @param p_ordersToExecute The list of orders to be set for the player.
     */
    public void setD_ordersToExecute(List<Order> p_ordersToExecute) {
        this.order_list = p_ordersToExecute;
    }

    /**
     * Gets the number of unallocated armies the player has.
     *
     * @return The number of unallocated armies.
     */
    public Integer getD_noOfUnallocatedArmies() {
        return d_noOfUnallocatedArmies;
    }

    /**
     * Sets the number of unallocated armies for the player.
     *
     * @param p_numberOfArmies The number of unallocated armies to be set for the player.
     */
    public void setD_noOfUnallocatedArmies(Integer p_numberOfArmies) {
        this.d_noOfUnallocatedArmies = p_numberOfArmies;
    }

    /**
     * Adds a player to the list of players negotiated with by this player.
     *
     * @param p_playerNegotiation The player to add to the negotiation list.
     */
    public void addPlayerNegotiation(Player p_playerNegotiation) {
        this.d_negotiatedWith.add(p_playerNegotiation);
    }

    /**
     * Retrieves the flag indicating whether the player has more orders to issue.
     *
     * @return True if the player has more orders to issue, false otherwise.
     */
    public boolean getD_moreOrders() {
        return d_moreOrders;
    }

    /**
     * Sets the flag indicating whether the player has more orders to issue.
     *
     * @param p_moreOrders The value to set for the flag indicating more orders.
     */
    public void setD_moreOrders(boolean p_moreOrders) {
        this.d_moreOrders = p_moreOrders;
    }

    /**
     * Retrieves the list of cards owned by the player.
     *
     * @return The list of cards owned by the player.
     */
    public List<String> getD_cardsOwnedByPlayer(){ return this.d_cardsOwnedByPlayer; }

    /**
     * Sets the flag indicating whether the player can issue only one card per turn.
     *
     * @param p_value The value to set for the one card per turn flag.
     */
    public void setD_oneCardPerTurn(Boolean p_value){
        this.d_oneCardPerTurn = p_value;
    }

    /**
     * Retrieves the names of countries owned by the player.
     *
     * @return A list containing the names of countries owned by the player.
     */
    public List<String> getCountryNames() {
        List<String> l_countryNames = new ArrayList<String>();
        for (Country c : d_coutriesOwned) {
            l_countryNames.add(c.getD_countryName());
        }
        return l_countryNames;
    }

    /**
     * Retrieves the names of continents owned by the player.
     *
     * @return A list containing the names of continents owned by the player,
     *         or null if the player does not own any continent.
     */
    public List<String> getContinentNames() {
        List<String> l_continentNames = new ArrayList<String>();
        if (d_continentsOwned != null) {
            for (Continent c : d_continentsOwned) {
                l_continentNames.add(c.getD_continentName());
            }
            return l_continentNames;
        }
        return null;
    }

    /**
     * Sets the player's log message and prints it based on the log type.
     *
     * @param p_playerLog The log message to set.
     * @param p_typeLog   The type of log ("error" or "log").
     */
    public void setD_playerLog(String p_playerLog, String p_typeLog) {
        this.d_playerLog = p_playerLog;
        if(p_typeLog.equals("error"))
            System.err.println(p_playerLog);
        else if(p_typeLog.equals("log"))
            System.out.println(p_playerLog);
    }

    /**
     * Retrieves the player's log message.
     *
     * @return The player's log message.
     */
    public String getD_playerLog(){
        return this.d_playerLog;
    }

    /**
     * Prompts the player to check if they want to issue more orders for the next turn.
     * Reads input from the console to determine the player's choice.
     * Sets the flag indicating whether the player has more orders to issue based on the input.
     *
     * @throws IOException If an I/O error occurs.
     */
    void checkForMoreOrders() throws IOException {
        BufferedReader l_reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\nDo you still want to give order for player : " + this.getPlayerName()
                + " in next turn ? \nPress Y for Yes or N for No");
        String l_nextOrderCheck = l_reader.readLine();
        if (l_nextOrderCheck.equalsIgnoreCase("Y") || l_nextOrderCheck.equalsIgnoreCase("N")) {
            this.setD_moreOrders(l_nextOrderCheck.equalsIgnoreCase("Y") ? true : false);
        } else {
            System.err.println("Invalid Input Passed.");
            this.checkForMoreOrders();
        }
    }

    /**
     * Creates a deploy order based on the given command.
     * Parses the command to extract the target country and number of armies.
     * Validates the number of armies in the deploy order against player's unallocated armies.
     * Adds the deploy order to the order list if validation passes, otherwise sets an error log.
     *
     * @param p_commandEntered The command entered by the player.
     */
    public void createDeployOrder(String p_commandEntered){
        String l_targetCountry;
        String l_noOfArmies;
        try {
            l_targetCountry = p_commandEntered.split(" ")[1];
            l_noOfArmies = p_commandEntered.split(" ")[2];
            if (validateDeployOrderArmies(this, l_noOfArmies)) {
                this.setD_playerLog(
                        "Given deploy order cant be executed as armies in deploy order exceeds player's unallocated armies.", "error");
            } else {
                this.order_list.add(new Deploy(this, l_targetCountry, Integer.parseInt(l_noOfArmies)));
                Integer l_unallocatedarmies = this.getD_noOfUnallocatedArmies() - Integer.parseInt(l_noOfArmies);
                this.setD_noOfUnallocatedArmies(l_unallocatedarmies);
                this.setD_playerLog("Deploy order has been added to queue for execution. For player: " + this.d_name, "log");

            }
        } catch (Exception l_e) {
            this.setD_playerLog("Invalid deploy order entered", "error");
        }

    }

    /**
     * Validates whether the number of armies in a deploy order exceeds the player's unallocated armies.
     *
     * @param p_player The player for whom the validation is performed.
     * @param p_noOfArmies The number of armies in the deploy order.
     * @return True if the number of armies exceeds unallocated armies, false otherwise.
     */
    public boolean validateDeployOrderArmies(Player p_player, String p_noOfArmies) {
        return p_player.getD_noOfUnallocatedArmies() < Integer.parseInt(p_noOfArmies) ? true : false;
    }

    /**
     * Issues an order by prompting the provided IssueOrderPhase to ask for an order from this player.
     * Throws exceptions if the command is invalid or if there are issues with the map.
     *
     * @param p_issueOrderPhase The phase responsible for asking the player for an order.
     * @throws InvalidCommand If the issued command is invalid.
     * @throws IOException If an I/O error occurs.
     * @throws InvalidMap If there are issues with the map.
     */
    public void issue_order(IssueOrderPhase p_issueOrderPhase) throws InvalidCommand, IOException, InvalidMap {
        p_issueOrderPhase.askForOrder(this);
    }

    /**
     * Retrieves the next order from the player's order list.
     *
     * @return The next order in the player's order list, or null if the list is empty.
     */
    public Order next_order() {
        if (CommonUtil.isCollectionEmpty(this.order_list)) {
            return null;
        }
        Order l_order = this.order_list.get(0);
        this.order_list.remove(l_order);
        return l_order;
    }


    /**
     * Creates an advance order based on the provided command and game state.
     * Parses the command to extract the source country, target country, and number of armies.
     * Checks if the command arguments are valid and if the countries are adjacent.
     * If all conditions are met, adds the advance order to the order list for execution.
     * Otherwise, sets an error log.
     *
     * @param p_commandEntered The command entered by the player.
     * @param p_gameState The current state of the game.
     */
    public void createAdvanceOrder(String p_commandEntered, GameState p_gameState) {
        try {
            if (p_commandEntered.split(" ").length == 4) {
                String l_sourceCountry = p_commandEntered.split(" ")[1];
                String l_targetCountry = p_commandEntered.split(" ")[2];
                String l_noOfArmies = p_commandEntered.split(" ")[3];
                if (this.checkCountryExists(l_sourceCountry, p_gameState)
                        && this.checkCountryExists(l_targetCountry, p_gameState)
                        && !checkZeroArmiesInOrder(l_noOfArmies)
                        && checkAdjacency(p_gameState, l_sourceCountry, l_targetCountry)) {
                    this.order_list
                            .add(new Advance(this, l_sourceCountry, l_targetCountry, Integer.parseInt(l_noOfArmies)));
                    this.setD_playerLog("Advance order has been added to queue for execution. For player: " + this.d_name, "log");
                }
            } else {
                this.setD_playerLog("Invalid Arguments Passed For Advance Order", "error");
            }

        } catch (Exception l_e) {
            this.setD_playerLog("Invalid Advance Order Given", "error");
        }
    }

    /**
     * Checks whether a country with the given name exists in the game map.
     * If the country does not exist, sets an error log indicating the country is missing.
     *
     * @param p_countryName The name of the country to check.
     * @param p_gameState The current state of the game.
     * @return True if the country exists, false otherwise.
     */
    private Boolean checkCountryExists(String p_countryName, GameState p_gameState) {
        if (p_gameState.getD_map().getCountryByName(p_countryName) == null) {
            this.setD_playerLog("Country : " + p_countryName
                    + " given in advance order doesnt exists in map. Order given is ignored.", "error");
            return false;
        }
        return true;
    }

    /**
     * Checks whether the number of armies in an advance order is zero.
     * If the number of armies is zero, sets an error log indicating the invalidity of the order.
     *
     * @param p_noOfArmies The number of armies to check.
     * @return True if the number of armies is zero, false otherwise.
     */
    private Boolean checkZeroArmiesInOrder(String p_noOfArmies) {
        if (Integer.parseInt(p_noOfArmies) == 0) {
            this.setD_playerLog("Advance order with 0 armies to move cant be issued.", "error");
            return true;
        }
        return false;
    }

    /**
     * Checks if the target country is adjacent to the source country.
     * Retrieves the source and target countries from the game state and verifies adjacency.
     * If the target country is not adjacent to the source country, sets an error log.
     *
     * @param p_gameState The current state of the game.
     * @param p_sourceCountryName The name of the source country.
     * @param p_targetCountryName The name of the target country.
     * @return True if the target country is adjacent to the source country, false otherwise.
     */
    @SuppressWarnings("unlikely-arg-type")
    public boolean checkAdjacency(GameState p_gameState, String p_sourceCountryName, String p_targetCountryName) {
        Country l_sourceCountry = p_gameState.getD_map().getCountryByName(p_sourceCountryName);
        Country l_targetCountry = p_gameState.getD_map().getCountryByName(p_targetCountryName);
        Integer l_targetCountryId = l_sourceCountry.getD_adjacentCountryIds().stream()
                .filter(l_adjCountry -> l_adjCountry == l_targetCountry.getD_countryId()).findFirst().orElse(null);
        if (l_targetCountryId == null) {
            this.setD_playerLog("Advance order cant be issued since target country : " + p_targetCountryName
                    + " is not adjacent to source country : " + p_sourceCountryName, "error");
            return false;
        }
        return true;
    }


    /**
     * Assigns a card to the player as a reward for successful conquest if the player hasn't already earned the maximum cards allowed per turn.
     * Adds the card to the player's list of owned cards and sets the appropriate log message.
     * Marks that the player has earned a card for this turn.
     */
    public void assignCard() {
        if (!d_oneCardPerTurn) {
            Random l_random = new Random();
            this.d_cardsOwnedByPlayer.add(ApplicationConstants.CARDS.get(l_random.nextInt(ApplicationConstants.SIZE)));
            this.setD_playerLog("Player: "+ this.d_name+ " has earned card as reward for the successful conquest- " + this.d_cardsOwnedByPlayer.get(this.d_cardsOwnedByPlayer.size()-1), "log");
            this.setD_oneCardPerTurn(true);
        }else{
            this.setD_playerLog("Player: "+this.d_name+ " has already earned maximum cards that can be allotted in a turn", "error");
        }
    }

    /**
     * Removes the specified card from the player's list of owned cards.
     *
     * @param p_cardName The name of the card to remove.
     */
    public void removeCard(String p_cardName){
        this.d_cardsOwnedByPlayer.remove(p_cardName);
    }

    /**
     * Validates if the player can attack the target country based on previous negotiations.
     *
     * @param p_targetCountryName The name of the target country.
     * @return True if the player can attack the target country, false otherwise.
     */
    public boolean negotiationValidation(String p_targetCountryName){
        boolean l_canAttack = true;
        for(Player p: d_negotiatedWith){
            if (p.getCountryNames().contains(p_targetCountryName))
                l_canAttack = false;
        }
        return l_canAttack;
    }

    /**
     * Resets the list of players negotiated with by this player.
     * Clears the list of negotiated players.
     */
    public void resetNegotiation(){
        d_negotiatedWith.clear();
    }

    /**
     * Checks if the arguments provided in the card command are valid based on the command type.
     * For "airlift" command, expects 4 arguments.
     * For "blockade", "bomb", or "negotiate" commands, expects 2 arguments.
     *
     * @param p_commandEntered The command entered by the player.
     * @return True if the arguments are valid for the specified command, false otherwise.
     */
    public boolean checkCardArguments(String p_commandEntered){
        if(p_commandEntered.split(" ")[0].equalsIgnoreCase("airlift")) {
            return p_commandEntered.split(" ").length == 4;
        } else if (p_commandEntered.split(" ")[0].equalsIgnoreCase("blockade")
                || p_commandEntered.split(" ")[0].equalsIgnoreCase("bomb")
                || p_commandEntered.split(" ")[0].equalsIgnoreCase("negotiate")) {
            return p_commandEntered.split(" ").length == 2;
        } else {
            return false;
        }
    }

    /**
     * Handles the execution of card commands based on the provided command and game state.
     * Parses the command and creates the corresponding card order.
     * Validates the order and adds it to the order list if valid.
     * Updates the player's log and the game state log accordingly.
     *
     * @param p_commandEntered The command entered by the player.
     * @param p_gameState The current state of the game.
     */
    public void handleCardCommands(String p_commandEntered, GameState p_gameState) {
        if (checkCardArguments(p_commandEntered)) {
            switch (p_commandEntered.split(" ")[0]) {
                case "airlift":
                    Card l_newOrder = new Airlift(p_commandEntered.split(" ")[1], p_commandEntered.split(" ")[2],
                            Integer.parseInt(p_commandEntered.split(" ")[3]), this);
                    if (l_newOrder.checkValidOrder(p_gameState)) {
                        this.order_list.add(l_newOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!", "log");
                        p_gameState.updateLog(getD_playerLog(), "effect");
                    }
                    break;
                case "blockade":
                    Card l_blockadeOrder = new Blockade(this, p_commandEntered.split(" ")[1]);
                    if (l_blockadeOrder.checkValidOrder(p_gameState)) {
                        this.order_list.add(l_blockadeOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!", "log");
                        p_gameState.updateLog(getD_playerLog(), "effect");
                    }
                    break;
                case "bomb":
                    Card l_bombOrder = new Bomb(this, p_commandEntered.split(" ")[1]);
                    if (l_bombOrder.checkValidOrder(p_gameState)) {
                        this.order_list.add(l_bombOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!", "log");
                        p_gameState.updateLog(getD_playerLog(), "effect");
                    }
                    break;
                case "negotiate":
                    Card l_negotiateOrder = new Diplomacy(p_commandEntered.split(" ")[1],this);
                    if (l_negotiateOrder.checkValidOrder(p_gameState)) {
                        this.order_list.add(l_negotiateOrder);
                        this.setD_playerLog("Card Command Added to Queue for Execution Successfully!", "log");
                        p_gameState.updateLog(getD_playerLog(), "effect");
                    }
                    break;
                default:
                    this.setD_playerLog("Invalid Command!", "error");
                    p_gameState.updateLog(getD_playerLog(), "effect");
                    break;
            }
        } else{
            this.setD_playerLog("Invalid Card Command Passed! Check Arguments!", "error");
        }
    }
}
