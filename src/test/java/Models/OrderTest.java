package Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;

public class OrderTest {

    Order d_orderDetails;

    Player d_playerInfo;

    @Before
    public void setup() {
        d_orderDetails = new Order();
        d_playerInfo = new Player();
    }

    @org.junit.Test
    public void testValidateDeployOrderCountry() {
        d_orderDetails.setD_targetCountryName("India");
        List<Country> l_countryList = new ArrayList<Country>();
        l_countryList.add(new Country("India"));
        l_countryList.add(new Country("Canada"));
        d_playerInfo.setD_coutriesOwned(l_countryList);
        boolean l_actualBoolean = d_orderDetails.validateDeployOrderCountry(d_playerInfo, d_orderDetails);
        assertTrue(l_actualBoolean);
    }

    @org.junit.Test
    public void testDeployOrderExecution() {
        Order l_order1 = new Order("deploy", "India", 5);
        Order l_order2 = new Order("deploy", "Canada", 10);
        Player l_player = new Player();
        List<Country> l_playersCountries = new ArrayList<Country>();
        l_playersCountries.add(new Country("India"));
        l_playersCountries.add(new Country("Canada"));
        l_player.setD_coutriesOwned(l_playersCountries);

        List<Country> l_mapCountries = new ArrayList<Country>();
        Country l_country1 = new Country(1, "Canada", 1);
        Country l_country2 = new Country(1, "India", 2);
        l_country2.setD_armies(5);
        Country l_country3 = new Country(1, "Japan", 2);

        l_mapCountries.add(l_country1);
        l_mapCountries.add(l_country2);
        l_mapCountries.add(l_country3);

        Map l_map = new Map();
        l_map.setD_countries(l_mapCountries);
        GameState l_gameState = new GameState();
        l_gameState.setD_map(l_map);

        l_order1.execute(l_gameState, l_player);
        Country l_countryIndia = l_gameState.getD_map().getCountryByName("India");
        assertEquals(l_countryIndia.getD_armies().toString(), "10");

        l_order2.execute(l_gameState, l_player);
        Country l_countryCanada = l_gameState.getD_map().getCountryByName("Canada");
        assertEquals(l_countryCanada.getD_armies().toString(), "15");

    }

    @Test
    void getD_orderAction() {
    }

    @Test
    void setD_orderAction() {
    }

    @Test
    void getD_targetCountryName() {
    }

    @Test
    void setD_targetCountryName() {
    }

    @Test
    void getD_sourceCountryName() {
    }

    @Test
    void setD_sourceCountryName() {
    }

    @Test
    void getD_numberOfArmiesToPlace() {
    }

    @Test
    void setD_numberOfArmiesToPlace() {
    }

    @Test
    void execute() {
    }

//    @Test
//    void validateDeployOrderCountry() {
//    }
}