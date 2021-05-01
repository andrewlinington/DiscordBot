package Commands.SH.Commands;

import Commands.Command;
import Commands.SH.utils.Gamestate;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;



//TODO: work on policy stuff for chancellor and president
public class LegislativeVote extends Command {
    public LegislativeVote(String key, String desc) {
        super(key, desc);
    }

    @Override
    public boolean keyMatches(String target) {
        return target.startsWith("!policy");
    }

    @Override
    public void start(MessageReceivedEvent event) {
        String[] s1 = event.getMessage().getContentRaw().split(" ");
        if(s1.length == 2) {
            String s = s1[1];
            try {
                if (Integer.parseInt(s) > 0 && Integer.parseInt(s) <= Gamestate.getHandSize()) {
                    int i = Integer.parseInt(s);
                    Gamestate.nextHand(i, event);
                } else {
                    event.getPrivateChannel().sendMessage("Choice must be between 1 and " + Gamestate.getHandSize() + "!").queue();
                }
                return;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        event.getPrivateChannel().sendMessage("Usage is: !policy <#>").queue();
    }
}
