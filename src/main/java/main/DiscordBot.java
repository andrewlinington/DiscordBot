package main;

import eventListeners.EventListeners;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DiscordBot  {
    private static String token = "enter token here";
    private static final String CONFIG_FILENAME = "config.txt";



    public static JDA API;

    public static void main(String[] args) {
        try {
            getToken();
        } catch (FileNotFoundException e) {
            System.out.println("Config file is missing");
            e.printStackTrace();
        }
        List<GatewayIntent> gatewayIntents = new ArrayList<>();
        gatewayIntents.add(GatewayIntent.GUILD_MEMBERS);
        try {
            JDABuilder.createDefault(token).setActivity(Activity.playing("!help")).build().addEventListener(EventListeners.ALL.toArray());
        } catch (LoginException e) {
            System.out.println("Failed to get config data");
            e.printStackTrace();
        }
    }

    private static void getToken()throws FileNotFoundException {
        File working = new File(CONFIG_FILENAME);
        Scanner readConfig = new Scanner(working);
        if (readConfig.hasNext()) {
            token = readConfig.next();
            token = token.substring(token.indexOf("=") + 1);
        }
    }

}
