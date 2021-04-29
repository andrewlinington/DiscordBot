package eventListeners;

import Commands.CommandList;
import Commands.Command;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CommandEventListener extends ListenerAdapter {


    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String messageContent = event.getMessage().getContentRaw();
        if (event.getAuthor().isBot()  || event.isFromType(ChannelType.PRIVATE)) {
            return;
        }
        if (messageContent.startsWith("!")) {
            for (Command c : CommandList.getCommands()) {
                if ( c.keyMatches(messageContent) ){
                    c.start(event);
                }
            }
        }

    }

}



// event.getMember().getUser().openPrivateChannel().queue(privateChannel -> {
//         privateChannel.sendMessage(event.getMessage().getContentDisplay()).queue();
//         });

//        System.out.println("Hello: " + event.getAuthor().getName());