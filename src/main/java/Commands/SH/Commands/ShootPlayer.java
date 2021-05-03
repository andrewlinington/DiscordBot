package Commands.SH.Commands;

import Commands.utils.Command;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static Commands.SH.utils.Gamestate.DeadCountAdd;
import static Commands.SH.utils.Gamestate.resetGame;

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
        EmbedBuilder eb = new EmbedBuilder();
        if(!Gamestate.getGameStage().equals(GameStage.Shoot)){
            eb.setTitle("There is no power to shoot at this time");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        if(event.getMessage().getMentionedUsers().isEmpty()){
            event.getChannel().sendMessage("Usage is !shoot <@user>").queue();
            return;
        }

        String id = event.getMessage().getMentionedUsers().get(0).getId();
        Player president = Gamestate.findPlayer(event.getMember().getId());
        if(president == null) {
            eb.setTitle("You are not playing.");
            event.getChannel().sendMessage(eb.build()).queue();
        } else if (!president.getStatus().equals(SecretHitlerStatus.President)) {
            eb.setTitle("You are not the President.");
            event.getChannel().sendMessage(eb.build()).queue();
        } else {
            Player hopefullyHitler = Gamestate.findPlayer(id);
            eb.setTitle("You have killed " + hopefullyHitler.getName() + "!");
            if(hopefullyHitler.getRole().getSecretRole().equals("Hitler")) {
                //Win condition
                //hitler has been killed
                eb.setDescription("They were Hitler!");
                event.getChannel().sendMessage(eb.build()).queue();
                resetGame(event.getTextChannel());
                return;
            }
            DeadCountAdd();
            hopefullyHitler.setStatus(SecretHitlerStatus.Dead);
            event.getChannel().sendMessage(eb.build());
            Gamestate.nextPres();
            Gamestate.updateNextPres();
            SecretHitlerLobby.showLobby(event.getChannel());
            SecretHitlerLobby.requestElection(event.getTextChannel());
        }
    }
}
