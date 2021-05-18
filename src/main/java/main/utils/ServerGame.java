package main.utils;

import Commands.SH.utils.Gamestate;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @version 1.0
 * TODO: update find user server to some JDA command system
 **/
public class ServerGame {
  @Getter @Setter private static HashMap<Long, SHGame> games = new HashMap<>();

  /**
   * @deprecated to be removed after JDA Utilities is introduced for Private message voting
   * returns the users first associated Guild to choose a policy
   * @param author the author who sent the message
   * @return the authors guild if it exists
   */
  public static Guild findUserServer(User author) {
    List<Guild> guilds = author.getMutualGuilds();
    Optional<Guild> guild = guilds.stream().filter(g -> checkIsLegislative(g, author)).findFirst();
    return guild.orElse(null);
  }

  /**
   * Checks if the author is a part of any election at the current time
   * @param g the guild for the user
   * @param author the event author
   * @return true if the user is a part of a legislation
   */
  private static boolean checkIsLegislative(Guild g, User author) {
    Gamestate gs = getGames().get(g.getIdLong()).getGamestate();
    return gs.findPlayer(author.getId()) != null
        && ((gs.findPlayer(author.getId()).getStatus().equals(SecretHitlerStatus.Chancellor)
                && gs.getGameStage().equals(GameStage.LegislationChancellor))
            || (gs.findPlayer(author.getId()).getStatus().equals(SecretHitlerStatus.President)
                && gs.getGameStage().equals(GameStage.LegislationPres)));
  }
}
