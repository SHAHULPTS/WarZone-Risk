package Models;

/**
 * Represents an order issued by the player.
 */
public class Order {

    /**
     * The actions to be performed by this order.
     */
    String d_orderAction;

    /**
     * The name of the target country for this order.
     */
    String d_targetCountryName;

    /**
     * The name of the source country for this order.
     */
    String d_sourceCountryName;

    /**
     * The number of armies to be placed in the enemy country.
     */
    Integer d_numberOfArmiesToPlace;

    /**
     * An instance of Order object.
     */
    Order orderObj;


    /**
     * Default constructor for Order Object.
     */

     public Order() {
     }

     /**
     * Parameterized constructor constructs a Order with a specified action , target country name
      * and Number of armies to place
     * @param p_orderAction The action to be performed by this order
     * @param p_targetCountryName The name of the target country for this order.
     * @param p_numberOfArmiesToPlace The number of armies to be placed in the target country.
     */
    public Order(String p_orderAction, String p_targetCountryName, Integer p_numberOfArmiesToPlace) {
        this.d_orderAction = p_orderAction;
        this.d_targetCountryName = p_targetCountryName;
        this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
    }

    /**
     * Gets the order action.
     * @return The order action.
     */
    public String getD_orderAction() {
        return d_orderAction;
    }

    /**
     * Sets the order action.
     * @param p_orderAction The order action.
     */
    public void setD_orderAction(String p_orderAction) {
        this.d_orderAction = p_orderAction;
    }

    /**
     * Gets the target country name.
     * @return The target country name.
     */
    public String getD_targetCountryName() {
        return d_targetCountryName;
    }

    /**
     * Sets the target country name.
     * @param p_targetCountryName The target country name.
     */
    public void setD_targetCountryName(String p_targetCountryName) {
        this.d_targetCountryName = p_targetCountryName;
    }

    /**
     * Gets the source country name.
     * @return The source country name.
     */
    public String getD_sourceCountryName() {
        return d_sourceCountryName;
    }

    /**
     * Sets the source country name.
     * @param p_sourceCountryName The source country name.
     */
    public void setD_sourceCountryName(String p_sourceCountryName) {
        this.d_sourceCountryName = p_sourceCountryName;
    }

    /**
     * Gets the number of armies to place.
     * @return The number of armies to place.
     */
    public Integer getD_numberOfArmiesToPlace() {
        return d_numberOfArmiesToPlace;
    }

    /**
     * Sets the number of armies to place.
     * @param p_numberOfArmiesToPlace The number of armies to place.
     */
    public void setD_numberOfArmiesToPlace(Integer p_numberOfArmiesToPlace) {
        this.d_numberOfArmiesToPlace = p_numberOfArmiesToPlace;
    }

    /**
     * Executes the order based on the game state and player.
     * @param p_gameState The current game state.
     * @param p_player The player issuing the order.
     */
    public void execute(GameState p_gameState, Player p_player) {
        if ("deploy".equals(this.d_orderAction) && checkDeployOrderCountry(p_player, this)) {
            deployOrder(this,p_gameState, p_player);
            System.out.println("\nOrder executed successfully. " + this.d_numberOfArmiesToPlace + " armies deployed to " + this.d_targetCountryName);
        } else {
            System.out.println("\nOrder not executed. Invalid command or target country does not belong to player: " + p_player.getPlayerName());
        }

    }

    /**
     * Deploys armies based on the specific order.
     * @param p_order The order containing deployment details.
     * @param p_gameState The current game state.
     * @param p_player The player issuing the order.
     */
    private void deployOrder(Order p_order, GameState p_gameState, Player p_player) {
        for (Country l_country : p_gameState.getD_map().getD_countries()) {
            if (l_country.getD_countryName().equalsIgnoreCase(p_order.getD_targetCountryName())) {
                Integer l_armiesToUpdate = l_country.getD_armies() == null ? p_order.getD_numberOfArmiesToPlace()
                        : l_country.getD_armies() + p_order.getD_numberOfArmiesToPlace();
                l_country.setD_armies(l_armiesToUpdate);
            }
        }
    }

    /**
     * Checks if the specified target country belongs to the player.
     * @param p_player The player whose countries are checked.
     * @param d_orderDetails The order details containing the target country name.
     * @return {@code true} if the target country belongs to the player, otherwise {@code false}.
     */
    public boolean checkDeployOrderCountry(Player p_player, Order d_orderDetails) {
        return p_player.getD_coutriesOwned().stream().anyMatch(c -> c.getD_countryName().equalsIgnoreCase(this.d_targetCountryName));
    }



}
