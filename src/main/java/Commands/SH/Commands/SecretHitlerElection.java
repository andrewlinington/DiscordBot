package Commands.SH.Commands;

import Commands.SH.Helper.EmbededHelper;
import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import Commands.utils.Command;
import main.utils.FileConfig;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

//TODO: REFACTOR
public class SecretHitlerElection extends Command {

    public SecretHitlerElection(String key, String desc) {
        super(key, desc);
    }

    @Override
    public boolean keyMatches(String string) {
        return  string.startsWith(getKey());
    }

    @Override
    public void start(MessageReceivedEvent event) {
        super.start(event);
        FileConfig fc =   getGame().getConfig();
        Gamestate gs = getGame().getGamestate();

        Emote yeetEmote = event.getGuild().getEmoteById(fc.getYeet_emote());
        Emote yeetntEmote = event.getGuild().getEmoteById(fc.getYeetnt_emote());

        if(!gs.getGameStage().equals(GameStage.Election)){
            MessageHelper.sendMessage(event.getChannel(),  "Game has not arrived at election stage");
            return;
        }
         if(event.getMessage().getMentionedUsers().isEmpty()){
             event.getChannel().sendMessage("Usage is !elect <@user>").queue();
             return;
         }

         String id = event.getMessage().getMentionedUsers().get(0).getId();
         Player president = gs.findPlayer(event.getMember().getId());
         if(president == null) {
             MessageHelper.sendMessage(event.getChannel(),"You are not playing.");
             return;
         }


         if( !id.equals(event.getMember().getId()) && president.getStatus().equals(SecretHitlerStatus.President) ) {
             Player chancellor = gs.findPlayer(id);
             if (chancellor == null || chancellor.getStatus().equals(SecretHitlerStatus.Dead) || chancellor.getStatus().equals(SecretHitlerStatus.Waiting)) {
                 MessageHelper.sendMessage(event.getChannel(), "Player must be Alive or Active to be a chancellor");
             } else if (chancellor.getStatus().equals(SecretHitlerStatus.Past_Chancellor) || (chancellor.getStatus().equals(SecretHitlerStatus.Past_President) && gs.getPlayerCount() > 5)) {
                 MessageHelper.sendMessage(event.getChannel(), "Player cannot be from previous legislation");
             } else {
                 EmbededHelper.sendEmbedQueue(event.getTextChannel(), EmbededHelper.createEmbeded("The President has chosen a Chancellor!", Color.CYAN, "Please vote " + yeetEmote.getAsMention() + " or " + yeetntEmote.getAsMention()), yeetEmote, yeetntEmote);
                 gs.setGameStage(GameStage.Voting);
                 chancellor.setStatus(SecretHitlerStatus.Chancellor);
                 gs.setChancellorLocation(gs.getPlayers().indexOf(chancellor));
             }

         } else {
             MessageHelper.sendMessage(event.getChannel(),(president.getStatus().equals(SecretHitlerStatus.President)) ? "You cannot vote for yourself." : "You are not the president.");
         }
    }
}
