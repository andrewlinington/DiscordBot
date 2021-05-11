package Commands.SH.utils;

import Commands.SH.Helper.EmbededHelper;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.Optional;

public final class PlayerList {


    private final ArrayList<Player> players;

    public PlayerList() {
        players = new ArrayList<>();
    }

    public void addPlayer(Player player){
        players.add(player);
    }

    public Player removePlayer(User user){
        Optional<Player> optPlayer =  players.stream().filter(p -> p.hasID(user.getId())).findAny();
        optPlayer.ifPresent(players::remove);
        return optPlayer.orElse(null);
    }

    public boolean isPlayer (User user) {
        return players.stream().filter(p -> p.hasID(user.getId())).findAny().orElse(null) != null;
    }

    public ArrayList<Player> getPlayers () {
        return players;
    }

    public int getPlayerCount() {
        return players.size();
    }

    public void resetPlayers(){
        players.forEach(Player::reset);
    }

    public void resetVotePlayers() {players.forEach(Player::resetVote);}

    public static  ArrayList<MessageEmbed.Field> toField (ArrayList<Player> playerList) {
        ArrayList<MessageEmbed.Field> fields = new ArrayList<>();
        playerList.forEach(p -> fields.add(EmbededHelper.generateField(p.getName(),p.userStatus())));
        return fields;
    }
    public static  ArrayList<MessageEmbed.Field> toFascistField (ArrayList<Player> playerList) {
        ArrayList<MessageEmbed.Field> fields = new ArrayList<>();
        playerList.forEach(p -> fields.add(EmbededHelper.generateField(p.getName(),p.getRole().getSecretRole())));
        return fields;
    }


}
