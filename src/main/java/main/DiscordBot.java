package main;

import eventListeners.EventListeners;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;

public class DiscordBot  {
    private static String token = "enter token here";

    public static JDA API;

    public static void main(String[] args) throws LoginException {
        List<GatewayIntent> gatewayIntents = new ArrayList<>();
        gatewayIntents.add(GatewayIntent.GUILD_MEMBERS);
        JDABuilder.createDefault(token).setActivity(Activity.playing("!help")).build().addEventListener(EventListeners.ALL.toArray());
    }
}
