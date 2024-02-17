package Views;

import java.util.*;
import Constants.ApplicationConstants;
import Exceptions.InvalidMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import Models.Continent;
import Models.Country;
import Models.Player;
import Utils.CommonUtil;

public class MapView {
    List<Player> d_players;
    //GameState d_gameState;
    Map d_map;
    List<Country> d_countries;
    List<Continent> d_continents;

    public static final String ANSI_RESET = "\u001B[0m";



    private String getColorizedString(String p_color, String p_s) {
        if(p_color == null) return p_s;

        return p_color + p_s + ANSI_RESET;
    }

    private void renderCenteredString (int p_width, String p_s) {
        String l_centeredString = String.format("%-" + p_width  + "s", String.format("%" + (p_s.length() + (p_width - p_s.length()) / 2) + "s", p_s));

        System.out.format(l_centeredString+"\n");
    }

    private void renderSeparator(){
        StringBuilder l_separator = new StringBuilder();

        for (int i = 0; i< ApplicationConstants.CONSOLE_WIDTH -2; i++){
            l_separator.append("-");
        }
        System.out.format("+%s+%n", l_separator.toString());
    }


    public void showMap() {
    }
}

