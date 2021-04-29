package Commands.SH.Objects;

import java.util.ArrayList;

public final class PlayerList {
    private PlayerList() {}

    private static int playerCount;
    private static ArrayList<Player> players;

    static {
        players = new ArrayList<>();
        playerCount = 0;
    }

    public static void addPlayer(Player player){
        players.add(player);
        playerCount++;
    }

    public static void removePlayer(Player player){
        players.remove(player);
        playerCount--;
    }

    public static Player findPlayer (String id){
        for (Player p: players) {
            if(p.hasID(id)) {
                return p;
            }
        }
        return null;
    }

    public static ArrayList<Player> getPlayers () {
        return players;
    }

    public static int getPlayerCount() {
        return playerCount;
    }
}
