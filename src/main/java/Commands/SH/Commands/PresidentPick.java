package Commands.SH.Commands;

import Commands.utils.Command;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

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
        EmbedBuilder eb = new EmbedBuilder();
        if(!Gamestate.getGameStage().equals(GameStage.Pick)){
            eb.setTitle("There is no power to Pick a president at this time");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        if(event.getMessage().getMentionedUsers().isEmpty()){
            event.getChannel().sendMessage("Usage is !pick <@user>").queue();
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
            Player p = Gamestate.findPlayer(id);
            if(!p.userStatus().equals("Dead") && !president.equals(p)) {
                Gamestate.updateNextChosenPres(p.getUser());
                Gamestate.nextPres();
                Gamestate.updateNextChosenPres();
                SecretHitlerLobby.showLobby(event.getChannel());
                SecretHitlerLobby.requestElection(event.getTextChannel());
            } else if (p.userStatus().equals("Dead")) {
                eb.setTitle("A dead player cannot be president");
                event.getChannel().sendMessage(eb.build()).queue();
            } else if (p.equals(president)) {
                eb.setTitle("You cannot elect yourself");
                event.getChannel().sendMessage(eb.build()).queue();
            }
        }
    }
}
