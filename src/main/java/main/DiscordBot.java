package main;

import Commands.utils.FileConfig;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import main.eventListeners.EventListeners;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DiscordBot  {
    private static String token = "enter token here";
    private static final String CONFIG_FILENAME = "config.txt";



    public static JDA API;
    public static final String FILE_PATH = "src/main/java/main/res/";
    public static FileConfig config = null;


    public static void main(String[] args) {
        try {
            getToken();
        } catch (FileNotFoundException e) {
            System.out.println("Config file is missing");
            e.printStackTrace();
            return;
        }
        List<GatewayIntent> gatewayIntents = new ArrayList<>();
        gatewayIntents.add(GatewayIntent.GUILD_MEMBERS);
        try {
            API = JDABuilder.createDefault(config.getToken()).setActivity(Activity.playing("!help")).build();
            API.addEventListener(EventListeners.ALL.toArray());
        } catch (LoginException e) {
            System.out.println("Failed to get config data");
            e.printStackTrace();
            return;
        }
    }

    private static void getToken()throws FileNotFoundException {
        Gson g = new Gson();
        JsonReader json =new JsonReader( new FileReader(CONFIG_FILENAME));
        config = g.fromJson(json,new TypeToken<FileConfig>() {}.getType());
    }

}
