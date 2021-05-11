package Commands;

import Commands.SH.Helper.EmbededHelper;
import Commands.utils.Command;
import Commands.utils.CommandList;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.ArrayList;

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


    private ArrayList<MessageEmbed.Field> generateField () {
        ArrayList<Command> commands = CommandList.getPrivateCommands();
        ArrayList<MessageEmbed.Field> fields = new ArrayList<>();
        fields.add(EmbededHelper.generateField(commands.get(0).getDescription(),"!help"));
        fields.add(EmbededHelper.generateField(commands.get(1).getDescription(),"!policy <#>"));
        return fields;
    }

}
