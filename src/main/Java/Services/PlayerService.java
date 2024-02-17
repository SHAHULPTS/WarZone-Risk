package Services;

import Constants.ApplicationConstants;
import Models.*;
import Utils.CommonUtil;
import java.util.Random;
import Exceptions.InvalidCommand;
import java.util.ArrayList;
import java.util.List;

public class PlayerService {

    public boolean isPlayerNameUnique(List<Player> p_existingPlayerList, String p_playerName)
    {
        boolean l_isUnique = true;
        if (!CommonUtil.isCollectionEmpty(p_existingPlayerList))
        {
            for (Player l_player : p_existingPlayerList)
            {
                if (l_player.getPlayerName().equalsIgnoreCase(p_playerName))
                {
                    l_isUnique = false;
                    break;
                }
            }
        }
        return l_isUnique;
    }

    public List<Player> addRemovePlayers(List<Player> p_existingPlayerList, String p_operation, String p_argument)
    {
        List<Player> l_updatedPlayers = new ArrayList<>();
        if (!CommonUtil.isCollectionEmpty(p_existingPlayerList))
            l_updatedPlayers.addAll(p_existingPlayerList);

        String l_enteredPlayerName = p_argument.split(" ")[0];
        boolean l_playerNameAlreadyExist = !isPlayerNameUnique(p_existingPlayerList, l_enteredPlayerName);

        switch (p_operation.toLowerCase())
        {
            case "add":
                addGamePlayer(l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExist);
                break;
            case "remove":
                removeGamePlayer(p_existingPlayerList, l_updatedPlayers, l_enteredPlayerName, l_playerNameAlreadyExist);
                break;
            default:
                System.out.println("Unrecognized action on the Players list.");
        }
        return l_updatedPlayers;
    }
    private void removeGamePlayer(List<Player> p_existingPlayerList, List<Player> p_updatedPlayers, String p_enteredPlayerName, boolean p_playerNameAlreadyExist)
    {
        if (p_playerNameAlreadyExist)
        {
            for (Player l_player : p_existingPlayerList)
            {
                if (l_player.getPlayerName().equalsIgnoreCase(p_enteredPlayerName))
                {
                    p_updatedPlayers.remove(l_player);
                    System.out.println("Player : " + p_enteredPlayerName + " has been successfully removed from the list");
                }
            }
        } else {
            System.out.print("Player : " + p_enteredPlayerName + " does not Exist. No changes made so far.");
        }
    }
    private void addGamePlayer(List<Player> p_updatedPlayers, String p_enteredPlayerName, boolean p_playerNameAlreadyExist)
    {
        if (p_playerNameAlreadyExist)
        {
            System.out.print("Player : " + p_enteredPlayerName + " already exists. No changes made.");
        }
        else
        {
            Player l_addNewPlayer = new Player(p_enteredPlayerName);
            p_updatedPlayers.add(l_addNewPlayer);
            System.out.println("Player : " + p_enteredPlayerName + " has been added to the list successfully.");
        }
    }
}