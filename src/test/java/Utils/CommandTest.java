package Utils;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

/**
 * The {@code CommandTest} class contains test cases for the {@code Command} class.
 * It ensures that the command parsing and extraction methods work correctly.
 */
public class CommandTest {

    /**
     * Test method to verify the extraction of the root command from a valid command string.
     */
    @org.junit.Test
    public void test_validCommand_getRootCommand(){
        Command l_command = new Command("editcontinent -add continentID continentvalue");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("editcontinent",l_rootCommand);
    }

    /**
     * Test method to verify the behavior of the getRootCommand() method when called with an empty command string.
     * It checks if the method returns an empty string.
     */
    @org.junit.Test
    public void test_inValidCommand_getRootCommand(){
        Command l_command = new Command("");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("", l_rootCommand);
    }

    /**
     * Test method to verify the behavior of the getRootCommand() method when called with a single-word command string.
     * It checks if the method correctly returns the root command.
     */
    @org.junit.Test
    public void test_singleWord_getRootCommand(){
        Command l_command = new Command("validatemap");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("validatemap", l_rootCommand);
    }

    /**
     * Test method to verify the behavior of the getRootCommand() method when called with a command containing no flags.
     * It checks if the method correctly returns the root command.
     */
    @org.junit.Test
    public void test_noFlagCommand_getRootCommand(){
        Command l_command = new Command("loadmap xyz.txt");
        String l_rootCommand = l_command.getRootCommand();

        assertEquals("loadmap", l_rootCommand);
    }

    /**
     * Test method to verify the behavior of the getOperationsAndArguments() method when called with a single command containing flags.
     * It checks if the method correctly extracts and returns the operations and their corresponding arguments.
     */
    @org.junit.Test
    public void test_singleCommand_getOperationsAndArguments(){
        Command l_command = new Command("editcontinent -remove continentID");
        List<Map<String , String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandTwo = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandTwo);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }


    /**
     * Test method to verify the behavior of the getOperationsAndArguments() method when called with a single command containing flags with extra spaces.
     * It checks if the method correctly handles and extracts operations and their corresponding arguments when extra spaces are present between the command elements.
     */
    @org.junit.Test
    public void test_singleCommandWithExtraSpaces_getOperationsAndArguments(){
        Command l_command = new Command("editcontinent      -remove continentID");
        List<Map<String , String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandTwo = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandTwo);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

    /**
     * Test method to verify the behavior of the getOperationsAndArguments() method when called with multiple commands.
     * It checks if the method correctly extracts operations and their corresponding arguments from multiple commands.
     */
    @org.junit.Test
    public void test_multiCommand_getOperationsAndArguments(){
        Command l_command = new Command("editcontinent -add continentID continentValue  -remove continentID");
        List<Map<String , String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandOne = new HashMap<String, String>() {{
            put("arguments", "continentID continentValue");
            put("operation", "add");
        }};
        Map<String, String> l_expectedCommandTwo = new HashMap<String, String>() {{
            put("arguments", "continentID");
            put("operation", "remove");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandOne);
        l_expectedOperationsAndValues.add(l_expectedCommandTwo);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }

    /**
     * Test method to verify the behavior of the getOperationsAndArguments() method when called with a single command without flags.
     * It checks if the method correctly identifies the command and treats it as a filename argument.
     */
    @org.junit.Test
    public void test_noFlagCommand_getOperationsAndArguments(){
        Command l_command = new Command("loadmap xyz.txt");
        List<Map<String , String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandOne = new HashMap<String, String>() {{
            put("arguments", "xyz.txt");
            put("operation", "filename");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandOne);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }


    /**
     * Test method to verify the behavior of the getOperationsAndArguments() method when called with a single command without flags,
     * but with extra spaces between the command and its argument.
     * It checks if the method correctly identifies the command and treats it as a filename argument.
     */
    @org.junit.Test
    public void test_noFlagCommandWithExtraSpaces_getOperationsAndArguments(){
        Command l_command = new Command("loadmap         xyz.txt");
        List<Map<String , String>> l_actualOperationsAndValues = l_command.getOperationsAndArguments();

        // Preparing Expected Value
        List<Map<String , String>> l_expectedOperationsAndValues = new ArrayList<Map<String, String>>();

        Map<String, String> l_expectedCommandOne = new HashMap<String, String>() {{
            put("arguments", "xyz.txt");
            put("operation", "filename");
        }};
        l_expectedOperationsAndValues.add(l_expectedCommandOne);

        assertEquals(l_expectedOperationsAndValues, l_actualOperationsAndValues);
    }
    /**
     * Test method to verify the behavior of the getRootCommand() method.
     */
    @Test
    void getRootCommand() {
    }

    /**
     * Test method to verify the behavior of the getOperationsAndArguments() method.
     */
    @Test
    void getOperationsAndArguments() {
    }

    /**
     * Test method to verify the behavior of the hasRequiredKeys() method.
     */
    @Test
    void hasRequiredKeys() {
    }

}