package Commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

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





//        eb.addField("Call for help",                          "!help",false);
//        eb.addField("Join the Lobby",                         "!join",false);
//        eb.addField("Leave the Lobby",                        "!leave", false);
//        eb.addField("Show the Lobby",                         "!show or !lobby",false);
//        eb.addField("Start the Game",                         "!start",false);
//        eb.addField("Elect a Chancellor",                     "!elect",false);
        event.getChannel().sendMessage(eb.build()).queue();
    }
}
