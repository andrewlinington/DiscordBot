package Commands.SH.Commands;

import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Player;
import Commands.SH.utils.PlayerList;
import Commands.utils.Command;
import main.utils.ServerGame;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

//Refactor 2
public class SecretHitlerJoin extends Command {


    /**
     * The !join command
     * @param key !join string
     * @param desc !join description
     */
    public SecretHitlerJoin(String key, String desc) {
        super(key,desc);
    }

    /**
     * Starts adding the user to the lobby
     * @param event the event that started the command
     */
    @Override
    public void start(MessageReceivedEvent event) {
        User user = event.getMember().getUser();
        addPlayer(user, event.getTextChannel());
    }


    /**
     * Adds the player to the Lobby
     * @param user the user to be added
     * @param channel the channel to send a message too
     */
    private void addPlayer (User user, TextChannel channel) {
        PlayerList players = ServerGame.getLobby().get(channel.getGuild());
        if(!players.isPlayer(user) && players.getPlayerCount() < 10) {
            players.addPlayer(new Player(user));
            MessageHelper.sendMessage(channel, user.getName() + " has joined the lobby.");
        } else {
            showFailureMessage(user, channel, players);
        }
    }


    /**
     * Shows an error message
     * @param user the user who sent the command
     * @param channel the channel to respond to
     */
    private void showFailureMessage (User user, TextChannel channel, PlayerList players) {
        if(players.isPlayer(user)) {
            MessageHelper.sendMessage(channel,"You have already Joined");
        } else if (players.getPlayerCount() == 10) {
            MessageHelper.sendMessage(channel,"Maximum number of players reached.");
        }
    }
}