package Views;
import Constants.ApplicationConstants;
import Models.GameState;
import Models.Tournament;
import org.davidmoten.text.utils.WordWrap;

import java.util.List;
public class TournamentView {
    Tournament d_tournament;
    List<GameState> d_gameStateObjects;
    public static final String ANSI_RESET = "\u001B[0m";
    public TournamentView(Tournament p_tournament){
        d_tournament = p_tournament;
        d_gameStateObjects = d_tournament.getD_gameStateList();
    }

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

    private void renderMapName(Integer p_gameIndex, String p_mapName){
        String l_formattedString = String.format("%s %s %d %s", p_mapName, " (Game Number: ",p_gameIndex, " )" );
        renderSeparator();
        renderCenteredString(ApplicationConstants.CONSOLE_WIDTH, l_formattedString);
        renderSeparator();
    }

    private void renderGames(GameState p_gameState){
        String l_winner;
        String l_conclusion;
        if(p_gameState.getD_winner()==null){
            l_winner = " ";
            l_conclusion = "Draw!";
        } else{
            System.out.println("Entered Here");
            l_winner = p_gameState.getD_winner().getPlayerName();
            l_conclusion = "Winning Player Strategy: "+ p_gameState.getD_winner().getD_playerBehaviorStrategy();
        }
        String l_winnerString = String.format("%s %s", "Winner -> ", l_winner);
        StringBuilder l_commaSeparatedPlayers = new StringBuilder();

        for(int i=0; i<p_gameState.getD_playersFailed().size(); i++) {
            l_commaSeparatedPlayers.append(p_gameState.getD_playersFailed().get(i).getPlayerName());
            if(i<p_gameState.getD_playersFailed().size()-1)
                l_commaSeparatedPlayers.append(", ");
        }
        String l_losingPlayers = "Losing Players -> "+ WordWrap.from(l_commaSeparatedPlayers.toString()).maxWidth(ApplicationConstants.CONSOLE_WIDTH).wrap();
        String l_conclusionString = String.format("%s %s", "Conclusion of Game -> ", l_conclusion);
        System.out.println(l_winnerString);
        System.out.println(l_losingPlayers);
        System.out.println(l_conclusionString);
    }

    public void viewTournament(){
        int l_counter = 0;
        System.out.println();
        if(d_tournament!=null && d_gameStateObjects!=null){
            for(GameState l_gameState: d_tournament.getD_gameStateList()){
                l_counter++;
                renderMapName(l_counter, l_gameState.getD_map().getD_mapFile());
                renderGames(l_gameState);
            }
        }
    }

}
