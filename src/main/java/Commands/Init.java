package Commands;

import Commands.utils.Command;
import com.google.gson.Gson;
import main.DiscordBot;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Init extends Command {
    /**
     * Generates the Command for a specific Key
     *
     * @param key  the string which is to be matched to run the command
     * @param desc
     */
    public Init(String key, String desc) {
        super(key, desc);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        Gson g = new Gson();
        DiscordBot.config.setServer_id(event.getMessage().getGuild().getIdLong());
        DiscordBot.config.setBot_channel(event.getTextChannel().getIdLong());

        Guild gl = DiscordBot.API.getGuildById(DiscordBot.config.getServer_id());

        if(gl.getEmotesByName("yeet", true).isEmpty()) {
            try {
                DiscordBot.config.setYeet_emote(DiscordBot.API.getGuildById(DiscordBot.config.getServer_id())
                        .createEmote("YEET", Icon.from(new File("src/main/java/main/res/Yeet"))).complete().getIdLong());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            DiscordBot.config.setYeet_emote(gl.getEmotesByName("Yeet", true).get(0).getIdLong());
        }
        if(gl.getEmotesByName("yeetnt", true).isEmpty()) {
            try {
                 DiscordBot.config.setYeetnt_emote(DiscordBot.API.getGuildById(DiscordBot.config.getServer_id())
                         .createEmote("YEETNT", Icon.from(new File("src/main/java/main/res/Yeetnt"))).complete().getIdLong());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            DiscordBot.config.setYeetnt_emote(gl.getEmotesByName("Yeetnt", true).get(0).getIdLong());
        }
        try {
            FileWriter file = new FileWriter("config.txt");
            file.write(g.toJson(DiscordBot.config));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
