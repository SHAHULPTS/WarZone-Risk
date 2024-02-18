package Services;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import Exceptions.InvalidCommand;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Models.Player;
import Utils.CommonUtil;

/**
 * Javadoc for the PlayerServiceTest class.
 * This class contains test cases for the PlayerService class, covering various functionalities related to player management and game state.
 */
public class PlayerServiceTest {

    Player d_playerInfo;     /** Player information used in the test cases. */

    PlayerService d_playerService;     /** Instance of PlayerService used to test player-related functionalities. */

    Map d_map;     /** Map used in the test cases. */

    GameState d_gameState;     /** GameState used in the test cases. */

    MapService d_mapservice;    /** Instance of MapService used in the test cases. */

    /** List of existing players used in the test cases. */

    List<Player> d_exisitingPlayerList = new ArrayList<>();
    /** Output stream used for capturing console output during tests. */

    private final ByteArrayOutputStream d_outContent = new ByteArrayOutputStream();

    /**
     * Sets up the necessary objects and data structures before each test method execution.
     * Initializes a new player object, a player service object, a game state object, and a list of existing players.
     * Existing players "Jay" and "Nidhi" are added to the list.
     */
    @Before
    public void setup() {
        d_playerInfo = new Player();
        d_playerService = new PlayerService();
        d_gameState = new GameState();
        d_exisitingPlayerList.add(new Player("Jay"));
        d_exisitingPlayerList.add(new Player("Nidhi"));

    }
    /**
     * Tests the addition of players using the addRemovePlayers method in the PlayerService class.
     * It checks whether the existing player list is not empty, then adds a new player "Vinisha" to the list.
     * Verifies if the new player "Vinisha" is successfully added to the updated player list.
     * Additionally, it tests the scenario where attempting to add an existing player "Jay" results in no changes.
     */
    @org.junit.Test
    public void testAddPlayers() {
        assertFalse(CommonUtil.isCollectionEmpty(d_exisitingPlayerList));
        List<Player> l_updatedPlayers = d_playerService.addRemovePlayers(d_exisitingPlayerList, "add", "Vinisha");
        assertEquals("Vinisha", l_updatedPlayers.get(2).getPlayerName());

        System.setOut(new PrintStream(d_outContent));
        d_playerService.addRemovePlayers(d_exisitingPlayerList, "add", "Jay");
        assertEquals("Player with name : Jay already Exists. Changes are not made.", d_outContent.toString());
    }
    /**
     * Tests the removal of players using the addRemovePlayers method in the PlayerService class.
     * It removes the player "Jay" from the existing player list and verifies if the player is successfully removed.
     * Additionally, it tests the scenario where attempting to remove a non-existing player "JaySurya" results in no changes.
     */
    @org.junit.Test
    public void testRemovePlayers() {
        List<Player> l_updatedPlayers = d_playerService.addRemovePlayers(d_exisitingPlayerList, "remove", "Jay");
        assertEquals(1, l_updatedPlayers.size());

        System.setOut(new PrintStream(d_outContent));
        d_playerService.addRemovePlayers(d_exisitingPlayerList, "remove", "JaySurya");
        assertEquals("Player with name : JaySurya does not Exist. Changes are not made.", d_outContent.toString());
    }
    /**
     * Tests the checkPlayersAvailability method in the PlayerService class.
     * It checks whether players exist in the game state and verifies that the method returns false when there are no players.
     */
    @org.junit.Test
    public void testPlayersAvailability() {
        boolean l_playersExists = d_playerService.checkPlayersAvailability(d_gameState);
        assertFalse(l_playersExists);
    }
    /**
     * Tests the playerCountryAssignment method in the PlayerService class.
     * It verifies that countries are assigned to players correctly.
     * This test loads a map, sets existing players, assigns countries to players,
     * and then checks if each player has a non-null list of countries owned and
     * if the total number of assigned countries matches the total number of countries in the map.
     */
    @org.junit.Test
    public void testPlayerCountryAssignment() {
        d_mapservice = new MapService();
        d_map = new Map();
        d_map = d_mapservice.loadMap(d_gameState, "canada.map");
        d_gameState.setD_map(d_map);
        d_gameState.setD_players(d_exisitingPlayerList);
        d_playerService.assignCountries(d_gameState);

        int l_assignedCountriesSize = 0;
        for (Player l_pl : d_gameState.getD_players()) {
            assertNotNull(l_pl.getD_coutriesOwned());
            l_assignedCountriesSize = l_assignedCountriesSize + l_pl.getD_coutriesOwned().size();
        }
        assertEquals(l_assignedCountriesSize, d_gameState.getD_map().getD_countries().size());
    }

