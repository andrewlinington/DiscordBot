package main.eventListeners;

import Commands.utils.Command;
import Commands.utils.CommandList;
import Commands.Init;
import main.DiscordBot;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Objects;

public class CommandEventListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String messageContent = event.getMessage().getContentRaw();
        if (!event.getAuthor().isBot() && messageContent.startsWith("!")) {

            if(!event.isFromType(ChannelType.PRIVATE) && event.getMember().isOwner()) {
                Command  c = new Init("!init", "");
                if (c.keyMatches(messageContent)) {
                    c.start(event);
                }
            }


            if (!event.isFromType(ChannelType.PRIVATE) && event.getTextChannel().getIdLong() == DiscordBot.config.getBot_channel()) {
                for (Command c : CommandList.getCommands()) {
                    if (c.keyMatches(messageContent)) {
                        c.start(event);
                        return;
                    }
                }
            } else if  (event.isFromType(ChannelType.PRIVATE)) {
                for (Command c : CommandList.getPrivateCommands()) {
                    if (c.keyMatches(messageContent)) {
                        c.start(event);
                        return;
                    }
                }
            }
        }

        if(event.getAuthor().isBot() && !event.getMessage().isFromType(ChannelType.PRIVATE) && !event.getMessage().getEmbeds().isEmpty() &&
                event.getMessage().getEmbeds().get(0).getColor() != null && event.getMessage().getEmbeds().get(0).getColor().equals(Color.CYAN) ){
            event.getMessage().addReaction(Objects.requireNonNull(event.getGuild().getEmoteById(DiscordBot.config.getYeet_emote()))).queue();
            event.getMessage().addReaction(Objects.requireNonNull(event.getGuild().getEmoteById(DiscordBot.config.getYeetnt_emote()))).queue();
        }
    }
}
