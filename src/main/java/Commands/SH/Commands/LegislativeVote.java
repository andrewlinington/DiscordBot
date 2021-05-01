package Commands.SH.Commands;

import Commands.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LegislativeVote extends Command {
    public LegislativeVote(String key) {
        super(key);
    }

    @Override
    public boolean keyMatches(String target) {
        return target.startsWith("!policy");
    }

    @Override
    public void start(MessageReceivedEvent event) {

    }
}
