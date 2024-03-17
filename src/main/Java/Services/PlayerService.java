package Services;

import Constants.ApplicationConstants;
import Models.*;
import Utils.CommonUtil;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * The PlayerService class provides services related to players in the game.
 * It includes operations such as adding or removing players, assigning colors, countries, and continents,
 * calculating armies, checking for unexecuted orders or unassigned armies, and updating player information.
 */
public class PlayerService {

    /** The log message for player operations. */
    String d_playerLog;

    /** The log message for country/continent assignment. */
    String d_assignmentLog = "Country/Continent Assignment:";


    /**
     * Checks if a player name is unique among the existing players.
     *
     * @param p_existingPlayerList The list of existing players to check against.
     * @param p_playerName         The player name to be checked for uniqueness.
     * @return                     true if the player name is unique, false otherwise.
     */
    public boolean isPlayerNameUnique(List<Player> p_existingPlayerList, String p_playerName)
    {
        boolean l_isUnique = true;
        if (!CommonUtil.isCollectionEmpty(p_existingPlayerList))
        {
            for (Player l_player : p_existingPlayerList)
            {
                if (l_player.getPlayerName().equalsIgnoreCase(p_playerName))
                {
                    l_isUnique = false;
                    break;
                }
            }
        }
        return l_isUnique;
    }
    /**
     * Adds or removes players from the existing player list based on the specified operation and argument.
     *
     * @param p_existingPlayerList The list of existing players.
     * @param p_operation          The operation to perform: "add" to add a player, "remove" to remove a player.
     * @param p_argument           The argument specifying the player name to be added or removed.
     *                             For "add" operation: "<player_name>"
     *                             For "remove" operation: "<player_name>"
     * @return                     The updated list of players after performing the add or remove operation.
     */
    public List<Player> addRemovePlayers(List<Player> p_existingPlayerList, String p_operation, String p_argument)
    {
        List<Player> l_updatedPlayers = new ArrayList<>();
        if (!CommonUtil.isCollectionEmpty(p_existingPlayerList))
            l_updatedPlayers.addAll(p_existingPlayerList);

        String l_enteredPlayerName = p_argument.split(" ")[0];
        boolean l_playerNameAlreadyExist = !isPlayerNameUnique(p_existingPlayerList, l_enteredPlayerName);

        switch (p_operation.toLowerCase())
        {
            case "add":
                addGamePlayer(l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExist);
                break;
            case "remove":
                removeGamePlayer(p_existingPlayerList, l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExist);
                break;
            default:
                setD_playerLog("Unrecognized action on the Players list.");
        }
        return l_updatedPlayers;
    }
    /**
     * Removes a player from the updated player list if the player name already exists in the existing player list.
     *
     * @param p_existingPlayerList   The list of existing players to check for the player to be removed.
     * @param p_updatedPlayers       The updated list of players from which the player will be removed.
     * @param p_enteredPlayerName    The name of the player to be removed.
     * @param p_playerNameAlreadyExist   A flag indicating whether the player name already exists in the existing player list.
     */
    private void removeGamePlayer(List<Player> p_existingPlayerList, List<Player> p_updatedPlayers, String p_enteredPlayerName, boolean p_playerNameAlreadyExist)
    {
        if (p_playerNameAlreadyExist)
        {
            for (Player l_player : p_existingPlayerList)
            {
                if (l_player.getPlayerName().equalsIgnoreCase(p_enteredPlayerName))
                {
                    p_updatedPlayers.remove(l_player);
                    setD_playerLog("Player : " + p_enteredPlayerName + " has been successfully removed from the list");
                }
            }
        } else {
            setD_playerLog("Player : " + p_enteredPlayerName + " does not Exist. No changes made so far.");
        }
    }
    /**
     * Adds a player to the updated player list if the player name does not already exist.
     *
     * @param p_updatedPlayers       The updated list of players to which the player will be added.
     * @param p_enteredPlayerName    The name of the player to be added.
     * @param p_playerNameAlreadyExist   A flag indicating whether the player name already exists in the existing player list.
     */
    private void addGamePlayer(List<Player> p_updatedPlayers, String p_enteredPlayerName, boolean p_playerNameAlreadyExist)
    {
        if (p_playerNameAlreadyExist)
        {
            setD_playerLog("Player : " + p_enteredPlayerName + " already exists. No changes made.");
        }
        else
        {
            Player l_addNewPlayer = new Player(p_enteredPlayerName);
            p_updatedPlayers.add(l_addNewPlayer);
            setD_playerLog("Player : " + p_enteredPlayerName + " has been added to the list successfully.");
        }
    }
    /**
     * Checks if players are available in the game state for assigning countries.
     *
     * @param p_gameState The game state containing the list of players.
     * @return            true if players are available, false otherwise.
     */
    public boolean checkPlayersAvailability(GameState p_gameState)
    {
        if (p_gameState.getD_players() == null || p_gameState.getD_players().isEmpty()) {

            return false;
        }
        return true;
    }
    /**
     * Assigns colors to players in the game state based on a predefined list of colors.
     * Each player is assigned a unique color from the list.
     *
     * @param p_gameState The game state containing the list of players to assign colors to.
     */
    public void assignColors(GameState p_gameState)
    {
        if (!checkPlayersAvailability(p_gameState)) return;

        List<Player> l_players = p_gameState.getD_players();

        for(int i = 0; i< l_players.size(); i++)
            {
            l_players.get(i).setD_color(ApplicationConstants.COLORS.get(i));
        }
    }
    /**
     * Assigns countries to players in the game state. Each player is assigned a set number of countries
     * based on the total number of countries available and the number of players.
     * Additionally, assigns continents to players based on the countries they own.
     *
     * @param p_gameState The game state containing the map and the list of players.
     */
    public void assignCountries(GameState p_gameState)
    {
        if (!checkPlayersAvailability(p_gameState)) {
            p_gameState.updateLog("Before assigning countries, players should be added.", "effect");
            return;
        }

        List<Country> l_countries = p_gameState.getD_map().getD_countries();

        int l_playerSize = p_gameState.getD_players().size();
        Player l_neutralPlayer = p_gameState.getD_players().stream()
                .filter(l_player -> l_player.getPlayerName().equalsIgnoreCase("Neutral")).findFirst().orElse(null);
        if (l_neutralPlayer != null)
            l_playerSize = l_playerSize - 1;

        int l_countriesPerPlayer = Math.floorDiv(l_countries.size(), p_gameState.getD_players().size());

        this.performRandomCountryAssignment(l_countriesPerPlayer, l_countries, p_gameState.getD_players(), p_gameState);
        this.performContinentAssignment(p_gameState.getD_players(), p_gameState.getD_map().getD_continents());
        p_gameState.updateLog(d_assignmentLog, "effect");
        System.out.println("Countries have been assigned to Players.");

    }
    /**
     * Performs random assignment of countries to players.
     * Each player is assigned a specified number of countries randomly selected from the list of unassigned countries.
     *
     * @param p_countriesPerPlayer The number of countries to be assigned to each player.
     * @param p_countries          The list of countries available for assignment.
     * @param p_players            The list of players to whom countries will be assigned.
     */
    private void performRandomCountryAssignment(int p_countriesPerPlayer, List<Country> p_countries, List<Player> p_players, GameState p_gameState)
    {
        List<Country> l_unassignedCountries = new ArrayList<>(p_countries);
        for (Player l_pl : p_players) {
            if(!l_pl.getPlayerName().equalsIgnoreCase("Neutral")) {
            if (l_unassignedCountries.isEmpty())
                break;
            for (int i = 0; i < p_countriesPerPlayer; i++)
            {
                Random l_random = new Random();
                int l_randomIndex = l_random.nextInt(l_unassignedCountries.size());
                Country l_randomCountry = l_unassignedCountries.get(l_randomIndex);

                if (l_pl.getD_coutriesOwned() == null)
                    l_pl.setD_coutriesOwned(new ArrayList<>());
                l_pl.getD_coutriesOwned().add(p_gameState.getD_map().getCountryByName(l_randomCountry.getD_countryName()));
                System.out.println("Player : " + l_pl.getPlayerName() + " is assigned with Country : " + l_randomCountry.getD_countryName());
                d_assignmentLog += "\n Player : " + l_pl.getPlayerName() + " is assigned with country : "
                        + l_randomCountry.getD_countryName();
                l_unassignedCountries.remove(l_randomCountry);
            }
            }
        }
        if (!l_unassignedCountries.isEmpty())
        {
            performRandomCountryAssignment(1, l_unassignedCountries, p_players,p_gameState);
        }
    }
    /**
     * Performs assignment of continents to players based on the countries they own.
     *
     * @param p_players    The list of players to whom continents will be assigned.
     * @param p_continents The list of continents available for assignment.
     */
    public void performContinentAssignment(List<Player> p_players, List<Continent> p_continents)
    {
        for (Player l_pl : p_players)
        {
            List<String> l_countriesOwned = new ArrayList<>();
            if (!CommonUtil.isCollectionEmpty(l_pl.getD_coutriesOwned()))
            {
                l_pl.getD_coutriesOwned().forEach(l_country -> l_countriesOwned.add(l_country.getD_countryName()));

                for (Continent l_cont : p_continents)
                {
                    List<String> l_countriesOfContinent = new ArrayList<>();
                    l_cont.getD_countries().forEach(l_count -> l_countriesOfContinent.add(l_count.getD_countryName()));
                    if (l_countriesOwned.containsAll(l_countriesOfContinent))
                    {
                        if (l_pl.getD_continentsOwned() == null)
                            l_pl.setD_continentsOwned(new ArrayList<>());

                        l_pl.getD_continentsOwned().add(l_cont);
                        System.out.println("Player : " + l_pl.getPlayerName() + " is assigned with Continent : "
                                + l_cont.getD_continentName());
                        d_assignmentLog += "\n Player : " + l_pl.getPlayerName() + " is assigned with continent : "
                                + l_cont.getD_continentName();
                    }
                }
            }
        }
    }

