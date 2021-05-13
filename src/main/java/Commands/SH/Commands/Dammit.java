package Commands.SH.Commands;

import Commands.SH.Helper.MessageHelper;
import Commands.utils.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.OffsetDateTime;
import java.util.Locale;

public class Dammit extends Command {
    private Long last = -1L;
    /**
     * Generates the Command for a specific Key
     *
     * @param key  the string which is to be matched to run the command
     * @param desc
     */
    public Dammit(String key, String desc) {
        super(key, desc);
    }


    /**
     * matches if the string exists somewhere in the message
     * @param string the string from the user
     * @return if the string exists true otherwise false
     */
    @Override
    public boolean keyMatches(String string) {
        return string.toLowerCase(Locale.ROOT).contains(getKey());
    }

    @Override
    public void start(MessageReceivedEvent event) {
        OffsetDateTime time = event.getMessage().getTimeCreated();
        if (last != -1){
            MessageHelper.sendMessage(event.getChannel(),  "last occurrence: " + (time.toEpochSecond() - last)/60 + " Minutes ago!");
        }
        last = time.toEpochSecond();
    }
}
