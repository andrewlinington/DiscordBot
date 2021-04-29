package Commands.SH;

import Commands.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

import static main.DiscordBot.YEET_EMOTE;

public class SecretHitlerElection extends Command {

    public SecretHitlerElection(String key) {
        super(key);
    }

    @Override
    public boolean keyMatches(String string) {
        return  string.startsWith(getKey());
    }

    @Override
    public void start(MessageReceivedEvent event) {
        Emote yeetEmote = event.getGuild().getEmoteById(YEET_EMOTE);
//        if(!Gamestate.isStarted() && !Gamestate.getGameStage().equals(GameStage.Election)){
//            EmbedBuilder eb = new EmbedBuilder();
//            eb.setTitle("Game has not started or Arrived at Election stage");
//            event.getChannel().sendMessage(eb.build()).queue();
//            return;
//        }
//         String id = event.getMessage().getMentionedUsers().get(0).getId();
//         Player president = PlayerList.findPlayer(event.getMember().getId());
//         if( !id.equals(event.getMember().getId()) && president.getStatus().equals(SecretHitlerStatus.PRESIDENT)) {
//             EmbedBuilder eb = new EmbedBuilder();
//             eb.setTitle("The President has chosen a Chancellor!");
//             eb.setDescription("please vote" + yeetEmote )
//             event.getChannel().sendMessage(eb.build()).queue();
//         }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("You are not the president   " + yeetEmote.getAsMention());
        eb.setColor(Color.CYAN);
        MessageEmbed m =  eb.build();
        event.getChannel().sendMessage(m).queue();

    }
}