    /**
     * Calculates the total number of armies available for deployment for a player.
     * The calculation includes base armies based on the number of countries owned by the player
     * and additional armies based on the continents owned by the player.
     *
     * @param p_player The player for whom the armies are calculated.
     * @return The total number of armies available for deployment for the player.
     */
    public Integer calculateArmiesForPlayer(Player p_player)
    {
        Integer l_armies = null != p_player.getD_noOfUnallocatedArmies() ? p_player.getD_noOfUnallocatedArmies() : 0;
        if (!CommonUtil.isCollectionEmpty(p_player.getD_coutriesOwned()))
        {
            l_armies = l_armies + Math.max(3, Math.round((p_player.getD_coutriesOwned().size()) / 3));
        }
        if (!CommonUtil.isCollectionEmpty(p_player.getD_continentsOwned()))
        {
            int l_continentCtrlValue = 0;
            for (Continent l_continent : p_player.getD_continentsOwned())
            {
                l_continentCtrlValue = l_continentCtrlValue + l_continent.getD_continentValue();
            }
            l_armies = l_armies + l_continentCtrlValue;
        }
        return l_armies;
    }
    /**
     * Assigns armies to each player based on the number of countries and continents they own.
     * The number of armies assigned to each player is calculated using the calculateArmiesForPlayer method.
     *
     * @param p_gameState The current game state containing information about players, countries, and continents.
     */
    public void assignArmies(GameState p_gameState) {
        for (Player l_pl : p_gameState.getD_players()) {
            Integer l_armies = this.calculateArmiesForPlayer(l_pl);
            this.setD_playerLog("Player : " + l_pl.getPlayerName() + " has been assigned with " + l_armies + " armies");
            p_gameState.updateLog(this.d_playerLog, "effect");

            l_pl.setD_noOfUnallocatedArmies(l_armies);
        }
    }
    /**
     * Checks if there are unexecuted orders for any player in the given list of players.
     *
     * @param p_playersList The list of players to check for unexecuted orders.
     * @return true if there are unexecuted orders for any player, false otherwise.
     */
    public boolean unexecutedOrdersExists(List<Player> p_playersList) {
        int l_totalUnexecutedOrders = 0;
        for (Player l_player : p_playersList) {
            l_totalUnexecutedOrders = l_totalUnexecutedOrders + l_player.getD_ordersToExecute().size();
        }
        return l_totalUnexecutedOrders != 0;
    }
    /**
     * Checks if there are unassigned armies for any player in the given list of players.
     *
     * @param p_playersList The list of players to check for unassigned armies.
     * @return true if there are unassigned armies for any player, false otherwise.
     */
    public boolean unassignedArmiesExists(List<Player> p_playersList) {
        int l_unassignedArmies = 0;
        for (Player l_player : p_playersList) {
            l_unassignedArmies = l_unassignedArmies + l_player.getD_noOfUnallocatedArmies();
        }
        return l_unassignedArmies != 0;
    }
    /**
     * Updates the list of players in the game state based on the specified operation and argument.
     *
     * @param p_gameState   The current game state containing information about players and the map.
     * @param p_operation   The operation to perform on the players list (e.g., "add" or "remove").
     * @param p_argument    The argument specifying the player name for addition or removal.
     */
    public void updatePlayers(GameState p_gameState, String p_operation, String p_argument) {
        if (!isMapLoaded(p_gameState)) {
            this.setD_playerLog("Please ensure the map is loaded prior to adding a player : " + p_argument);
            p_gameState.updateLog(this.d_playerLog, "effect");
            return;
        }
        List<Player> l_updatedPlayers = this.addRemovePlayers(p_gameState.getD_players(), p_operation, p_argument);

        if (!CommonUtil.isNull(l_updatedPlayers)) {
            p_gameState.setD_players(l_updatedPlayers);
            p_gameState.updateLog(d_playerLog, "effect");
        }
    }
    /**
     * Checks if the map is loaded in the given game state.
     *
     * @param p_gameState The game state to check for the loaded map.
     * @return true if the map is loaded, false otherwise.
     */
    public boolean isMapLoaded(GameState p_gameState) {
        return !CommonUtil.isNull(p_gameState.getD_map()) ? true : false;
    }

