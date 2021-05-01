package Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

public class PrivateHelp extends Command {
    public PrivateHelp(String key, String desc) {
        super(key,desc);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Bot Commands:");
        eb.setColor(Color.GREEN);
        ArrayList<Command> commands = CommandList.getPrivateCommands();
        eb.addField(commands.get(0).getDescription(),                          "!help",false);
        eb.addField(commands.get(1).getDescription(),                         "!policy <#>",false);
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
