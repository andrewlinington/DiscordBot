package main.utils;

import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;

/**
 * @version 1.1
 * TODO: update find user server to some JDA command system
 **/
public class ServerGame {
    @Getter @Setter private static HashMap<Long, SHGame> games = new HashMap<>();

    /**
     * @// TODO: 5/20/2021 to be removed after JDA Utilities is introduced for Private message
     * voting returns the users first associated Guild to choose a policy
     *
     * @param author the author who sent the message
     * @return the authors guild if it exists
     */
    public static Guild findUserServer(User author) {
       try{
            return author.getMutualGuilds().stream().filter(g -> checkIsLegislative(g, author)).findFirst().orElse(null);
        } catch (Exception e) {
           return null;
       }
    }

    /**
     * Checks if the author is a part of any election at the current time
     * @param g the guild for the user
     * @param author the event author
     * @return true if the user is a part of a legislation
     */
    private static boolean checkIsLegislative(Guild g, User author) throws NullPointerException {
        Gamestate gs = games.get(g.getIdLong()).getGamestate();
        Player p = gs.findPlayer(author.getId());
        return  (p.getStatus().equals(SecretHitlerStatus.Chancellor) && gs.getGameStage().equals(GameStage.LegislationChancellor))
                || (p.getStatus().equals(SecretHitlerStatus.President) && gs.getGameStage().equals(GameStage.LegislationPres));
    }
}