    /**
     * Checks if there are more orders to execute for any player in the given list of players.
     *
     * @param p_playersList The list of players to check for more orders.
     * @return true if there are more orders to execute for any player, false otherwise.
     */
    public boolean checkForMoreOrders(List<Player> p_playersList) {
        for (Player l_player : p_playersList) {
            if(l_player.getD_moreOrders())
                return true;
        }
        return false;
    }

    /**
     * Resets certain flags and states for all players in the given list of players.
     *
     * @param p_playersList The list of players to reset flags for.
     */
    public void resetPlayersFlag(List<Player> p_playersList) {
        for (Player l_player : p_playersList) {
            if (!l_player.getPlayerName().equalsIgnoreCase("Neutral"))
                l_player.setD_moreOrders(true);
            l_player.setD_oneCardPerTurn(false);
            l_player.resetNegotiation();
        }
    }

    /**
     * Sets the player log message.
     *
     * @param p_playerLog The player log message to be set.
     */
    public void setD_playerLog(String p_playerLog) {
        this.d_playerLog = p_playerLog;
        System.out.println(p_playerLog);
    }

    /**
     * Finds and returns a player by their name from the given game state.
     *
     * @param p_playerName The name of the player to find.
     * @param p_gameState  The game state containing the list of players.
     * @return The player object if found, null otherwise.
     */
    public Player findPlayerByName(String p_playerName, GameState p_gameState) {
        return p_gameState.getD_players().stream().filter(l_player -> l_player.getPlayerName().equals(p_playerName)).findFirst().orElse(null);
    }
}
