package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * The {@code Command} class represents a command parsed from user input.
 * It provides methods to process and extract information from the command string.
 */
public class Command {

    /** The original command string. */
    public String d_command;

    /**
     * Constructs a new Command object with the specified command string.
     *
     * @param p_command the command string to be processed
     */
    public Command(String p_command) {
        // Trim and reduce multiple spaces to a single space for uniformity.
        this.d_command = p_command.trim().replaceAll(" +", " ");
    }

    /**
     * Gets the root command from the command string.
     *
     * @return the root command
     */
    public String getRootCommand() {
        return d_command.split(" ")[0];
    }


    /**
     * Extracts operations and their arguments from the command string.
     *
     * @return a list of maps containing operations and their arguments
     */
    public List<Map<String, String>> getOperationsAndArguments() {
        String l_rootCommand = getRootCommand();
        String l_operationsString = d_command.replaceFirst(Pattern.quote(l_rootCommand), "").trim();

        if (l_operationsString.isEmpty()) {
            return new ArrayList<Map<String , String>>();
        }


        boolean isFlaglessCommand = !l_operationsString.contains("-") && l_operationsString.matches("[^-\\s]+");

        if (isFlaglessCommand){
            l_operationsString = "-filename " + l_operationsString;
        }
        List<Map<String, String>> l_operationsList = new ArrayList<>();
        String[] l_operations = l_operationsString.split("-");

        Arrays.stream(l_operations).filter(operation -> operation.length() > 1).forEach(operation -> {
            l_operationsList.add(getOperationAndArgumentsMap(operation));
        });

        return l_operationsList;
    }

    /**
     * Constructs a map containing an operation and its arguments.
     *
     * @param p_operation the operation string
     * @return a map containing the operation and its arguments
     */
    private Map<String, String> getOperationAndArgumentsMap(String p_operation) {
        Map<String, String> l_operationMap = new HashMap<>();
        String[] l_parts = p_operation.trim().split(" ", 2);

        l_operationMap.put("operation", l_parts[0]);
        l_operationMap.put("arguments", l_parts.length > 1 ? l_parts[1] : "");

        return l_operationMap;
    }

    /**
     * Checks if a required key is present in a map and its value is not empty.
     *
     * @param key the key to be checked
     * @param inputMap the map to be checked
     * @return {@code true} if the key is present and its value is not empty, otherwise {@code false}
     */
    public boolean hasRequiredKeys(String key, Map<String, String> inputMap) {
        return inputMap.containsKey(key) && !inputMap.get(key).isEmpty();
    }


    }

