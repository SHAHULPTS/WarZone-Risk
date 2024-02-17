package Services;

import Constants.ApplicationConstants;
import Models.*;
import Utils.CommonUtil;

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
}