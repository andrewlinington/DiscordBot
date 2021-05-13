package main;

import Commands.SH.utils.Gamestate;
import Commands.SH.utils.PlayerList;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import lombok.Getter;
import main.eventListeners.EventListeners;
import main.utils.FileConfig;
import main.utils.SHGame;
import main.utils.ServerGame;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// TODO: REFACTOR
// TODO: better system for managing files not in this main
@Getter
public class DiscordBot {
  private static final String CONFIG_FILENAME = "config.txt";
  private static final Logger logger = LogManager.getLogger(DiscordBot.class);
  public static Long BOT_ID;
  public static JDA API;
  public static final String FILE_PATH = "src/main/java/main/res/";

  /**
   * runs the entire bot
   *
   * @param args N/A
   */
  public static void main(String[] args) {
    BasicConfigurator.configure();
    logger.debug("Bot Running");
    List<GatewayIntent> gatewayIntents = new ArrayList<>();
    gatewayIntents.add(GatewayIntent.GUILD_MEMBERS);
    try {
      API =
          JDABuilder.createDefault(getToken())
              .setActivity(Activity.playing("!help"))
              .enableIntents(gatewayIntents)
              .setMemberCachePolicy(MemberCachePolicy.ALL)
              .build();
      API.addEventListener(EventListeners.ALL.toArray());
      getConfig();
      BOT_ID = API.getSelfUser().getIdLong();
    } catch (Exception e) {
      System.out.println("Failed to launch Bot");
      e.printStackTrace();
    }
  }

  /**
   * gets the token for the bot
   *
   * @return the bot token
   * @throws FileNotFoundException no token available
   */
  private static String getToken() throws IOException {
    return Files.readString(Path.of("token.txt"));
  }

  /**
   * gets the token and other server info
   *
   * @throws FileNotFoundException no file
   */
  private static void getConfig() throws FileNotFoundException {
    HashMap<Long, FileConfig> configs =
        new Gson()
            .fromJson(
                new JsonReader(new FileReader(CONFIG_FILENAME)),
                new TypeToken<HashMap<Long, FileConfig>>() {}.getType());
    configs.forEach(DiscordBot::createConfig);
  }

  /**
   * creates a config from a key value pair inside of the gameInfo
   *
   * @param key the Long Guild id for the server
   * @param value the value of the FileConfig
   */
  private static void createConfig(Long key, FileConfig value) {
    ServerGame.getGames().put(key, new SHGame(new Gamestate(), new PlayerList(), value));
  }
}
