package Models;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;

/**
 * Class contains test cases for the Player class.
 */
public class PlayerTest {

    /**
     * List of existing players.
     */
    List<Player> d_exisitingPlayerList = new ArrayList<>();

    /**
     * Sets up the test environment before each test method execution.
     */
    @Before
    public void setup() {
        d_exisitingPlayerList.add(new Player("Jay"));
        d_exisitingPlayerList.add(new Player("Nidhi"));
    }

    /**
     * Tests the functionality of the "next_order" method in the Player class.
     */
    @org.junit.Test
    public void testNextOrder() {

        Order l_order1 = new Order();
        l_order1.setD_orderAction("deploy");
        l_order1.setD_numberOfArmiesToPlace(5);
        l_order1.setD_sourceCountryName(null);
        l_order1.setD_targetCountryName("India");

        Order l_order2 = new Order();
        l_order1.setD_orderAction("airlift");
        l_order2.setD_numberOfArmiesToPlace(6);
        l_order2.setD_sourceCountryName("Canada");
        l_order2.setD_targetCountryName("Finland");

        List<Order> l_orderlist = new ArrayList<>();
        l_orderlist.add(l_order1);
        l_orderlist.add(l_order2);

        d_exisitingPlayerList.get(0).setD_ordersToExecute(l_orderlist);
        Order l_order = d_exisitingPlayerList.get(0).next_order();
        assertEquals(l_order, l_order1);
        assertEquals(d_exisitingPlayerList.get(0).getD_ordersToExecute().size(), 1);
    }

    @Test
    void getPlayerName() {
    }

    @Test
    void setPlayerName() {
    }

    @Test
    void getD_color() {
    }

    @Test
    void setD_color() {
    }

    @Test
    void getD_coutriesOwned() {
    }

    @Test
    void setD_coutriesOwned() {
    }

    @Test
    void getD_continentsOwned() {
    }

    @Test
    void setD_continentsOwned() {
    }

    @Test
    void getD_ordersToExecute() {
    }

    @Test
    void setD_ordersToExecute() {
    }

    @Test
    void getD_noOfUnallocatedArmies() {
    }

    @Test
    void setD_noOfUnallocatedArmies() {
    }

    @Test
    void getCountryNames() {
    }

    @Test
    void getContinentNames() {
    }

    @Test
    void issue_order() {
    }

    @Test
    void next_order() {
    }
}