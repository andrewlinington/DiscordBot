package Commands.SH.Commands;

import Commands.utils.Command;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import static Commands.SH.utils.Gamestate.findPlayer;
import static Commands.SH.utils.Gamestate.getGameStage;


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
        String[] s1 = event.getMessage().getContentRaw().split(" ");
        if(s1.length == 2) {
            String s = s1[1];
            try {
                SecretHitlerStatus status = findPlayer(event.getAuthor().getId()).getStatus();
                if (Integer.parseInt(s) > 0 && Integer.parseInt(s) <= Gamestate.getHandSize()) {
                    int i = Integer.parseInt(s);
                    Gamestate.nextHand(i, event);
                } else if ((status.equals(SecretHitlerStatus.President) && getGameStage() == GameStage.LegislationPres) ||
                        (status.equals(SecretHitlerStatus.Chancellor) && getGameStage() == GameStage.LegislationChancellor)) {
                    event.getPrivateChannel().sendMessage("Choice must be between 1 and " + Gamestate.getHandSize() + "!").queue();
                } else {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("You are not able to pick a policy at this time");
                    event.getPrivateChannel().sendMessage(eb.build()).queue();
                }
                return;
            } catch (NumberFormatException e) {
                System.out.println("qua");
//                e.printStackTrace();
            }
        }
        event.getPrivateChannel().sendMessage("Usage is: !policy <#>").queue();
    }
}
