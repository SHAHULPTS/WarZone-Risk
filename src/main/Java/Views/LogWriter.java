package Views;

import Models.LogEntryBuffer;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Observable;
import java.util.Observer;

/**
 * The LogWriter class implements the Observer interface to observe changes in the LogEntryBuffer.
 * It writes log messages to a file.
 */
public class LogWriter implements Observer {

    /** The LogEntryBuffer instance to observe. */
    private LogEntryBuffer d_logEntryBuffer;

    /**
     * This method is called whenever the observed object (LogEntryBuffer) is changed.
     *
     * @param p_observable the Observable object being observed, expected to be a LogEntryBuffer instance
     * @param p_object     the argument passed to the notifyObservers method, representing the changed object
     */
    @Override
    public void update(Observable p_observable, Object p_object) {
        if (!(p_object instanceof LogEntryBuffer)) {
            // Unexpected object, do nothing
            return;
        }

        d_logEntryBuffer = (LogEntryBuffer) p_observable;
        String l_logMessage = d_logEntryBuffer.getD_logMessage();

        try {
            // Create the log file if it doesn't exist
            File l_logFile = new File("LogFile.txt");
            if (!l_logFile.exists()) {
                l_logFile.createNewFile();
            }

            // Truncate the log file if the message is for initializing the game
            if (l_logMessage.equals("Initializing the Game ......\n\n")) {
                Files.newBufferedWriter(Paths.get("LogFile.txt"), StandardCharsets.UTF_8).write("");
            }

            // Append the log message to the file
            Files.write(Paths.get("LogFile.txt"), l_logMessage.getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException p_exception) {
            p_exception.printStackTrace();
        }
    }
}
