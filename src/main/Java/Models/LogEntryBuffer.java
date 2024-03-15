package Models;

import Views.LogWriter;
import java.util.Observable;

public class LogEntryBuffer extends Observable {

    private String d_logMessage;

    public LogEntryBuffer() {
        LogWriter l_logWriter = new LogWriter();
        this.addObserver(l_logWriter);
    }

    public String getD_logMessage() {
        return d_logMessage;
    }

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
