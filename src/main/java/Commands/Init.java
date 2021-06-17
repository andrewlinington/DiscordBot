package Commands;

import Commands.SH.Helper.MessageHelper;
import Commands.utils.Command;
import com.google.gson.Gson;
import main.DiscordBot;
import main.utils.FileConfig;
import main.utils.ServerGame;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
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
     * @param desc the description of the command
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

    private HashMap<Long,FileConfig> createHashMap () {
        HashMap<Long,FileConfig> fileConfigHashMap = new HashMap<>();
        ServerGame.getGames().forEach((key,data) -> {
            fileConfigHashMap.put(key, data.getConfig());
        });
        return fileConfigHashMap;
    }



    private void writeFileConfig () {
        Gson g = new Gson();
        try {
            FileWriter file = new FileWriter("config.txt");
            file.write(g.toJson(createHashMap()));
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void generateFileConfig (MessageReceivedEvent event) throws NullPointerException {
        FileConfig fileConfig = (getGame().getConfig() == null) ? new FileConfig(): getGame().getConfig();
        fileConfig.setBot_channel(event.getTextChannel().getIdLong());
        initYeets(fileConfig,event);
        getGame().setConfig(fileConfig);
    }


    @Override
    public void start(MessageReceivedEvent event) throws NullPointerException{
        super.start(event);
        generateFileConfig(event);
        writeFileConfig();
        MessageHelper.sendMessage(event.getTextChannel(), "Initialized Game on this channel");
    }
}
