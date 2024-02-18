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

public class GameEngineControllerTest {


    Map d_map;

    GameState d_state;

    GameEngineController d_gameEngine;

    @Before
    public void setup() {
        d_map = new Map();
        d_gameEngine = new GameEngineController();
        d_state = d_gameEngine.getD_gameState();
    }

    @Test(expected = InvalidCommand.class)
    public void testPerformEditMapInvalidCommand() throws IOException, InvalidCommand {
        Command l_command = new Command("editmap");
        d_gameEngine.performMapEdit(l_command);
    }

    @Test(expected = InvalidCommand.class)
    public void testPerformEditContinentInvalidCommand() throws IOException, InvalidCommand, InvalidMap {
        Command l_command = new Command("editcontinent -add");
        d_gameEngine.performEditContinent(l_command);
    }

    @Test
    public void testPerformEditContinentValidCommand() throws IOException, InvalidCommand, InvalidMap {
        d_map.setD_mapFile("testeditmap.map");
        d_state.setD_map(d_map);
        Command l_addCommand = new Command("editcontinent -add India 6 -add Afghanistan 5");
        d_gameEngine.performEditContinent(l_addCommand);

        List<Continent> l_continents = d_state.getD_map().getD_continents();
        assertEquals(l_continents.size(), 2);
        assertEquals(l_continents.get(0).getD_continentName(), "India");
        assertEquals(l_continents.get(0).getD_continentValue().toString(), "6");
        assertEquals(l_continents.get(1).getD_continentName(), "Afghanistan");
        assertEquals(l_continents.get(1).getD_continentValue().toString(), "5");

        Command l_removeCommand = new Command("editcontinent -remove Afghanistan");
        d_gameEngine.performEditContinent(l_removeCommand);
        l_continents = d_state.getD_map().getD_continents();
        assertEquals( 1, l_continents.size());
    }

    @Test(expected = InvalidCommand.class)
    public void testPerformSaveMapInvalidCommand() throws InvalidCommand, InvalidMap {
        Command l_command = new Command("savemap");
        d_gameEngine.performSaveMap(l_command);
    }

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
    void performEditContinent() {
    }

    @org.junit.jupiter.api.Test
    void performSaveMap() {
    }

    @org.junit.jupiter.api.Test
    void performEditCountry() {
    }

    @org.junit.jupiter.api.Test
    void performEditNeighbour() {
    }

    @org.junit.jupiter.api.Test
    void performMapEdit() {
    }
}