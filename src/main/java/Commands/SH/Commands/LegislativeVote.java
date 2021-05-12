package Commands.SH.Commands;

import Commands.SH.utils.Gamestate;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import Commands.utils.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


//TODO: REFACTOR
public class LegislativeVote extends Command {
    public LegislativeVote(String key, String desc) {
        super(key, desc);
    }

    @Override
    public boolean keyMatches(String target) {
        return target.startsWith(getKey());
    }

    @Override
    public void start(MessageReceivedEvent event) {
        super.start(event);
        if (getGame() == null ) {
            event.getPrivateChannel().sendMessage("You are not playing").queue();
            return;
        }
        Gamestate gs = getGame().getGamestate();
        String[] s1 = event.getMessage().getContentRaw().split(" ");
        if(s1.length == 2) {
            String s = s1[1];
            try {
                SecretHitlerStatus status = gs.findPlayer(event.getAuthor().getId()).getStatus();
                if (Integer.parseInt(s) > 0 && Integer.parseInt(s) <= gs.getHandSize()) {
                    int i = Integer.parseInt(s);
                    gs.nextHand(i, event);
                } else if ((status.equals(SecretHitlerStatus.President) && gs.getGameStage() == GameStage.LegislationPres) ||
                        (status.equals(SecretHitlerStatus.Chancellor) && gs.getGameStage() == GameStage.LegislationChancellor)) {
                    event.getPrivateChannel().sendMessage("Choice must be between 1 and " + gs.getHandSize() + "!").queue();
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("You are not able to pick a policy at this time");
                    event.getPrivateChannel().sendMessage(eb.build()).queue();
                }
                return;
            } catch (NumberFormatException e) {
                System.out.println("qua");
            }
        }
        event.getPrivateChannel().sendMessage("Usage is: !policy <#>").queue();
    }
}
