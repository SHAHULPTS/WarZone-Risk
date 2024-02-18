package Models;

public class Order {
    String d_orderAction;
    String d_targetCountryName;
    String d_sourceCountryName;
    Integer d_numberOfArmiesToPlace;
    Order orderObj;

    public Order() {
    }

    public Order(String p_orderAction, String p_targetCountryName, Integer p_numberOfArmiesToPlace) {
        this.d_orderAction = p_orderAction;
        this.d_targetCountryName = p_targetCountryName;
        this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
    }

    public String getD_orderAction() {
        return d_orderAction;
    }

    public void setD_orderAction(String p_orderAction) {
        this.d_orderAction = p_orderAction;
    }

    public String getD_targetCountryName() {
        return d_targetCountryName;
    }

    public void setD_targetCountryName(String p_targetCountryName) {
        this.d_targetCountryName = p_targetCountryName;
    }

    public String getD_sourceCountryName() {
        return d_sourceCountryName;
    }

    public void setD_sourceCountryName(String p_sourceCountryName) {
        this.d_sourceCountryName = p_sourceCountryName;
    }

    public Integer getD_numberOfArmiesToPlace() {
        return d_numberOfArmiesToPlace;
    }

    public void setD_numberOfArmiesToPlace(Integer p_numberOfArmiesToPlace) {
        this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
    }

    public void execute(GameState p_gameState, Player p_player) {
        if ("deploy".equals(this.d_orderAction) && checkDeployOrderCountry(p_player, this)) {
            deployOrder(this,p_gameState, p_player);
            System.out.println("\nOrder executed successfully. " + this.d_numberOfArmiesToPlace + " armies deployed to " + this.d_targetCountryName);
        } else {
            System.out.println("\nOrder not executed. Invalid command or target country does not belong to player: " + p_player.getPlayerName());
        }

    }

    private void deployOrder(Order p_order, GameState p_gameState, Player p_player) {
        for (Country l_country : p_gameState.getD_map().getD_countries()) {
            if (l_country.getD_countryName().equalsIgnoreCase(p_order.getD_targetCountryName())) {
                Integer l_armiesToUpdate = l_country.getD_armies() == null ? p_order.getD_numberOfArmiesToPlace()
                        : l_country.getD_armies() + p_order.getD_numberOfArmiesToPlace();
                l_country.setD_armies(l_armiesToUpdate);
            }
        }
    }

    public boolean checkDeployOrderCountry(Player p_player, Order d_orderDetails) {
        return p_player.getD_coutriesOwned().stream().anyMatch(c -> c.getD_countryName().equalsIgnoreCase(this.d_targetCountryName));
    }



}
