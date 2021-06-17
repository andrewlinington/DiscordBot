package Commands.SH.Commands;

import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.PlayerList;
import Commands.SH.utils.enums.SecretHitlerStatus;
import Commands.utils.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
//TODO: REFACTOR
public class SecretHitlerLeave  extends Command {
    public SecretHitlerLeave(String key, String desc) {
        super(key,desc);
    }


    //ToDo: make sure that when a player leaves win conditions for hitler dying are checked
    @Override
    public void start(MessageReceivedEvent event) {
        super.start(event);
        MessageHelper.sendMessage(event.getTextChannel(),removePlayer(getGame().getLobby(), event));
    }

    private String removePlayer(PlayerList p, MessageReceivedEvent event) {
       Player player = p.removePlayer(event.getAuthor());
       if(player != null) {
           checkPlayerRemoval(player,getGame().getGamestate(),event);
           return player.getName() + " has left the lobby.";
       }
        return "You are not in the lobby";
    }

    private void checkPlayerRemoval(Player p, Gamestate gs, MessageReceivedEvent event) {
        if( p.getRole() != null && p.getRole().getSecretRoleName().equals("Hitler")){
            MessageHelper.sendMessage(event.getTextChannel(),"Hitler has left. Game restarting");
            gs.resetGame(event.getTextChannel());
        } else if ( p.getStatus().equals(SecretHitlerStatus.President) || p.getStatus().equals(SecretHitlerStatus.Chancellor) ) {
            //TODO: check if each gamestate could result in issues and end game properly
            //A player has left reseting for sake of cohesive game
//            if(!gs.getGameStage().equals(GameStage.Election)) {
                MessageHelper.sendMessage(event.getTextChannel(),"Game restarted due to key player leaving");
                gs.resetGame(event.getTextChannel());
//            }
        }else if (!p.getStatus().equals(SecretHitlerStatus.Waiting)) {
            gs.setPlayerCount(gs.getPlayerCount() - 1);
        }

    }
}
