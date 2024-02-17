package Models;

import java.util.ArrayList;
import java.util.List;

public class Continent {

    Integer d_continentID;
    String d_continentName;
    Integer d_continentValue;

    public Continent(Integer p_continentID, String p_continentName, int p_continentValue) {
        this.d_continentID = p_continentID;
        this.d_continentName = p_continentName;
        this.d_continentValue = p_continentValue;
    }

    public Continent() {
    }

    public Continent(String p_continentName) {
        this.d_continentName = p_continentName;
    }

    public Integer getD_continentID() {
        return d_continentID;
    }

    public void setD_continentID(Integer p_continentID) {
        this.d_continentID = p_continentID;
    }

    public String getD_continentName() {
        return d_continentName;
    }

    public void setD_continentName(String p_continentName) {
        this.d_continentName = p_continentName;
    }

    public Integer getD_continentValue() {
        return d_continentValue;
    }

    public void setD_continentValue(Integer p_continentValue) {
        this.d_continentValue = p_continentValue;
    }
}
