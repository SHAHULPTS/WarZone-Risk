package Views;

import Constants.ApplicationConstants;
import Exceptions.InvalidMap;
import java.util.List;
import Models.Continent;
import Models.Country;
import Models.Player;
import Utils.CommonUtil;
import Models.Map;
import Models.GameState;
import org.davidmoten.text.utils.WordWrap;

/**
 * The MapView class is responsible for displaying the game map, including continents, countries, players, and their information.
 */
public class MapView {
    /**
     * List of players in the game.
     */
    List<Player> d_players;
    /**
     * Map object representing the game map.
     */
    Map d_map;
    /**
     * GameState object representing the current state of the game.
     */
    GameState d_gameState;
    /**
     * List of countries in the game map.
     */
    List<Country> d_countries;

    /**
     * List of continents in the game map.
     */
    List<Continent> d_continents;

    /**
     * ANSI escape code for resetting text color.
     */
    public static final String ANSI_RESET = "\u001B[0m";

    /**
     * Constructor to initialize MapView with GameState.
     *
     * @param p_gameState The GameState object representing the current state of the game.
     */
    public MapView(GameState p_gameState){
        d_gameState = p_gameState;
        d_map = p_gameState.getD_map();
        d_map = p_gameState.getD_map();
        d_countries = d_map.getD_countries();
        d_continents = d_map.getD_continents();
    }

    /**
     * Constructor to initialize MapView with GameState and list of players.
     *
     * @param p_gameState The GameState object representing the current state of the game.
     * @param p_players The list of Player objects representing the players in the game.
     */
        public MapView(GameState p_gameState, List<Player> p_players){
        d_gameState = p_gameState;
        d_players = p_players;
        d_map = p_gameState.getD_map();
        d_countries = d_map.getD_countries();
        d_continents = d_map.getD_continents();
    }


    /**
     * Returns the string with specified color applied, or the original string if the color is null.
     *
     * @param p_color The ANSI escape code representing the color to apply.
     * @param p_s The input string to which the color will be applied.
     * @return The input string with the specified color applied, or the original string if the color is null.
     */
    private String getColourString(String p_color, String p_s) {
        if(p_color == null) return p_s;

        return p_color + p_s + ANSI_RESET;
    }

    /**
     * Prints a string centered within a specified width.
     *
     * @param p_width The width of the space in which the string should be centered.
     * @param p_s The string to be centered.
     */
    private void giveCenteredString (int p_width, String p_s) {
        String l_centeredString = String.format("%-" + p_width  + "s", String.format("%" + (p_s.length() + (p_width - p_s.length()) / 2) + "s", p_s));

        System.out.format(l_centeredString+"\n");
    }

    /**
     * Prints a separator line consisting of dashes to the console.
     * The length of the separator is determined by the constant CONSOLE_WIDTH
     * defined in the ApplicationConstants class.
     */
    private void giveSeparator(){
        StringBuilder l_separator = new StringBuilder();

        for (int i = 0; i< ApplicationConstants.CONSOLE_WIDTH -2; i++){
            l_separator.append("-");
        }
        System.out.format("+%s+%n", l_separator.toString());
    }

    /**
     * Prints the name of a continent to the console, optionally colored based on the owning player.
     * The continent name is centered within a separator line.
     *
     * @param p_continentName The name of the continent to be printed.
     */
        private void giveContinentName(String p_continentName){
        String l_continentName = p_continentName+" ( "+ApplicationConstants.CONTROL_VALUE+" : "+ d_gameState.getD_map().getContinent(p_continentName).getD_continentValue()+" )";

        giveSeparator();
        if(d_players != null){
            l_continentName = getColourString(getContinentColor(p_continentName), l_continentName);
        }
        giveCenteredString(ApplicationConstants.CONSOLE_WIDTH, l_continentName);
        giveSeparator();
    }

    /**
     * Formats the name of a country with an index and additional information, such as the number of armies.
     * Optionally colorizes the formatted country name based on the owning player's color.
     *
     * @param p_index       The index of the country.
     * @param p_countryName The name of the country.
     * @return The formatted country name.
     */
    private String getFormattedCountryName(int p_index, String p_countryName){
        String l_indexedString = String.format("%02d. %s", p_index, p_countryName);

        if(d_players != null){
            String l_armies = "( "+ApplicationConstants.ARMIES+" : "+ getCountryArmies(p_countryName)+" )";
            l_indexedString = String.format("%02d. %s %s", p_index, p_countryName, l_armies);
        }
        return getColourString(getCountryColor(p_countryName), String.format("%-30s", l_indexedString));
    }

