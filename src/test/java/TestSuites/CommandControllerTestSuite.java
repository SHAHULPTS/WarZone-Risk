package TestSuites;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import Controllers.GameEngineTest;
import Models.OrderExecutionPhaseTest;
import Utils.CommandTest;

@RunWith(Suite.class)
@SuiteClasses({ CommandTest.class, GameEngineTest.class, OrderExecutionPhaseTest.class })
public class CommandControllerTestSuite {
}