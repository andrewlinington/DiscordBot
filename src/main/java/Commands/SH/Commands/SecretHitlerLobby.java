package Commands.SH.Commands;

import Commands.SH.Helper.EmbededHelper;
import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.PlayerList;
import Commands.SH.utils.enums.GameStage;
import Commands.utils.Command;
import main.utils.ServerGame;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

//Refactor 2
public class SecretHitlerLobby extends Command {

    /**
     * the command builder
     * @param key the command key
     * @param desc the description of the command
     */
    public SecretHitlerLobby(String key, String desc) {
        super(key, desc);
    }


    /**
     * Requests an election phase for a given president
     * @param channel the channel to share this information to
     */
    public static void requestElection(TextChannel channel) {
        Gamestate gs = ServerGame.getGames().get(channel.getGuild().getIdLong()).getGamestate();
        gs.setGameStage(GameStage.Election);
        electionMessage(channel, gs);
    }

    /**
     * creates an embed and sends it off
     * @param channel the channel to share the information to
     */
    private static void electionMessage (TextChannel channel, Gamestate gs) {
        MessageHelper.sendMessage(channel,  "President <@" +  gs.getPlayers().get(gs.getPresidentLocation()).getUser().getId() + ">: please pick a chancellor");
    }


    /**
     * Creates the Lobby for the user to see
     * @param event the event that started the command
     */
    @Override
    public void start(MessageReceivedEvent event) {
        super.start(event);
        showLobby(event.getTextChannel());
    }

    /**
     * shows the lobby as a list of players and their status
     * @param channel the text channel to use
     */
    public void showLobby (TextChannel channel) {
        Gamestate gs = getGame().getGamestate();
        PlayerList pl = getGame().getLobby();
        EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("Secret Hitler Lobby " + pl.getPlayerCount() + "/5-10", Color.ORANGE,(GameStage.Idle == gs.getGameStage())? "Game has not started yet.": "Game has started.", PlayerList.toField(pl.getPlayers())), false);
    }


}
