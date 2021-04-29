package Commands.SH;

import Commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class SecretHitlerJoin extends Command {
    public SecretHitlerJoin(String s) {
        super(s);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        String name = event.getAuthor().getName();
        String id = event.getMessage().getAuthor().getId();
        ArrayList<Player> players = PlayerList.getPlayers();
        for (Player p: players){
            if (p.hasID(id)) {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("You have already Joined");
                event.getChannel().sendMessage(eb.build()).queue();
                SecretHitlerLobby.showLobby(event);
                return;
            }
        }
        if(PlayerList.getPlayerCount() < 10) {
            PlayerList.addPlayer(new Player(name, id));
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle(name + " has joined the lobby.");
            event.getChannel().sendMessage(eb.build()).queue();
        }
        else {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Maximum number of players reached.");
            event.getChannel().sendMessage(eb.build()).queue();
        }
        SecretHitlerLobby.showLobby(event);
    }


}