package Commands;

import Commands.SH.Helper.EmbededHelper;
import Commands.utils.Command;
import Commands.utils.CommandList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

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
        EmbededHelper.sendEmbed(event.getTextChannel(), EmbededHelper.createEmbeded("Bot Commands:",Color.GREEN,"",generateCommandFields()), false);
    }

    private ArrayList<MessageEmbed.Field> generateCommandFields () {
        ArrayList<MessageEmbed.Field> fields = new ArrayList<>();
    for (Command c : CommandList.getInstance().getCommands()) {
            fields.add(EmbededHelper.generateField(c.getKey(), c.getDescription()));
        }
        return fields;
    }

}
