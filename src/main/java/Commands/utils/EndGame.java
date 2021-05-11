package Commands.utils;

import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import main.utils.ServerGame;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EndGame extends Command {
    public EndGame(String key, String desc) {
        super(key,desc);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        ServerGame.getGuildGames().put(event.getGuild(), new Gamestate());
        ServerGame.getLobby().get(event.getGuild()).resetPlayers();
        MessageHelper.sendMessage(event.getTextChannel(), "Ended the current game");
    }
}
