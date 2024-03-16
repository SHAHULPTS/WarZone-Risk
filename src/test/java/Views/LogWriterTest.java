package Views;

import Models.LogEntryBuffer;

/**
 * This class is used to test the functionality of the LogWriter class.
 */
public class LogWriterTest {

    /**
     * The main method to run the test.
     *
     * @param args command-line arguments (not used in this application)
     */
    public static void main(String[] args) {
        // Create a LogEntryBuffer instance
        LogEntryBuffer logEntryBuffer = new LogEntryBuffer();

        // Create a LogWriter instance and attach it to the LogEntryBuffer
        LogWriter logWriter = new LogWriter();
        logEntryBuffer.addObserver(logWriter);

        // Simulate some changes in the LogEntryBuffer
        logEntryBuffer.currentLog("Initializing the Game ......", "start");
        logEntryBuffer.notifyObservers();

        logEntryBuffer.currentLog("This is a test log message.", "effect");
        logEntryBuffer.notifyObservers();

        logEntryBuffer.currentLog("Another test log message.", "effect");
        logEntryBuffer.notifyObservers();
    }
}