    /**
     * Tests the calculateArmiesForPlayer method in the PlayerService class.
     * It verifies that the correct number of armies is calculated for a player based on owned countries and continents.
     * This test creates a player with a list of owned countries and continents,
     * sets the number of unallocated armies, calculates the armies for the player,
     * and then compares the actual result with the expected result.
     */
    @org.junit.Test
    public void testCalculateArmiesForPlayer() {
        Player l_playerInfo = new Player();
        List<Country> l_countryList = new ArrayList<Country>();
        l_countryList.add(new Country("Kidrar"));
        l_countryList.add(new Country("Koptan"));
        l_countryList.add(new Country("Opering"));
        l_countryList.add(new Country("Jindre"));
        l_playerInfo.setD_coutriesOwned(l_countryList);
        List<Continent> l_continentList = new ArrayList<Continent>();
        l_continentList.add(new Continent(1, "Froing", 5));
        l_playerInfo.setD_continentsOwned(l_continentList);
        l_playerInfo.setD_noOfUnallocatedArmies(10);
        Integer l_actualResult = d_playerService.calculateArmiesForPlayer(l_playerInfo);
        Integer l_expectedresult = 8;
        assertEquals(l_expectedresult, l_actualResult);
    }
    /**
     * Tests the validateDeployOrderArmies method in the PlayerService class.
     * It verifies whether the deployment order involving a certain number of armies is valid for a player.
     * This test sets the number of unallocated armies for a player,
     * provides a number of armies for deployment, and then checks if the order is valid.
     * The expected result is that the deployment order should be valid since the number of armies requested for deployment
     * is less than or equal to the number of unallocated armies of the player.
     */
    @org.junit.Test
    public void testValidateDeployOrderArmies() {
        d_playerInfo.setD_noOfUnallocatedArmies(10);
        String l_noOfArmies = "4";
        boolean l_bool = d_playerService.validateDeployOrderArmies(d_playerInfo, l_noOfArmies);
        assertFalse(l_bool);

    }
    /**
     * Tests the createDeployOrder method in the PlayerService class.
     * It verifies the creation of a deployment order for a player.
     * This test sets up a player with unallocated armies and a country owned by the player.
     * It then creates a deployment order for a specified number of armies to be deployed to a country.
     * The test checks if the order is created correctly by verifying the player's remaining unallocated armies,
     * the number of orders in the player's order list, the target country of the order, and the number of armies to be deployed.
     * The expected result is that the deployment order is created successfully with the specified parameters.
     *
     * @throws InvalidCommand if the command provided for creating the deployment order is invalid
     */
    @org.junit.Test
    public void testDeployOrder() throws InvalidCommand {
        Player l_player = new Player("Maze");
        l_player.setD_noOfUnallocatedArmies(10);
        Country l_country = new Country(1, "Japan", 1);
        l_player.setD_coutriesOwned(Arrays.asList(l_country));
        d_playerService.createDeployOrder("deploy Japan 4", l_player);

        assertEquals(l_player.getD_noOfUnallocatedArmies().toString(), "6");
        assertEquals(l_player.getD_ordersToExecute().size(), 1);
        assertEquals(l_player.getD_ordersToExecute().get(0).getD_targetCountryName(), "Japan");
        assertEquals(l_player.getD_ordersToExecute().get(0).getD_numberOfArmiesToPlace().toString(), "4");
    }
    /**
     * This method tests the functionality to check if player names are unique.
     */
    @Test
    void isPlayerNameUnique() {
    }

    /**
     * This method tests the functionality to add or remove players from the player list.
     */
    @Test
    void addRemovePlayers() {
    }
    /**
     * This method tests the functionality to check the availability of players.
     */
    @Test
    void checkPlayersAvailability() {
    }
    /**
     * This method tests the functionality to assign colors to players.
     */
    @Test
    void assignColors() {
    }
    /**
     * This method tests the functionality to assign countries to players.
     */
    @Test
    void assignCountries() {
    }

    /**
     * This method tests the functionality to create deployment orders for players.
     */
    @Test
    void createDeployOrder() {
    }

    /**
     * This method tests the functionality to validate the number of armies in a deployment order.
     */
    @Test
    void validateDeployOrderArmies() {
    }

    /**
     * This method tests the functionality to calculate the number of armies for a player based on owned countries and continents.
     */
    @Test
    void calculateArmiesForPlayer() {
    }

    /**
     * This method tests the functionality to assign armies to players.
     */
    @Test
    void assignArmies() {
    }

    /**
     * This method tests the functionality to check if unexecuted orders exist for players.
     */
    @Test
    void unexecutedOrdersExists() {
    }

    /**
     * This method tests the functionality to check if unassigned armies exist for players.
     */
    @Test
    void unassignedArmiesExists() {
    }
    /**
     * This method tests the functionality to update player information.
     */
    @Test
    void updatePlayers() {
    }
    /**
     * This method tests the functionality to check if a map is loaded.
     */
    @Test
    void isMapLoaded() {
    }
}