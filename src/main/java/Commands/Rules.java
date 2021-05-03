package Commands;

import Commands.utils.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Rules extends Command {
    public Rules(String key, String desc) {
        super(key, desc);
    }


    @Override
    public void start(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Rules");
        eb.setDescription("https://www.secrethitler.com/assets/Secret_Hitler_Rules.pdf");
        event.getTextChannel().sendMessage(eb.build()).queue();
    }
}
