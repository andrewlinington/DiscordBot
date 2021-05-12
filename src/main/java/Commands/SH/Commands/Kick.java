package Commands.SH.Commands;

import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.PlayerList;
import Commands.SH.utils.enums.GameStage;
import Commands.utils.Command;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Kick extends Command {

    public Kick(String key, String desc) {
        super(key, desc);
    }


    @Override
    public boolean keyMatches(String string) {
        return  string.startsWith(getKey());
    }

    @Override
    public void start(MessageReceivedEvent event) {
        super.start(event);
        Gamestate gs = getGame().getGamestate();
        PlayerList pl = getGame().getLobby();
        if(!gs.getGameStage().equals(GameStage.Idle)){
            MessageHelper.sendMessage(event.getTextChannel(), "You cannot kick someone while the game is active.");
            return;
        }
        if(event.getMessage().getMentionedUsers().isEmpty()){
            MessageHelper.sendMessage(event.getTextChannel(), "Usage is !kick <@user>");
            return;
        }
        User id = event.getMessage().getMentionedUsers().get(0);
        if(  pl.removePlayer(id)  != null) {
            MessageHelper.sendMessage(event.getTextChannel(), "Kicked " + id.getName());
        } else {
            MessageHelper.sendMessage(event.getTextChannel(), "Player is not in the given game");
        }

    }
}
