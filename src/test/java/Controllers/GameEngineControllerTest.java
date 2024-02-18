package Controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import Exceptions.InvalidCommand;
import Exceptions.InvalidMap;
import Models.GameState;
import Models.Map;
import Utils.Command;

import Models.Continent;
import org.junit.Before;
import org.junit.Test;
/**
 * This class contains unit tests for the GameEngineController class.
 */
public class GameEngineControllerTest {


    Map d_map;

    GameState d_state;

    GameEngineController d_gameEngine;

    /**
     * Sets up the test environment before each test method runs.
     */
    @Before
    public void setup() {
        d_map = new Map();
        d_gameEngine = new GameEngineController();
        d_state = d_gameEngine.getD_gameState();
    }

    /**
     * Tests the behavior of performMapEdit method with an invalid command.
     * @throws IOException if an I/O error occurs.
     * @throws InvalidCommand if the command is invalid.
     */
    @Test(expected = InvalidCommand.class)
    public void testEditMapInvalidCommand() throws IOException, InvalidCommand {
        Command l_command = new Command("editmap");
        d_gameEngine.MapEdit(l_command);
    }

    /**
     * Tests the behavior of performEditContinent method with an invalid command.
     * @throws IOException if an I/O error occurs.
     * @throws InvalidCommand if the command is invalid.
     * @throws InvalidMap if the map is invalid.
     */
    @Test(expected = InvalidCommand.class)
    public void testEditContinentInvalidCommand() throws IOException, InvalidCommand, InvalidMap {
        Command l_command = new Command("editcontinent -add");
        d_gameEngine.EditContinent(l_command);
    }

    /**
     * Tests the behavior of performEditContinent method with a valid command.
     * @throws IOException if an I/O error occurs.
     * @throws InvalidCommand if the command is invalid.
     * @throws InvalidMap if the map is invalid.
     */
    @Test
    public void testEditContinentValidCommand() throws IOException, InvalidCommand, InvalidMap {
        d_map.setD_mapFile("testeditmap.map");
        d_state.setD_map(d_map);
        Command l_addCommand = new Command("editcontinent -add India 6 -add Afghanistan 5");
        d_gameEngine.EditContinent(l_addCommand);

        List<Continent> l_continents = d_state.getD_map().getD_continents();
        assertEquals(l_continents.size(), 2);
        assertEquals(l_continents.get(0).getD_continentName(), "India");
        assertEquals(l_continents.get(0).getD_continentValue().toString(), "6");
        assertEquals(l_continents.get(1).getD_continentName(), "Afghanistan");
        assertEquals(l_continents.get(1).getD_continentValue().toString(), "5");

        Command l_removeCommand = new Command("editcontinent -remove Afghanistan");
        d_gameEngine.EditContinent(l_removeCommand);
        l_continents = d_state.getD_map().getD_continents();
        assertEquals( 1, l_continents.size());
    }

    /**
     * Tests the behavior of performSaveMap method with an invalid command.
     * @throws InvalidCommand if the command is invalid.
     * @throws InvalidMap if the map is invalid.
     */
    @Test(expected = InvalidCommand.class)
    public void testSaveMapInvalidCommand() throws InvalidCommand, InvalidMap {
        Command l_command = new Command("savemap");
        d_gameEngine.SaveMap(l_command);
    }

    /**
     * Tests the behavior of assignCountries method with an invalid command.
     * @throws InvalidCommand if the command is invalid.
     * @throws IOException if an I/O error occurs.
     */
    @Test(expected = InvalidCommand.class)
    public void testAssignCountriesInvalidCommand() throws InvalidCommand, IOException {
        Command l_command = new Command("assigncountries -add India");
        d_gameEngine.assignCountries(l_command);
    }

    @org.junit.jupiter.api.Test
    void getD_gameState() {
    }

    @org.junit.jupiter.api.Test
    void main() {
    }

    @org.junit.jupiter.api.Test
    void handleCommand() {
    }

    @org.junit.jupiter.api.Test
    void EditContinent() {
    }

    @org.junit.jupiter.api.Test
    void SaveMap() {
    }

    @org.junit.jupiter.api.Test
    void EditCountry() {
    }

    @org.junit.jupiter.api.Test
    void EditNeighbour() {
    }

    @org.junit.jupiter.api.Test
    void MapEdit() {
    }
}