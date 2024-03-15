package Models;

import Views.LogWriter;
import java.util.Observable;

/**
 * The LogEntryBuffer class extends Observable and represents a buffer for log messages.
 * It notifies observers (such as LogWriter) when a new log message is added.
 */
public class LogEntryBuffer extends Observable {

    /** The log message stored in the buffer. */
    private String d_logMessage;

    /**
     * Constructs a new LogEntryBuffer and adds a LogWriter observer to it.
     */
    public LogEntryBuffer() {
        LogWriter l_logWriter = new LogWriter();
        this.addObserver(l_logWriter);
    }

    /**
     * Gets the log message stored in the buffer.
     *
     * @return the log message
     */
    public String getD_logMessage() {
        return d_logMessage;
    }

    /**
     * Updates the log message in the buffer based on the provided message and log type,
     * then notifies observers of the change.
     *
     * @param p_messageToUpdate the message to update the log with
     * @param p_logType         the type of log message (e.g., command, order, phase, effect, start, end)
     */
    public void currentLog(String p_messageToUpdate, String p_logType) {
        switch (p_logType.toLowerCase()) {
            case "command":
                d_logMessage = "\nInput Command: " + p_messageToUpdate + "\n";
                break;
            case "order":
                d_logMessage = "\nIssued Order: " + p_messageToUpdate + "\n";
                break;
            case "phase":
                d_logMessage = "\n-------" + p_messageToUpdate + "-------\n\n";
                break;
            case "effect":
                d_logMessage = "Log: " + p_messageToUpdate + "\n";
                break;
            case "start":
            case "end":
                d_logMessage = p_messageToUpdate + "\n";
                break;
        }
        setChanged();
        notifyObservers();
    }
}
