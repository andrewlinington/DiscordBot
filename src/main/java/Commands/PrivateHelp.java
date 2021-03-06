package Commands;

import Commands.SH.Helper.EmbededHelper;
import Commands.utils.Command;
import Commands.utils.CommandList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

/**
 * @// TODO: 5/21/2021 remove and replace commands with JDA Utilities Menu
 *
 * @version 1.1
 */
public class PrivateHelp extends Command {
    /**
     * creates the help command
     * @param key the key to be used
     * @param desc the description of the command
     */
    public PrivateHelp(String key, String desc) {
        super(key,desc);
    }


    /**
     * Starts the help command to show all available commands
     * @param event the event that started the command
     */
    @Override
    public void start(MessageReceivedEvent event) {
        EmbededHelper.sendEmbed(event.getPrivateChannel(), EmbededHelper.createEmbeded("Bot Commands:",Color.green,"", generateField()), true);
    }

    /**
     * generates a list of fields
     * @return the fields
     */
    private ArrayList<MessageEmbed.Field> generateField () {
        ArrayList<Command> commands = CommandList.getInstance().getPrivateCommands();
        ArrayList<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(EmbededHelper.generateField("!help", commands.get(0).getDescription()));
        fields.add(EmbededHelper.generateField("!policy <#>", commands.get(1).getDescription()));
        return fields;
    }

}
