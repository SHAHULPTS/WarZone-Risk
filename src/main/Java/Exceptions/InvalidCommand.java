package Exceptions;

public class InvalidCommand extends Exception {

    public InvalidCommand(String p_message) {
        super(p_message);
    }
}