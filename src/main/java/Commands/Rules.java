package Commands;

import Commands.SH.Helper.EmbededHelper;
import Commands.utils.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
public class Rules extends Command {
    /**
     * The Command for the Rules
     * @param key !rules
     * @param desc the description for the command
     */
    public Rules(String key, String desc) {
        super(key, desc);
    }

    /**
     * the start event
     * @param event the event that started the command
     */
    @Override
    public void start(MessageReceivedEvent event) {
        EmbededHelper.sendEmbed(event.getChannel(),  EmbededHelper.createEmbeded("Rules", Color.white, "https://www.secrethitler.com/assets/Secret_Hitler_Rules.pdf"), false);
    }
}
