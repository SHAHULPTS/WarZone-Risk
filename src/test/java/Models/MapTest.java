package Models;

import Exceptions.InvalidMap;
import Services.MapService;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class MapTest {

    Map d_map;
    MapService d_ms;
    GameState d_gameState;

    @Before
    public void beforeValidateTest(){
        d_map=new Map();
        d_gameState=new GameState();
        d_ms= new MapService();
    }

    @org.junit.Test(expected = InvalidMap.class)
    public void testValidateNoContinent() throws InvalidMap{
        assertEquals(d_map.Validate(), false);
    }

    @org.junit.Test(expected = InvalidMap.class)
    public void testValidate() throws InvalidMap {
        d_map= d_ms.loadMap(d_gameState, "canada.map");

        assertEquals(d_map.Validate(), true);
        d_map= d_ms.loadMap(d_gameState, "swiss.map");
        d_map.Validate();
    }

    @org.junit.Test(expected = InvalidMap.class)
    public void testValidateNoCountry() throws InvalidMap{
        Continent l_continent = new Continent();
        List<Continent> l_continents = new ArrayList<Continent>();

        l_continents.add(l_continent);
        d_map.setD_continents(l_continents);
        d_map.Validate();
    }

    @org.junit.Test(expected = InvalidMap.class)
    public void testContinentConnectivity() throws  InvalidMap{
        d_map= d_ms.loadMap(d_gameState, "continentConnectivity.map");
        d_map.Validate();
    }

    @org.junit.Test(expected = InvalidMap.class)
    public void testCountryConnectivity() throws InvalidMap{
        d_map.createContinent("Australia", 8);
        d_map.createCountry("India", "Asia");
        d_map.createCountry("China", "Asia");
        d_map.createCountry("Pakistan", "Asia");
        d_map.addCountryNeighbour("India", "China");
        d_map.addCountryNeighbour("China", "India");
        d_map.createCountry("SriLanka", "Asia");
        d_map.addCountryNeighbour("India", "Pakistan");
        d_map.validateCountryConnectivity();
    }

    @Test
    void getD_mapFile() {
    }

    @Test
    void setD_mapFile() {
    }

    @Test
    void getD_continents() {
    }

    @Test
    void setD_continents() {
    }

    @Test
    void getD_countries() {
    }

    @Test
    void setD_countries() {
    }

    @Test
    void addContinent() {
    }

    @Test
    void addCountry() {
    }

    @Test
    void getContinentIDs() {
    }

    @Test
    void getCountryIDs() {
    }

    @Test
    void checkContinents() {
    }

    @Test
    void checkCountries() {
    }

    @Test
    void validate() {
    }

    @Test
    void checkForNullObjects() {
    }

    @Test
    void checkContinentConnectivity() {
    }

    @Test
    void subGraphConnectivity() {
    }

    @Test
    void dfsSubgraph() {
    }

    @Test
    void dfsCountry() {
    }

    @Test
    void checkCountryConnectivity() {
    }

    @Test
    void getAdjacentCountry() {
    }

    @Test
    void getCountry() {
    }

    @Test
    void getCountryByName() {
    }

    @Test
    void getContinent() {
    }

    @Test
    void getContinentByID() {
    }

    @Test
    void testAddContinent() {
    }

    @Test
    void removeContinent() {
    }

    @Test
    void testAddCountry() {
    }

    @Test
    void removeCountry() {
    }

    @Test
    void addCountryNeighbour() {
    }

    @Test
    void removeCountryNeighboursFromAll() {
    }

    @Test
    void removeCountryNeighbour() {
    }

    @Test
    void updateNeighboursCont() {
    }
}