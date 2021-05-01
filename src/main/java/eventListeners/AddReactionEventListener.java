package eventListeners;

import Commands.SH.utils.Gamestate;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.Player;
import Commands.SH.utils.SecretHitlerStatus;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class AddReactionEventListener extends ListenerAdapter {


    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        Message m = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete();
        if (m.getAuthor().isBot() && m.getEmbeds().get(0).getColor().equals(Color.CYAN)) {
            if (Gamestate.getGameStage().equals(GameStage.Voting)) {
                String react = event.getUser().getId();
                Player p = Gamestate.findPlayer(react);
                if(p != null ) {
                    if (p.isFailedToVote()) {
                        p.setFailedToVote(false);
                        return;
                    }
                    if(p.hasVoted()) {
                        if (event.getReactionEmote().getEmote().getName().startsWith("YEETNT")) {
                            p.removeVote();
                            Gamestate.removeVote(false);
                        }
                        else if (event.getReactionEmote().getEmote().getName().startsWith("YEET")){
                            p.removeVote();
                            Gamestate.removeVote(true);
                        }
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        Message m = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete();
        if(m.getAuthor().isBot() &&  m.getEmbeds().get(0).getColor().equals(Color.CYAN) && !event.getUser().isBot() ) {
            if (Gamestate.getGameStage().equals(GameStage.Voting)) {
                String react = event.getUser().getId();
                Player p = Gamestate.findPlayer(react);
                if (p != null) {
                    if(!p.hasVoted() && (!p.getStatus().equals(SecretHitlerStatus.Waiting) && ! p.getStatus().equals(SecretHitlerStatus.Dead))) {
                        if (event.getReactionEmote().getEmote().getName().startsWith("YEETNT")) {
                            p.vote();
                            Gamestate.addVote(false, event);
                        }
                        else if (event.getReactionEmote().getEmote().getName().startsWith("YEET")){
                            p.vote();
                            Gamestate.addVote(true, event);
                        }
                        else {
                            event.getReaction().removeReaction(event.getUser()).queue();
                        }
                        return;
                    }
                    p.setFailedToVote(true);
                }
            }
            event.getReaction().removeReaction(event.getUser()).queue();
        }
    }
}
