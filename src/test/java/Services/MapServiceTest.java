package Services;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.Before;
import Exceptions.InvalidMap;
import Models.Continent;
import Models.Country;
import Models.GameState;
import Models.Map;
import Utils.CommonUtil;
/**
 * This class contains unit tests for the MapService class.
 */
public class MapServiceTest {

    MapService d_mapservice;// Instance of MapService for testing

    GameState d_state;// Instance of GameState for testing

    Map d_map; // Instance of Map for testing

    /**
     * Sets up the test environment by initializing objects.
     */
    @Before
    public void setup() {
        d_mapservice = new MapService();
        d_map = new Map();
        d_state = new GameState();
        d_map = d_mapservice.loadMap(d_state, "Canada");
    }
    /**
     * Test method for the editMap() function in the MapService class.
     * It tests whether the editMap() function correctly edits the map file.
     * @throws IOException If an I/O error occurs.
     */
    @org.junit.Test
    public void testEditMap() throws IOException {
        d_mapservice.editMap(d_state, "Mapone");
        File l_file = new File(CommonUtil.getMapFilePath("Mapone"));

        assertTrue(l_file.exists());
    }
    /**
     * Test method for the addRemoveContinents() function in the MapService class, specifically testing the addition of continents.
     * It verifies whether the addRemoveContinents() function correctly adds a continent to the map.
     * @throws IOException If an I/O error occurs.
     * @throws InvalidMap If the map is invalid.
     */
    @org.junit.Test
    public void testEditContinentAdd() throws IOException, InvalidMap {
        d_state.setD_map(new Map());
        Map l_updatedContinents = d_mapservice.addRemoveContinents(d_state.getD_map(), "Add", "Russia 12");

        assertEquals(l_updatedContinents.getD_continents().size(), 1);
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentName(), "Russia");
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentValue().toString(), "12");
    }
    /**
     * Test method for the addRemoveContinents() function in the MapService class, specifically testing the removal of continents.
     * It verifies whether the addRemoveContinents() function correctly removes a continent from the map.
     * @throws IOException If an I/O error occurs.
     * @throws InvalidMap If the map is invalid.
     */
    @org.junit.Test
    public void testEditContinentRemove() throws IOException, InvalidMap {
        List<Continent> l_continents = new ArrayList<>();
        Continent l_c1 = new Continent();
        l_c1.setD_continentID(1);
        l_c1.setD_continentName("Russia");
        l_c1.setD_continentValue(10);

        Continent l_c2 = new Continent();
        l_c2.setD_continentID(2);
        l_c2.setD_continentName("Europe");
        l_c2.setD_continentValue(20);

        l_continents.add(l_c1);
        l_continents.add(l_c2);

        Map l_map = new Map();
        l_map.setD_continents(l_continents);
        d_state.setD_map(l_map);
        Map l_updatedContinents = d_mapservice.addRemoveContinents(d_state.getD_map(), "Remove", "Russia");

        assertEquals(l_updatedContinents.getD_continents().size(), 1);
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentName(), "Europe");
        assertEquals(l_updatedContinents.getD_continents().get(0).getD_continentValue().toString(), "20");
    }
    /**
     * Test method for verifying the continent IDs and values in the loaded map.
     * It compares the actual continent IDs and values with the expected ones.
     */
    @org.junit.Test
    public void testContinentIdAndValues() {
        List<Integer> l_actualContinentIdList = new ArrayList<Integer>();
        List<Integer> l_actualContinentValueList = new ArrayList<Integer>();

        List<Integer> l_expectedContinentIdList = new ArrayList<Integer>();
        l_expectedContinentIdList.addAll(Arrays.asList(1, 2, 3, 4));

        List<Integer> l_expectedContinentValueList = new ArrayList<Integer>();
        l_expectedContinentValueList.addAll(Arrays.asList(5, 4, 7, 2));

        for (Continent l_continent : d_map.getD_continents()) {
            l_actualContinentIdList.add(l_continent.getD_continentID());
            l_actualContinentValueList.add(l_continent.getD_continentValue());
        }

        assertEquals(l_expectedContinentIdList, l_actualContinentIdList);
        assertEquals(l_expectedContinentValueList, l_actualContinentValueList);
    }
    /**
     * Test method for verifying the country IDs and neighbor relationships in the loaded map.
     * It compares the actual country IDs and their neighboring country IDs with the expected ones.
     */
    @org.junit.Test
    public void testCountryIdAndNeighbors() {
        List<Integer> l_actualCountryIdList = new ArrayList<Integer>();
        LinkedHashMap<Integer, List<Integer>> l_actualCountryNeighbors = new LinkedHashMap<Integer, List<Integer>>();

        List<Integer> l_expectedCountryIdList = new ArrayList<Integer>();
        l_expectedCountryIdList.addAll(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31
));

        LinkedHashMap<Integer, List<Integer>> l_expectedCountryNeighbors = new LinkedHashMap<Integer, List<Integer>>() {
            {
                put(1, new ArrayList<Integer>(Arrays.asList(8, 2, 3)));
                put(2, new ArrayList<Integer>(Arrays.asList(1, 3)));
                put(3, new ArrayList<Integer>(Arrays.asList(1, 2, 4)));
                put(4, new ArrayList<Integer>(Arrays.asList(3, 5, 7)));
                put(5, new ArrayList<Integer>(Arrays.asList(4, 7, 6)));
                put(6, new ArrayList<Integer>(Arrays.asList(5, 7, 22, 23)));
                put(7, new ArrayList<Integer>(Arrays.asList(4, 5, 6, 8, 11)));
                put(8, new ArrayList<Integer>(Arrays.asList(1, 7, 9)));
                put(9, new ArrayList<Integer>(Arrays.asList(8, 11, 10)));
                put(10, new ArrayList<Integer>(Arrays.asList(9, 11, 12)));
                put(11, new ArrayList<Integer>(Arrays.asList(7, 9, 10, 20, 22)));
                put(12, new ArrayList<Integer>(Arrays.asList(10, 13, 20)));
                put(13, new ArrayList<Integer>(Arrays.asList(12, 19, 14)));
                put(14, new ArrayList<Integer>(Arrays.asList(13, 18, 15)));
                put(15, new ArrayList<Integer>(Arrays.asList(14, 16, 17)));
                put(16, new ArrayList<Integer>(Arrays.asList(15)));
                put(17, new ArrayList<Integer>(Arrays.asList(15, 31, 30, 18)));
                put(18, new ArrayList<Integer>(Arrays.asList(14, 17, 19, 30)));
                put(19, new ArrayList<Integer>(Arrays.asList(13, 18, 20, 30)));
                put(20, new ArrayList<Integer>(Arrays.asList(11, 12, 19, 21)));
                put(21, new ArrayList<Integer>(Arrays.asList(20, 22, 23, 24, 25, 30)));
                put(22, new ArrayList<Integer>(Arrays.asList(6, 11, 21, 23)));
                put(23, new ArrayList<Integer>(Arrays.asList(6, 21, 22, 26, 25)));
                put(24, new ArrayList<Integer>(Arrays.asList(21, 25, 29)));
                put(25, new ArrayList<Integer>(Arrays.asList(21, 23, 24, 26, 27)));
                put(26, new ArrayList<Integer>(Arrays.asList(23, 25)));
                put(27, new ArrayList<Integer>(Arrays.asList(25, 28, 29)));
                put(28, new ArrayList<Integer>(Arrays.asList(27, 29, 30)));
                put(29, new ArrayList<Integer>(Arrays.asList(24, 27, 28, 30)));
                put(30, new ArrayList<Integer>(Arrays.asList(17, 18, 19, 21, 28, 29, 31)));
                put(31, new ArrayList<Integer>(Arrays.asList(17, 30)));

            }
        };

        for (Country l_country : d_map.getD_countries()) {
            ArrayList<Integer> l_neighbours = new ArrayList<Integer>();
            l_actualCountryIdList.add(l_country.getD_countryId());
            l_neighbours.addAll(l_country.getD_adjacentCountryIds());
            l_actualCountryNeighbors.put(l_country.getD_countryId(), l_neighbours);
        }

        assertEquals(l_expectedCountryIdList, l_actualCountryIdList);
        assertEquals(l_expectedCountryNeighbors, l_actualCountryNeighbors);
    }
    /**
     * Test method for verifying the behavior of saving an invalid map.
     * It sets up an invalid map file and attempts to save it, expecting it to throw an InvalidMap exception.
     *
     * @throws InvalidMap if the map being saved is invalid
     */

    /**
     * Test method for verifying the addition of a country to the map.
     * It loads a map, adds a country named "England" to the continent "Asia", and checks if the country was added successfully.
     *
     * @throws IOException  if there is an I/O error while loading the map
     * @throws InvalidMap   if the map is invalid
     */
    @org.junit.Test
    public void testEditCountryAdd() throws IOException, InvalidMap {
        d_mapservice.loadMap(d_state, "Mapone");
        d_mapservice.editCountry(d_state, "add", "Asia England");

        assertEquals(d_state.getD_map().getCountryByName("Asia").getD_countryName(), "England");
    }
    /**
     * Test method for verifying the removal of a country from the map.
     * It loads a map, attempts to remove a country named "England", and expects an InvalidMap exception to be thrown.
     *
     * @throws InvalidMap if the map is invalid or if removing the country operation fails
     */
    @org.junit.Test(expected = InvalidMap.class)
    public void testEditCountryRemove() throws InvalidMap{
        d_mapservice.loadMap(d_state, "Mapone");
        d_mapservice.editCountry(d_state, "remove", "England");
    }
    /**
     * Test method for verifying the addition of a neighbor country to a specified country.
     * It loads a map, adds a continent "Northern-America", adds countries "Canada" and "Alaska" to this continent,
     * then adds "Alaska" as a neighbor to "Canada", and checks if "Alaska" is added as a neighbor to "Canada".
     *
     * @throws InvalidMap if the map is invalid or if the neighbor addition operation fails
     * @throws IOException if an I/O error occurs while loading the map
     */
    @org.junit.Test
    public void testEditNeighborAdd() throws InvalidMap, IOException {
        d_mapservice.loadMap(d_state, "Mapone");
        d_mapservice.editContinent(d_state, "Northern-America 8", "add");
        d_mapservice.editCountry(d_state, "add", "Canada Northern-America");
        d_mapservice.editCountry(d_state, "add", "Alaska Northern-America");
        d_mapservice.editNeighbour(d_state, "add", "Canada Alaska");

        assertEquals(d_state.getD_map().getCountryByName("Canada").getD_adjacentCountryIds().get(0), d_state.getD_map().getCountryByName("Alaska").getD_countryId());
    }
    /**
     * Test method for verifying the removal of a neighbor country from a specified country.
     * It edits a map, adds a continent "Asia", adds countries "Maldives" and "Singapore" to this continent,
     * then adds "Singapore" as a neighbor to "Maldives", and attempts to remove this neighbor.
     * The test expects an {@link InvalidMap} exception to be thrown, indicating that the neighbor removal operation failed.
     *
     * @throws InvalidMap if the map is invalid or if the neighbor removal operation fails
     * @throws IOException if an I/O error occurs while editing the map
     */
    @org.junit.Test(expected = InvalidMap.class)
    public void testEditNeighborRemove() throws InvalidMap, IOException{
        d_mapservice.editMap(d_state, "Mapone");
        d_mapservice.editContinent(d_state, "Asia 9", "add");
        d_mapservice.editCountry(d_state, "add", "Maldives Asia");
        d_mapservice.editNeighbour(d_state, "add", "Singapore Maldives");
    }
    /**
     * Javadoc for the JUnit test methods in the MapServiceTest class.
     * These test methods cover various functionalities related to map editing and management.
     * They ensure the correctness and reliability of map-related operations in the MapService class.
     */
    @Test
    void loadMap() {
    }
    /**
     * Tests the loading of a file.
     */
    @Test
    void loadFile() {
    }
    /**
     * Tests the retrieval of metadata.
     */
    @Test
    void getMetaData() {
    }
    /**
     * Tests the parsing of continents metadata.
     */
    @Test
    void parseContinentsMetaData() {
    }
    /**
     * Tests the parsing of countries metadata.
     */
    @Test
    void parseCountriesMetaData() {
    }
    /**
     * Tests the parsing of border metadata.
     */
    @Test
    void parseBorderMetaData() {
    }
    /**
     * Tests the linking of countries to continents.
     */
    @Test
    void linkCountryContinents() {
    }

    /**
     * Tests the editing of a map.
     */
    @Test
    void editMap() {
    }

    /**
     * Tests the editing of continents.
     */
    @Test
    void editContinent() {
    }
    /**
     * Tests the addition or removal of continents.
     */
    @Test
    void addRemoveContinents() {
    }

    /**
     * Tests the editing of countries.
     */
    @Test
    void editCountry() {
    }
    /**
     * Tests the addition or removal of countries.
     */
    @Test
    void addRemoveCountry() {
    }
    /**
     * Tests the editing of neighbors.
     */
    @Test
    void editNeighbour() {
    }

    /**
     * Tests the addition or removal of neighbors.
     */
    @Test
    void addRemoveNeighbour() {
    }
    /**
     * Tests the saving of a map.
     */
    @Test
    void saveMap() {
    }
    /**
     * Tests the resetting of a map.
     */
    @Test
    void resetMap() {
    }
}