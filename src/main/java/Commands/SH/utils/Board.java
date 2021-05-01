package Commands.SH.utils;

import Commands.SH.utils.enums.BoardType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;

public class Board {

    private ArrayList<Policy> fascist;
    private ArrayList<Policy> liberal;
    private BoardType type;
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
        if(p.getRole().equals("Fascist")){
            fascist.add(p);
            hitlerWin = fascist.size() >= 3;
        } else {
            liberal.add(p);
        }
        Gamestate.checkWin(event, fascist.size(), liberal.size());
        performAction(event);

    }

    private void performAction(MessageReceivedEvent event) {

    }


    public boolean isHitlerWin() {
        return hitlerWin;
    }
}
