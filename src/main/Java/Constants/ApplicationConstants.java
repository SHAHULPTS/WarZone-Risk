package Constants;
import java.util.Arrays;
import java.util.List;

public final class ApplicationConstants {
    // Error messages for invalid commands
    public static final String INVALID_COMMAND_ERROR_EDITMAP = "Invalid command. Please provide the command in the following format: editmap filename";
    public static final String INVALID_COMMAND_ERROR_EDITCONTINENT = "Invalid command. Please provide the command in the following format: editcontinent -add continentID continentvalue -remove continentID";
    public static final String INVALID_COMMAND_ERROR_EDITCOUNTRY = "Invalid command. Please provide the command in the following format: editcountry -add countryID continentID -remove countryID";
    public static final String INVALID_COMMAND_ERROR_EDITNEIGHBOUR = "Invalid command. Please provide the command in the following format: editneighbor -add countryID neighborcountryID -remove countryID neighborcountryID";
    public static final String INVALID_COMMAND_ERROR_SAVEMAP = "Invalid command. Please provide the command in the following format: savemap filename";
    public static final String INVALID_MAP_ERROR_EMPTY = "No map found! Please load a valid map to check.";
    public static final String INVALID_COMMAND_ERROR_LOADMAP = "Invalid command. Please provide the command in the following format: loadmap filename";
    public static final String INVALID_COMMAND_ERROR_VALIDATEMAP = "Invalid command! Validatemap is not supposed to have any arguments.";
    public static final String INVALID_COMMAND_ERROR_GAMEPLAYER = "Invalid command. Please provide the command in the following format: gameplayer -add playername -remove playername";
    public static final String INVALID_MAP_LOADED = "Map cannot be loaded as it is invalid. Please provide a valid map.";
    public static final String INVALID_COMMAND_ERROR_ASSIGNCOUNTRIES = "Invalid command. Please provide the command in the following format: assigncountries";
    public static final String INVALID_COMMAND_ERROR_DEPLOY_ORDER = "Invalid command. Please provide the command in the following format: deploy countryID <CountryName> <num> (until all reinforcements have been placed).";
    public static final String VALID_MAP = "The loaded map is valid!";

    // Key Constants
    public static final String ARGUMENTS = "arguments";
    public static final String OPERATION = "operation";

    // File Extension
    public static final String MAPFILEEXTENSION = ".map";

    // Important Constants
    public static final String ARMIES = "Armies";
    public static final String CONTROL_VALUE = "Control Value";
    public static final String CONNECTIVITY = "Connections";
    public static final String SRC_MAIN_RESOURCES = "src/main/resources";
    public static final int CONSOLE_WIDTH = 80;

    // ANSI- Color Codes
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\u001B[47m";

    // Colors Available
    public static final List<String> COLORS = Arrays.asList(RED, GREEN, YELLOW, BLUE, PURPLE, CYAN);


}