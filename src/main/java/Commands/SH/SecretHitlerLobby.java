package Commands.SH;

import Commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class SecretHitlerLobby extends Command {
    public SecretHitlerLobby(String key) {
        super(key);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        showLobby(event);
    }

    public static void showLobby (MessageReceivedEvent event) {
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
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
