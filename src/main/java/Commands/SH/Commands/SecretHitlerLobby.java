package Commands.SH.Commands;

import Commands.Command;
import Commands.SH.utils.Player;
import Commands.SH.utils.PlayerList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class SecretHitlerLobby extends Command {
    public SecretHitlerLobby(String key) {
        super(key);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        showLobby(event.getChannel());
    }

    public static void showLobby (MessageChannel event) {
        EmbedBuilder eb = new EmbedBuilder();
        String status;
        String name;
        ArrayList<Player> players = PlayerList.getPlayers();
        int numPlayers = PlayerList.getPlayerCount();
        eb.setTitle("Secret Hitler Lobby " + numPlayers + "/5-10");
        for(Player p: players) {
            name =  p.getName();
            status = p.userStatus();
            eb.addField(name ,status,false);
        }
        eb.setColor(Color.RED);
        event.sendMessage(eb.build()).queue();
    }
}
