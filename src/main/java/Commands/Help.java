package Commands;

import Commands.utils.Command;
import Commands.utils.CommandList;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

/**
 * Shows the help for all public commands
 */
public class Help extends Command {

    /**
     * generates a help command
     * @param key !help
     * @param desc the description of help
     */
    public Help(String key, String desc) {
        super(key,desc);
    }


    /**
     * Starts the help command listing all of the commands for a public channel
     * @param event the event that started the command
     */
    @Override
    public void start(MessageReceivedEvent event) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Bot Commands:");
        eb.setColor(Color.GREEN);
        for (Command c: CommandList.getCommands()) {
            eb.addField(c.getDescription(),c.getKey(),false);
        }
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
