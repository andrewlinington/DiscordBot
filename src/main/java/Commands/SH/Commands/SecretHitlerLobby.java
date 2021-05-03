package Commands.SH.Commands;

import Commands.utils.Command;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.PlayerList;
import Commands.SH.utils.enums.GameStage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class SecretHitlerLobby extends Command {

    public SecretHitlerLobby(String key, String desc) {
        super(key, desc);
    }

    public static void requestElection(TextChannel channel) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Please pick a chancellor");
        Gamestate.setGameStage(GameStage.Election);
        channel.sendMessage("<@" +Gamestate.getPlayers().get(Gamestate.getPresidentLocation()).getUser().getId() + ">").queue();
        channel.sendMessage(eb.build()).queue();
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
