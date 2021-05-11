package main.utils;

import Commands.SH.utils.Gamestate;
import Commands.SH.utils.PlayerList;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import Commands.utils.FileConfig;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

//TODO: REFACTOR
public class ServerGame {
    @Getter
    @Setter
    private static HashMap<Guild, Gamestate> guildGames = new HashMap<>();
    @Setter
    @Getter
    private static HashMap<Long, FileConfig> config = new HashMap<>();
    @Setter
    @Getter
    private static HashMap<Guild, PlayerList> lobby = new HashMap<>();


    public static Guild findUserServer(User author) {
        List<Guild> guilds = author.getMutualGuilds();
        //add identifier as to order of which game was started
       Optional<Guild> guild =  guilds.stream().filter(g -> checkIsLegislative(g,author)).findFirst();
       return guild.orElse(null);
    }

    private static boolean checkIsLegislative (Guild g, User author) {
        Gamestate gs = getGuildGames().get(g);
        return gs.findPlayer(author.getId()) != null  && ((gs.findPlayer(author.getId()).getStatus().equals(SecretHitlerStatus.Chancellor) && gs.getGameStage().equals(GameStage.LegislationChancellor) ) ||
                ( gs.findPlayer(author.getId()).getStatus().equals(SecretHitlerStatus.President) && gs.getGameStage().equals(GameStage.LegislationPres)));
    }

}
