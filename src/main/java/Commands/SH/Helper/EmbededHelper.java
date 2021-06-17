package Commands.SH.Helper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
//TODO: REFACTOR
public class EmbededHelper {

    //TODO:Refactor 1: implement embed helper for all code
    public static EmbedBuilder createEmbeded (String title, Color color) {
        return new EmbedBuilder().setTitle(title).setColor(color);
    }

    public static EmbedBuilder createEmbeded (String title,Color color, String desc) {
        return createEmbeded(title, color).setDescription(desc);
    }

    public static EmbedBuilder createEmbeded (String title, Color color, String desc, ArrayList<Field> fields) {
        EmbedBuilder eb = createEmbeded(title,color, desc);
        fields.forEach(field -> addField(field, eb));
        return eb;
    }

    public static EmbedBuilder createEmbededImage (String title, Color color, String image) {
        return createEmbeded(title,color).setImage("attachment://" + image);
    }
    public static EmbedBuilder createEmbededImage (String title, Color color, Field field, String image) {
        return createEmbeded(title,color).addField(field).setImage("attachment://" + image);
    }

    public static void sendEmbed (MessageChannel channel, File f, EmbedBuilder eb, String loc) {
        MessageHelper.sendEmbededMessage(channel, eb, f, loc);
        eb.clear();
    }

    public static void sendEmbed(MessageChannel channel, EmbedBuilder eb, boolean isClear) {
        MessageHelper.sendEmbededMessage(channel,eb);
        if(isClear) {
            eb.clear();
        }
    }

    private static void addField(Field f, EmbedBuilder eb) {
        eb.addField(f);
    }

    public static Field generateField (String name, String value ) {
        return new Field(name, value,false);
    }

    public static void sendEmbedQueue(TextChannel textChannel, EmbedBuilder embeded, Emote yeet, Emote yeetnt) {
        textChannel.sendMessage(embeded.build()).queue((message) -> {
            message.addReaction(yeet).queue();
            message.addReaction(yeetnt).queue();
        });
    }

    public static <T> void sendEmbed(T any, T any1) {
    }
}
