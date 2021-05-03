package main.eventListeners;

import Commands.SH.utils.Gamestate;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.Player;
import Commands.SH.utils.enums.SecretHitlerStatus;
import main.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class AddReactionEventListener extends ListenerAdapter {

    private int breadCount= 0;
    @Override
    public void onMessageReactionRemove(@NotNull MessageReactionRemoveEvent event) {
        Message m = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete();
        if (m.getAuthor().isBot() && m.getEmbeds().get(0).getColor() != null && m.getEmbeds().get(0).getColor().equals(Color.CYAN)) {
            if (Gamestate.getGameStage().equals(GameStage.Voting)) {
                String react = event.getUser().getId();
                Player p = Gamestate.findPlayer(react);
                if(p != null ) {
                    if (p.isFailedToVote()) {
                        p.setFailedToVote(false);
                        return;
                    }
                    if(p.hasVoted() && Gamestate.getGameStage().equals(GameStage.Voting)) {
                        if (event.getReactionEmote().getEmote().getIdLong() == DiscordBot.config.getYeetnt_emote()) {
                            p.removeVote();
                            Gamestate.removeVote(false);
                        }
                        else if (event.getReactionEmote().getEmote().getIdLong() == DiscordBot.config.getYeet_emote()){
                            p.removeVote();
                            Gamestate.removeVote(true);
                        }
                    }else if(p.hasVoted() && Gamestate.getGameStage().equals(GameStage.Veto)) {
                        if (event.getReactionEmote().getEmote().getIdLong() == DiscordBot.config.getYeetnt_emote()) {
                            p.removeVote();
                            Gamestate.removeVeto(false);
                        }
                        else if (event.getReactionEmote().getEmote().getIdLong() == DiscordBot.config.getYeet_emote()){
                            p.removeVote();
                            Gamestate.removeVeto(true);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        Message m = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete();
        if(m.getAuthor().isBot()  && m.getEmbeds().get(0).getColor() != null &&  m.getEmbeds().get(0).getColor().equals(Color.CYAN) && !event.getUser().isBot() ) {
            if (Gamestate.getGameStage().equals(GameStage.Voting)) {
                String react = event.getUser().getId();
                Player p = Gamestate.findPlayer(react);
                if (p != null) {
                    if(!p.hasVoted() && (!p.getStatus().equals(SecretHitlerStatus.Waiting) && ! p.getStatus().equals(SecretHitlerStatus.Dead))) {
                        if(!event.getReactionEmote().isEmote()) {
                            breadReact(event);
                        }
                        else if (event.getReactionEmote().getEmote().getIdLong() == DiscordBot.config.getYeetnt_emote()) {
                            p.vote();
                            Gamestate.addVote(false, event);
                        }
                        else if (event.getReactionEmote().getEmote().getIdLong() == DiscordBot.config.getYeet_emote()){
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
            } else if (Gamestate.getGameStage().equals(GameStage.Veto)) {
                String react = event.getUser().getId();
                Player p = Gamestate.findPlayer(react);
                if (p != null) {
                    if(!p.hasVoted() && (p.getStatus().equals(SecretHitlerStatus.President) || p.getStatus().equals(SecretHitlerStatus.Chancellor))) {
                        if(!event.getReactionEmote().isEmote()) {
                            breadReact(event);
                        }
                        else if (event.getReactionEmote().getEmote().getIdLong() == DiscordBot.config.getYeetnt_emote()) {
                            p.vote();
                            Gamestate.addVeto(false, event);
                        }
                        else if (event.getReactionEmote().getEmote().getIdLong() == DiscordBot.config.getYeet_emote()){
                            p.vote();
                            Gamestate.addVeto(true, event);
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

    private void breadReact(@NotNull MessageReactionAddEvent event) {
        if(event.getReactionEmote().getEmoji().equals(new String(Character.toChars(0x1F35E)))){
            breadCount++;
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Bread count = " + breadCount);
            event.getTextChannel().sendMessage(eb.build()).queue();
        }
        event.getReaction().removeReaction(event.getUser()).queue();
    }
}
