package TestSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import Models.AdvanceTest;
import Models.AirliftTest;
import Models.BlockadeTest;
import Models.BombTest;
import Models.DeployTest;
import Models.DiplomacyTest;
import Models.PlayerTest;
import Services.PlayerServiceTest;

/**
 * Test suite designed to assess issue detection and order execution capabilities,
 * along with several player services, including adding players, and assigning armies and countries.
 */
@RunWith(Suite.class)
@SuiteClasses({ DeployTest.class, PlayerTest.class, PlayerServiceTest.class, AdvanceTest.class, AirliftTest.class,
        BlockadeTest.class, BombTest.class, DiplomacyTest.class})
public class MainGameTestSuite {
}