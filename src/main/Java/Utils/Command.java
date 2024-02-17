package Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
public class Command {
    public String d_command;

    public Command(String p_command) {
        // Trim and reduce multiple spaces to a single space for uniformity.
        this.d_command = p_command.trim().replaceAll(" +", " ");
    }

    public String getRootCommand() {
        return d_command.split(" ")[0];
    }

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
            l_operationsList.add(parseOperation(operation));
        });

        return l_operationsList;
    }

    private Map<String, String> parseOperation(String p_operation) {
        Map<String, String> l_operationMap = new HashMap<>();
        String[] l_parts = p_operation.trim().split(" ", 2);

        l_operationMap.put("operation", l_parts[0]);
        l_operationMap.put("arguments", l_parts.length > 1 ? l_parts[1] : "");

        return l_operationMap;
    }

    public boolean isKeyPresent(String key, Map<String, String> inputMap) {
        return inputMap.containsKey(key) && !inputMap.get(key).isEmpty();
    }



    }

