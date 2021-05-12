package Commands.SH.Commands;

import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import Commands.utils.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EndGame extends Command {
    public EndGame(String key, String desc) {
        super(key,desc);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        super.start(event);
        getGame().setGamestate(new Gamestate());
        getGame().getLobby().resetPlayers();
        MessageHelper.sendMessage(event.getTextChannel(), "Ended the current game");
    }
}
