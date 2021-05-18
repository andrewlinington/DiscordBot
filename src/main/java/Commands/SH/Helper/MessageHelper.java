package Commands.SH.Helper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.io.File;

//TODO: REFACTOR
public class MessageHelper {

    //Adding feature to introduce ratelimit
    //private static HashMap<MessageChannel, Rate> failedMessages = new HashMap<>();

    /**
     * sends an embed message to the target channel
     * @param channel the channel to send a message on
     * @param eb the embed to send
     */
    public static void sendEmbededMessage(MessageChannel channel, EmbedBuilder eb) {
        channel.sendMessage(eb.build()).queue();
    }

    /**
     * sends an embed message to the target channel
     * @param channel the channel to send a message on
     * @param eb the embed to send
     * @param location the File location
     * @param name the name of the temp file attached
     */
    public static void sendEmbededMessage (MessageChannel channel, EmbedBuilder eb, File location, String name) {
        channel.sendMessage(eb.build()).addFile(location,name).queue();
    }

    /**
     * sends a plain text message
     * @param channel the channel to send a message on
     * @param s the string to be sent
     */
    public static void sendMessage(MessageChannel channel, String s) {
        channel.sendMessage(s).queue();
    }
}
