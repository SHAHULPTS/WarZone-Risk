package Models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import Utils.CommonUtil;

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

    public Player(String p_playerName) {
        this.d_name = p_playerName;
        this.d_noOfUnallocatedArmies = 0;
        this.d_coutriesOwned = new ArrayList<Country>();
        this.order_list = new ArrayList<Order>();
        this.d_moreOrders = true;
    }

    public Player() {

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
        return order_list;
    }

    public void setD_ordersToExecute(List<Order> p_ordersToExecute) {
        this.order_list = p_ordersToExecute;
    }

    public Integer getD_noOfUnallocatedArmies() {
        return d_noOfUnallocatedArmies;
    }

    public void setD_noOfUnallocatedArmies(Integer p_numberOfArmies) {
        this.d_noOfUnallocatedArmies = p_numberOfArmies;
    }

    public void addPlayerNegotiation(Player p_playerNegotiation) {
        this.d_negotiatedWith.add(p_playerNegotiation);
    }

    public boolean getD_moreOrders() {
        return d_moreOrders;
    }

    public void setD_moreOrders(boolean p_moreOrders) {
        this.d_moreOrders = p_moreOrders;
    }

    public List<String> getD_cardsOwnedByPlayer(){ return this.d_cardsOwnedByPlayer; }

    public void setD_oneCardPerTurn(Boolean p_value){
        this.d_oneCardPerTurn = p_value;
    }

    public List<String> getCountryNames() {
        List<String> l_countryNames = new ArrayList<String>();
        for (Country c : d_coutriesOwned) {
            l_countryNames.add(c.getD_countryName());
        }
        return l_countryNames;
    }

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
    public void setD_playerLog(String p_playerLog, String p_typeLog) {
        this.d_playerLog = p_playerLog;
        if(p_typeLog.equals("error"))
            System.err.println(p_playerLog);
        else if(p_typeLog.equals("log"))
            System.out.println(p_playerLog);
    }

    public String getD_playerLog(){
        return this.d_playerLog;
    }

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
    public boolean validateDeployOrderArmies(Player p_player, String p_noOfArmies) {
        return p_player.getD_noOfUnallocatedArmies() < Integer.parseInt(p_noOfArmies) ? true : false;
    }

    public Order next_order() {
        if (CommonUtil.isCollectionEmpty(this.order_list)) {
            return null;
        }
        Order l_order = this.order_list.get(0);
        this.order_list.remove(l_order);
        return l_order;
    }

    private Boolean checkCountryExists(String p_countryName, GameState p_gameState) {
        if (p_gameState.getD_map().getCountryByName(p_countryName) == null) {
            this.setD_playerLog("Country : " + p_countryName
                    + " given in advance order doesn't exists in map. Order given is ignored.", "error");
            return false;
        }
        return true;
    }

    private Boolean checkZeroArmiesInOrder(String p_noOfArmies) {
        if (Integer.parseInt(p_noOfArmies) == 0) {
            this.setD_playerLog("Advance order with 0 armies to move cant be issued.", "error");
            return true;
        }
        return false;
    }

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

    public void removeCard(String p_cardName){
        this.d_cardsOwnedByPlayer.remove(p_cardName);
    }

    public boolean negotiationValidation(String p_targetCountryName){
        boolean l_canAttack = true;
        for(Player p: d_negotiatedWith){
            if (p.getCountryNames().contains(p_targetCountryName))
                l_canAttack = false;
        }
        return l_canAttack;
    }

    public void resetNegotiation(){
        d_negotiatedWith.clear();
    }

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
}
