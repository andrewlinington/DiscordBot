package Commands.SH.Commands;
import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.PlayerList;
import Commands.SH.utils.enums.GameStage;
import Commands.utils.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class KickAll extends Command {

    public KickAll(String key, String desc) {
        super(key, desc);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        super.start(event);
        boolean isInactive = (getGame().getGamestate().getGameStage().equals(GameStage.Idle));
        MessageHelper.sendMessage(event.getTextChannel(), isInactive ? "Lobby has been cleared" : "You cannot kick people while the game is active");
        getGame().setLobby(isInactive ? new PlayerList(): getGame().getLobby());
    }
}