    /**
     * Formats and displays the names of adjacent countries for a given country.
     * Optionally colorizes the formatted adjacent country names based on the owning player's color.
     *
     * @param p_countryName   The name of the country.
     * @param p_adjCountries  The list of adjacent countries.
     */
    private void giveFormattedAdjacentCountryName(String p_countryName, List<Country> p_adjCountries){
        StringBuilder l_commaSeparatedCountries = new StringBuilder();

        for(int i=0; i<p_adjCountries.size(); i++) {
            l_commaSeparatedCountries.append(p_adjCountries.get(i).getD_countryName());
            if(i<p_adjCountries.size()-1)
                l_commaSeparatedCountries.append(", ");
        }
        String l_adjacentCountry = ApplicationConstants.CONNECTIVITY+" : "+ WordWrap.from(l_commaSeparatedCountries.toString()).maxWidth(ApplicationConstants.CONSOLE_WIDTH).wrap();
        System.out.println(getColourString(getCountryColor(p_countryName),l_adjacentCountry));
        System.out.println();
    }

    /**
     * Retrieves the color of the owning player for the specified country.
     *
     * @param p_countryName The name of the country.
     * @return The color of the owning player, or null if the country is not owned by any player.
     */
    private String getCountryColor(String p_countryName){
        if(getCountryOwner(p_countryName) != null){
            return getCountryOwner(p_countryName).getD_color();
        }else{
            return null;
        }
    }


    /**
     * Retrieves the color of the owning player for the specified continent.
     *
     * @param p_continentName The name of the continent.
     * @return The color of the owning player, or null if the continent is not owned by any player.
     */
    private String getContinentColor(String p_continentName){
        if(getContinentOwner(p_continentName) != null){
            return getContinentOwner(p_continentName).getD_color();
        }else{
            return null;
        }
    }

    /**
     * Retrieves the owner of the specified country.
     *
     * @param p_countryName The name of the country.
     * @return The owner player of the country, or null if the country is not owned by any player.
     */
    private Player getCountryOwner(String p_countryName){
        if (d_players != null) {
            for (Player p: d_players){
                if(p.getCountryNames().contains(p_countryName)){
                    return p;
                }
            }
        }
        return null;
    }

    /**
     * Renders information about a player.
     *
     * @param p_index The index of the player.
     * @param p_player The player object containing the information to be rendered.
     */
    private void renderPlayerInfo(Integer p_index, Player p_player){
        String l_playerInfo = String.format("%02d. %-8s %s", p_index,p_player.getPlayerName(), " -> "+ getColourString(p_player.getD_color(), " COLOR "));
        System.out.println(l_playerInfo);
    }


    /**
     * Renders information about all players in the game.
     * This includes their names, indices, and colors.
     */
    private void renderPlayers(){
        int l_counter = 0;

        giveSeparator();
        giveCenteredString(ApplicationConstants.CONSOLE_WIDTH, "GAME PLAYERS");
        giveSeparator();

        for(Player p: d_players){
            l_counter++;
            renderPlayerInfo(l_counter, p);
        }
    }

    /**
     * Retrieves the owner of the specified continent.
     *
     * @param p_continentName The name of the continent.
     * @return The player who owns the specified continent, or null if no player owns it.
     */
    private Player getContinentOwner(String p_continentName){
        if (d_players != null) {
            for (Player p: d_players){
                if(!CommonUtil.isNull(p.getContinentNames()) && p.getContinentNames().contains(p_continentName)){
                    return p;
                }
            }
        }
        return null;
    }


    /**
     * Retrieves the number of armies in the specified country.
     *
     * @param p_countryName The name of the country.
     * @return The number of armies in the country, or 0 if the country does not exist or has no armies.
     */
    private Integer getCountryArmies(String p_countryName){
        Integer l_armies = d_gameState.getD_map().getCountryByName(p_countryName).getD_armies();

        if(l_armies == null)
            return 0;
        return l_armies;
    }



    /**
     * Displays the game map, including continents, countries, and their connections.
     * If players are present, also renders player information.
     */
    public void showMap() {

        if(d_players != null){
            renderPlayers();
        }

        // renders the continent if any
        if (!CommonUtil.isNull(d_continents)) {
            d_continents.forEach(l_continent -> {
                giveContinentName(l_continent.getD_continentName());

                List<Country> l_continentCountries = l_continent.getD_countries();
                final int[] l_countryIndex = {1};

                // renders the country if any
                if (!CommonUtil.isCollectionEmpty(l_continentCountries)) {
                    l_continentCountries.forEach((l_country) -> {
                        String l_formattedCountryName = getFormattedCountryName(l_countryIndex[0]++, l_country.getD_countryName());
                        System.out.println(l_formattedCountryName);
                        try {
                            List<Country> l_adjCountries = d_map.getAdjacentCountry(l_country);

                            giveFormattedAdjacentCountryName(l_country.getD_countryName(), l_adjCountries);
                        } catch (InvalidMap l_invalidMap) {
                            System.out.println(l_invalidMap.getMessage());
                        }
                    });
                } else {
                    System.out.println("No countries are present in the continent!");
                }
            });
        } else {
            System.out.println("No continents to display!");
        }
    }

}

