package Commands;

import Commands.SH.Helper.MessageHelper;
import Commands.utils.Command;
import Commands.utils.FileConfig;
import com.google.gson.Gson;
import main.DiscordBot;
import main.utils.ServerGame;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

//TODO: comment code
public class Init extends Command {
    /**
     * Generates the Command for a specific Key
     * @param key  the string which is to be matched to run the command
     * @param desc
     */
    public Init(String key, String desc) {
        super(key, desc);
    }


    private Long initEmote (Guild gl,String name ) throws IOException {
        if(gl.getEmotesByName(name, true).isEmpty()) {
             return gl.createEmote(name, Icon.from(new File(DiscordBot.FILE_PATH +  name))).complete().getIdLong();
        } else {
            return gl.getEmotesByName(name, true).get(0).getIdLong();
        }
    }


    private void initYeets (FileConfig fileConfig, MessageReceivedEvent event) {
        try {
            Guild gl = event.getGuild();
            fileConfig.setYeet_emote(initEmote(gl,"Yeet"));
            fileConfig.setYeetnt_emote(initEmote(gl,"Yeetnt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFileConfig (TextChannel channel) {
        Gson g = new Gson();
        try {
            FileWriter file = new FileWriter("config.txt");
            file.write(g.toJson(ServerGame.getConfig()));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateFileConfig (MessageReceivedEvent event) {
        if (ServerGame.getConfig() == null) {
            ServerGame.setConfig( new HashMap<>());
        }
        FileConfig fileConfig =  (ServerGame.getConfig().containsKey(event.getGuild().getIdLong())) ? ServerGame.getConfig().get(event.getGuild().getIdLong()) : new FileConfig();
        fileConfig.setBot_channel(event.getTextChannel().getIdLong());
        initYeets(fileConfig,event);
        ServerGame.getConfig().put(event.getGuild().getIdLong(),fileConfig);
    }


    @Override
    public void start(MessageReceivedEvent event) {
        generateFileConfig(event);
        writeFileConfig(event.getTextChannel());
        MessageHelper.sendMessage(event.getTextChannel(), "Initialized Game on this channel");
    }
}
