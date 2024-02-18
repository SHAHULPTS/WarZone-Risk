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
import static org.junit.jupiter.api.Assertions.*;

public class PlayerServiceTest {

    Player d_playerInfo;

    PlayerService d_playerService;

    Map d_map;

    GameState d_gameState;

    MapService d_mapservice;

    List<Player> d_exisitingPlayerList = new ArrayList<>();

    private final ByteArrayOutputStream d_outContent = new ByteArrayOutputStream();


    @Before
    public void setup() {
        d_playerInfo = new Player();
        d_playerService = new PlayerService();
        d_gameState = new GameState();
        d_exisitingPlayerList.add(new Player("Jay"));
        d_exisitingPlayerList.add(new Player("Nidhi"));

    }

    @org.junit.Test
    public void testAddPlayers() {
        assertFalse(CommonUtil.isCollectionEmpty(d_exisitingPlayerList));
        List<Player> l_updatedPlayers = d_playerService.addRemovePlayers(d_exisitingPlayerList, "add", "Vinisha");
        assertEquals("Vinisha", l_updatedPlayers.get(2).getPlayerName());

        System.setOut(new PrintStream(d_outContent));
        d_playerService.addRemovePlayers(d_exisitingPlayerList, "add", "Jay");
        assertEquals("Player with name : Jay already Exists. Changes are not made.", d_outContent.toString());
    }

    @org.junit.Test
    public void testRemovePlayers() {
        List<Player> l_updatedPlayers = d_playerService.addRemovePlayers(d_exisitingPlayerList, "remove", "Jay");
        assertEquals(1, l_updatedPlayers.size());

        System.setOut(new PrintStream(d_outContent));
        d_playerService.addRemovePlayers(d_exisitingPlayerList, "remove", "JaySurya");
        assertEquals("Player with name : JaySurya does not Exist. Changes are not made.", d_outContent.toString());
    }

    @org.junit.Test
    public void testPlayersAvailability() {
        boolean l_playersExists = d_playerService.checkPlayersAvailability(d_gameState);
        assertFalse(l_playersExists);
    }

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

    @org.junit.Test
    public void testValidateDeployOrderArmies() {
        d_playerInfo.setD_noOfUnallocatedArmies(10);
        String l_noOfArmies = "4";
        boolean l_bool = d_playerService.validateDeployOrderArmies(d_playerInfo, l_noOfArmies);
        assertFalse(l_bool);

    }

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

    @Test
    void isPlayerNameUnique() {
    }

    @Test
    void addRemovePlayers() {
    }

    @Test
    void checkPlayersAvailability() {
    }

    @Test
    void assignColors() {
    }

    @Test
    void assignCountries() {
    }

    @Test
    void createDeployOrder() {
    }

    @Test
    void validateDeployOrderArmies() {
    }

    @Test
    void calculateArmiesForPlayer() {
    }

    @Test
    void assignArmies() {
    }

    @Test
    void unexecutedOrdersExists() {
    }

    @Test
    void unassignedArmiesExists() {
    }

    @Test
    void updatePlayers() {
    }

    @Test
    void isMapLoaded() {
    }
}