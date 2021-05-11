package Commands.SH.Commands;

import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import Commands.utils.Command;
import main.utils.ServerGame;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

//TODO: REFACTOR
public class PresidentPick extends Command {
    /**
     * Generates the Command for a specific Key
     * creates a command with a description.
     * @param key  the string which is to be matched to run the command
     * @param desc the description of PresidentPick
     */
    public PresidentPick(String key, String desc) {
        super(key, desc);
    }

    @Override
    public boolean keyMatches(String string) {
        return string.startsWith(getKey());
    }

    @Override
    public void start(MessageReceivedEvent event) {
        Gamestate gs = ServerGame.getGuildGames().get(event.getGuild());

        if(!gs.getGameStage().equals(GameStage.Pick)){
            MessageHelper.sendMessage(event.getTextChannel(), "There is no power to Pick a president at this time");
            return;
        }
        if(event.getMessage().getMentionedUsers().isEmpty()){
            MessageHelper.sendMessage(event.getTextChannel(), "Usage is !pick <@user>");
            return;
        }

        String id = event.getMessage().getMentionedUsers().get(0).getId();
        Player president = gs.findPlayer(event.getMember().getId());
        if(president == null) {
            MessageHelper.sendMessage(event.getTextChannel(), "You are not playing.");
        } else if (!president.getStatus().equals(SecretHitlerStatus.President)) {
            MessageHelper.sendMessage(event.getTextChannel(), "You are not the President.");
        } else {
            Player p = gs.findPlayer(id);
            if(!p.userStatus().equals("Dead") && !president.equals(p)) {
                gs.updateNextChosenPres(p.getUser());
                gs.nextPres();
                gs.updateNextChosenPres();
                SecretHitlerLobby.requestElection(event.getTextChannel());
            } else {
                MessageHelper.sendMessage(event.getTextChannel(), (p.userStatus().equals("Dead") ? "A dead player cannot be president" : "You cannot elect yourself"));
            }
        }
    }
}
