package Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class Help extends Command {

    public Help(String key) {
        super(key);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Bot Commands:");
//        eb.setThumbnail("https://picsum.photos/seed/picsum/200/300");
        eb.setColor(Color.GREEN);
        eb.addField("Call for help",                          "!help",false);
        eb.addField("Join the Lobby",                         "!SH Join",false);
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
