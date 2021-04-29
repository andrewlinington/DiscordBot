package eventListeners;

import Commands.CommandList;
import Commands.Command;
import main.DiscordBot;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

import static main.DiscordBot.YEET_EMOTE;

public class CommandEventListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String messageContent = event.getMessage().getContentRaw();
        if (!event.getAuthor().isBot()  && !event.isFromType(ChannelType.PRIVATE)) {

            if (messageContent.startsWith("!")) {
                for (Command c : CommandList.getCommands()) {
                    if (c.keyMatches(messageContent)) {
                        c.start(event);
                        return;
                    }
                }
            }
        }

        if(event.getAuthor().isBot() && !event.getMessage().isFromType(ChannelType.PRIVATE) && event.getMessage().getEmbeds().get(0).getColor() != null && event.getMessage().getEmbeds().get(0).getColor().equals(Color.CYAN) ){
            event.getMessage().addReaction(event.getGuild().getEmoteById(YEET_EMOTE)).queue();
            event.getMessage().addReaction(event.getGuild().getEmoteById(DiscordBot.YEETNT_EMOTE)).queue();
        }
    }
}
