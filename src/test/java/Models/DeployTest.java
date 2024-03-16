package Models;

import Exceptions.InvalidCommand;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used to test functionality of Deploy order.
 */
public class DeployTest {

    /**
     * First Player
     */
    Player d_player1;

    /**
     * Second Player
     */
    Player d_player2;

    /**
     * First Deploy Order.
     */
    Deploy d_deployOrder1;

    /**
     * Second Deploy Order.
     */
    Deploy d_deployOrder2;

    /**
     * Game State.
     */
    GameState d_gameState = new GameState();
    /**
     * Initializes the environment for deploy order tests by setting up players, countries, and deploy orders.
     * It creates two players, Nidhi and Vinisha, and assigns them ownership of the countries "India" and "Canada".
     * This setup mirrors a scenario where both players share ownership over these countries for the sake of testing.
     *
     * The method also configures a map containing "Canada" and "India", with "India" initially having 5 armies.
     * Deploy orders are then created for both players: Nidhi deploying to "India" and Vinisha to "Canada", with
     * specified numbers of armies. This setup aims to test the correct functionality of deploy orders within the
     * game, ensuring that armies are accurately added to the respective countries in the game state.
     */

    @Before
    public void setup() {
        d_player1 = new Player();
        d_player1.setPlayerName("Nidhi");

        d_player2 = new Player();
        d_player2.setPlayerName("Vinisha");

        List<Country> l_countryList = new ArrayList<Country>();
        l_countryList.add(new Country("India"));
        l_countryList.add(new Country("Canada"));
        d_player1.setD_coutriesOwned(l_countryList);
        d_player2.setD_coutriesOwned(l_countryList);

        List<Country> l_mapCountries = new ArrayList<Country>();
        Country l_country1 = new Country(1, "Canada", 1);
        Country l_country2 = new Country(2, "India", 2);
        l_country2.setD_armies(5);

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);

        Map l_map = new Map();
        l_map.setD_countries(l_mapCountries);
        d_gameState.setD_map(l_map);

        d_deployOrder1 = new Deploy(d_player1, "India", 5);
        d_deployOrder2 = new Deploy(d_player2, "Canada", 15);
    }

    /**
     * Tests the validity of deploy orders for both players in the game state to ensure that deploy orders
     * are considered valid when the target country is owned by the deploying player. This method checks
     * the validity of deploy orders created during the setup phase for two scenarios:
     *
     * 1. A deploy order by player Nidhi targeting "India", a country they own.
     * 2. A deploy order by player Vinisha targeting "Canada", a country they own.
     *
     * The test asserts that both deploy orders are valid based on the ownership of the target countries,
     * ensuring that the game correctly recognizes valid deploy actions according to the current game state
     * and ownership rules.
     */

    @Test
    public void testValidateDeployOrderCountry() {
        boolean l_actualBoolean = d_deployOrder1.valid(d_gameState);
        assertTrue(l_actualBoolean);
        boolean l_actualBoolean2 = d_deployOrder2.valid(d_gameState);
        assertTrue(l_actualBoolean2);
    }

    /**
     * Tests the execution of deploy orders to verify that the correct number of armies are added to
     * the specified countries. This method executes two deploy orders:
     *
     * 1. A deploy order for player Nidhi to deploy 5 armies to "India". Before the order, "India"
     *    has 5 armies, and after successful execution, it is expected to have 10 armies.
     * 2. A deploy order for player Vinisha to deploy 15 armies to "Canada". As "Canada" initially
     *    has no armies (assuming it starts with 0 for the sake of this test), it is expected to have
     *    15 armies after the order's execution.
     *
     * The test asserts that the actual number of armies in "India" and "Canada" matches the expected
     * outcomes after the deploy orders are executed, ensuring that deploy orders correctly modify
     * the army count of the target countries within the game state.
     */

    @Test
    public void testDeployOrderExecution() {
        d_deployOrder1.execute(d_gameState);
        Country l_countryIndia = d_gameState.getD_map().getCountryByName("India");
        assertEquals("10", l_countryIndia.getD_armies().toString());

        d_deployOrder2.execute(d_gameState);
        Country l_countryCanada = d_gameState.getD_map().getCountryByName("Canada");
        assertEquals("15", l_countryCanada.getD_armies().toString());
    }

    /**
     * Tests the creation and effects of a deploy order on a player's state, including the allocation
     * of armies to a specified country. This method simulates the scenario where a player, "Maze", has
     * 10 unallocated armies and owns a country named "Japan". The player issues a deploy order to place
     * 4 armies in "Japan", and the test verifies the following:
     *
     * 1. The number of unallocated armies for the player decreases from 10 to 6, reflecting the deployment
     *    of 4 armies to "Japan".
     * 2. The player's order list contains exactly 1 deploy order after the operation, indicating that the
     *    deploy order was successfully created and added to the player's list of orders to execute.
     * 3. The created deploy order targets "Japan" and specifies the placement of 4 armies, ensuring the
     *    order's correctness and alignment with the player's command.
     *
     * This test confirms that deploy orders are correctly interpreted, processed, and affect the player's
     * state as intended, including the reduction of unallocated armies and the creation of appropriate
     * deploy orders.
     *
     * @throws InvalidCommand If the command issued to create the deploy order is malformed or otherwise
     *                        invalid, an exception is expected to be thrown, although it is not explicitly
     *                        tested here.
     */

    @Test
    public void testDeployOrder() throws InvalidCommand {
        Player l_player = new Player("Maze");
        l_player.setD_noOfUnallocatedArmies(10);
        Country l_country = new Country(1, "Japan", 1);
        l_player.setD_coutriesOwned(Arrays.asList(l_country));

        l_player.createDeployOrder("deploy Japan 4");

        assertEquals(l_player.getD_noOfUnallocatedArmies().toString(), "6");
        assertEquals(l_player.getD_ordersToExecute().size(), 1);
        Deploy l_order = (Deploy) l_player.order_list.get(0);
        assertEquals("Japan", l_order.d_targetCountryName);
        assertEquals("4", String.valueOf(l_order.d_numberOfArmiesToPlace));
    }

}
