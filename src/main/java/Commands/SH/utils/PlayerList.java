package Commands.SH.utils;

import java.util.ArrayList;

public final class PlayerList {
    private PlayerList() {}

    private static int playerCount;
    private static final ArrayList<Player> players;

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

    public static ArrayList<Player> getPlayers () {
        return players;
    }

    public static int getPlayerCount() {
        return playerCount;
    }


    public static void resetPlayers(){
        for (Player p :
                players) {
            p.reset();
        }
    }

}
