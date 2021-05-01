package Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class PrivateHelp extends Command {
    public PrivateHelp(String key) {
        super(key);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Bot Commands:");
        eb.setColor(Color.GREEN);
        eb.addField("Call for help",                          "!help",false);
        eb.addField("Pick a Policy",                         "!policy <#>",false);
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
