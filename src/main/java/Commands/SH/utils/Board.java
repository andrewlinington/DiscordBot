package Commands.SH.utils;

import Commands.SH.utils.enums.BoardType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

import static main.DiscordBot.API;
import static main.DiscordBot.SERVER_ID;

public class Board {

    private final ArrayList<Policy> fascist;
    private final ArrayList<Policy> liberal;
    private final BoardType type;
    private boolean hitlerWin = false;


    public Board (int numPlayers) {
        if (numPlayers < 7) {
            type = BoardType.SMALL;
        } else if (numPlayers < 9){
            type = BoardType.MED;
        } else {
            type = BoardType.LARGE;
        }
        fascist = new ArrayList<>();
        liberal = new ArrayList<>();
    }


    public void addPolicy (Policy p, MessageReceivedEvent event) {
       EmbedBuilder eb = new EmbedBuilder();

        if(p.getRole().equals("Fascist")){
            fascist.add(p);
            hitlerWin = fascist.size() >= 3;
            eb.setColor(Color.RED);
        } else {
            liberal.add(p);
            eb.setColor(Color.BLUE);
        }
        Gamestate.checkWin(event, fascist.size(), liberal.size());
        performAction(event);
        eb.setTitle("Policy added: " + p.getRole());
        API.getGuildById(SERVER_ID).getTextChannelsByName("general",true).get(0).sendMessage(eb.build()).queue();
    }

    private void performAction(MessageReceivedEvent event) {

    }


    public boolean isHitlerWin() {
        return hitlerWin;
    }
}
