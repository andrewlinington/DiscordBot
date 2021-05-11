package Commands.SH.Commands;

import Commands.SH.Helper.EmbededHelper;
import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import Commands.utils.Command;
import main.utils.ServerGame;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;


//TODO: REFACTOR
public class ShootPlayer extends Command {
    /**
     * Generates the Command for a specific Key
     *
     * @param key  the string which is to be matched to run the command
     * @param desc the description of the command
     */
    public ShootPlayer(String key, String desc) {
        super(key, desc);
    }

    /**
     * determine if the key matches the start
     * @param string the string from the user
     * @return if strings match
     */
    @Override
    public boolean keyMatches(String string) {
        return  string.startsWith(getKey());
    }

    @Override
    public void start(MessageReceivedEvent event) {
        Gamestate gs = ServerGame.getGuildGames().get(event.getGuild());
        if(!gs.getGameStage().equals(GameStage.Shoot)){
            MessageHelper.sendMessage(event.getTextChannel(), "There is no power to shoot at this time");
            return;
        }
        if(event.getMessage().getMentionedUsers().isEmpty()){
            MessageHelper.sendMessage(event.getTextChannel(), "Usage is !shoot <@user>");
            return;
        }

        String id = event.getMessage().getMentionedUsers().get(0).getId();
        Player president = gs.findPlayer(event.getMember().getId());
        if(president == null) {
            MessageHelper.sendMessage(event.getTextChannel(), "You are not playing.");
        } else if (!president.getStatus().equals(SecretHitlerStatus.President)) {
            MessageHelper.sendMessage(event.getTextChannel(), "You are not the President.");
        } else {
            Player hopefullyHitler = gs.findPlayer(id);
            if(hopefullyHitler.getRole().getSecretRole().equals("Hitler")) {
                EmbededHelper.sendEmbed(event.getTextChannel(), EmbededHelper.createEmbeded("You have killed " + hopefullyHitler.getName() + "!", Color.blue, "They were Hitler!"),false);
                gs.resetGame(event.getTextChannel());
                return;
            }
            gs.DeadCountAdd();
            EmbededHelper.sendEmbed(event.getTextChannel(), EmbededHelper.createEmbeded("You have killed " + hopefullyHitler.getName() + "!", Color.blue),false);
            hopefullyHitler.setStatus(SecretHitlerStatus.Dead);
            gs.nextPres();
            gs.updateNextPres();
            SecretHitlerLobby.requestElection(event.getTextChannel());
        }
    }
}
