package Commands.SH.Commands;

import Commands.Command;
import Commands.SH.utils.Player;
import Commands.SH.utils.PlayerList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class SecretHitlerLeave  extends Command {
    public SecretHitlerLeave(String key) {
        super(key);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        String id = event.getMessage().getAuthor().getId();
        ArrayList<Player> players = PlayerList.getPlayers();
        for (Player p: players){
            if (p.hasID(id)) {
                eb.setTitle(p.getName() + " has left the lobby.");
                PlayerList.removePlayer(p);
                event.getChannel().sendMessage(eb.build()).queue();
                SecretHitlerLobby.showLobby(event.getChannel());
                return;
            }
        }
        eb.setTitle("You are not in the lobby");
        event.getChannel().sendMessage(eb.build()).queue();
        SecretHitlerLobby.showLobby(event.getChannel());
    }
}
