package Commands.SH.Commands;

import Commands.Command;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.enums.SecretHitlerStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

import static main.DiscordBot.YEETNT_EMOTE;
import static main.DiscordBot.YEET_EMOTE;

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
        Emote yeetEmote = event.getGuild().getEmoteById(YEET_EMOTE);
        Emote yeetntEmote = event.getGuild().getEmoteById(YEETNT_EMOTE);
        EmbedBuilder eb = new EmbedBuilder();
        if(!Gamestate.getGameStage().equals(GameStage.Election)){
            eb.setTitle("Game has not arrived at election stage");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
         if(event.getMessage().getMentionedUsers().isEmpty()){
             event.getChannel().sendMessage("Usage is !elect <@user>").queue();
             return;
         }

         String id = event.getMessage().getMentionedUsers().get(0).getId();
         Player president = Gamestate.findPlayer(event.getMember().getId());
         if(president == null) {
             eb.setTitle("You are not playing.");
             event.getChannel().sendMessage(eb.build()).queue();
         }


         if( !id.equals(event.getMember().getId()) && president.getStatus().equals(SecretHitlerStatus.President) ) {
             Player chancellor = Gamestate.findPlayer(id);
             if(chancellor == null || chancellor.getStatus().equals(SecretHitlerStatus.Dead) || chancellor.getStatus().equals(SecretHitlerStatus.Waiting)) {
                 eb.setTitle("Player must be Alive or Active to be a chancellor");
             } else {
                 eb.setTitle("The President has chosen a Chancellor!");
                 eb.setDescription("please vote " + yeetEmote.getAsMention() + " or " + yeetntEmote.getAsMention());
                 eb.setColor(Color.CYAN);
                 Gamestate.setGameStage(GameStage.Voting);
                 chancellor.setStatus(SecretHitlerStatus.Chancellor);
                 Gamestate.setChancellorLocation(Gamestate.getPlayers().indexOf(chancellor));
             }
             event.getChannel().sendMessage(eb.build()).queue();
         } else if(!president.getStatus().equals(SecretHitlerStatus.President)) {
             eb.setTitle("You are not the president.");
             event.getChannel().sendMessage(eb.build()).queue();
         }else if(id.equals(event.getMember().getId())) {
             eb.setTitle("You cannot vote for yourself.");
             event.getChannel().sendMessage(eb.build()).queue();
         }

    }
}
