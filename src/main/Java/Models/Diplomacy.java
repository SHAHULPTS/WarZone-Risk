package Models;

import Services.PlayerService;

public class Diplomacy implements Card {

    Player d_IssuingPlayer;
    String d_targetPlayer;

    String d_orderExecutionLog;


    public Diplomacy(String p_targetPlayer, Player p_IssuingPlayer){
        this.d_targetPlayer = p_targetPlayer;
        this.d_IssuingPlayer = p_IssuingPlayer;
    }

    @Override
    public void execute(GameState p_gameState) {
        PlayerService l_playerService = new PlayerService();
        Player l_targetPlayer = l_playerService.findPlayerByName(d_targetPlayer, p_gameState);
        l_targetPlayer.addPlayerNegotiation(d_IssuingPlayer);
        d_IssuingPlayer.addPlayerNegotiation(l_targetPlayer);
        d_IssuingPlayer.removeCard("negotiate");
        this.setD_orderExecutionLog("Negotiation with "+ d_targetPlayer+ " approached by "+d_IssuingPlayer.getPlayerName()+" successful!", "default");
        p_gameState.updateLog(d_orderExecutionLog, "effect");
    }

    @Override
    public boolean valid(GameState p_gameState) {
        return true;
    }

    public void printOrder() {
        this.d_orderExecutionLog = "----------Diplomacy order issued by player " + this.d_IssuingPlayer.getPlayerName()
                + "----------" + System.lineSeparator() + "Request to " + " negotiate attacks from "
                + this.d_targetPlayer;
        System.out.println(System.lineSeparator()+this.d_orderExecutionLog);
    }

    @Override
    public String orderExecutionLog() {
        return this.d_orderExecutionLog;
    }

    @Override
    public Boolean checkValidOrder(GameState p_gameState) {
        PlayerService l_playerService = new PlayerService();
        Player l_targetPlayer = l_playerService.findPlayerByName(d_targetPlayer, p_gameState);
        if(!p_gameState.getD_players().contains(l_targetPlayer)){
            this.setD_orderExecutionLog("Player to negotiate doesn't exist!", "error");
            p_gameState.updateLog(orderExecutionLog(), "effect");
            return false;
        }
        return true;
    }

    public void setD_orderExecutionLog(String p_orderExecutionLog, String p_logType) {
        this.d_orderExecutionLog = p_orderExecutionLog;
        if (p_logType.equals("error")) {
            System.err.println(p_orderExecutionLog);
        } else {
            System.out.println(p_orderExecutionLog);
        }
    }
    private String currentOrder() {
        return "Diplomacy Order : " + "negotiate" + " " + this.d_targetPlayer;
    }

    @Override
    public String getOrderName() {
        return "diplomacy";
    }
}
