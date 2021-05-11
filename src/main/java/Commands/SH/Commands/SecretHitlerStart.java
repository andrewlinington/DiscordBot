package Commands.SH.Commands;

import Commands.SH.Helper.EmbededHelper;
import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.Gamestate;
import Commands.SH.utils.Player;
import Commands.SH.utils.PlayerList;
import Commands.SH.utils.Role;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.RoleType;
import Commands.SH.utils.enums.SecretHitlerStatus;
import Commands.utils.Command;
import main.DiscordBot;
import main.utils.ServerGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.stream.Collectors;

//TODO: REFACTOR
public class SecretHitlerStart extends Command {


    public SecretHitlerStart(String key, String desc) {
        super(key, desc);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        PlayerList pl = ServerGame.getLobby().get(event.getGuild());
        Gamestate gs = ServerGame.getGuildGames().get(event.getGuild());
        int numPlayers = pl.getPlayerCount();

        if(!gs.getGameStage().equals(GameStage.Idle)) {
            MessageHelper.sendMessage(event.getTextChannel(), "Game has already started");
            return;
        }
//        if(numPlayers < 5) {
//            MessageHelper.sendMessage(event.getTextChannel(), "Not enough players for a round");
//            return;
//        }
        ArrayList<Role> roles =  createRoles(numPlayers);
        //maybe remove this and use playerlist
        ArrayList<Player> players = pl.getPlayers();


        for (int i = 0; i < numPlayers; i++){
            players.get(i).setStatus(SecretHitlerStatus.Alive);
            players.get(i).setRole(roles.get(i));
            showRole(roles.get(i), players.get(i));
        }
        ArrayList<Player> fascists = players.stream().filter(p-> p.getRole().getPublicRole().equals("Fascist")).collect(Collectors.toCollection(ArrayList::new));
        Random rand = new Random();
        int presidentLoc = rand.nextInt(numPlayers);
        players.get(presidentLoc).setStatus(SecretHitlerStatus.President);
        ServerGame.getGuildGames().get(event.getGuild()).setStart(numPlayers, players,fascists, presidentLoc);
        showFascists(numPlayers, fascists);
        SecretHitlerLobby.requestElection(event.getTextChannel());
    }

    //FIX WALLLLL


    private void showFascists(int numPlayers, ArrayList<Player> fascists) {
        EmbedBuilder eb =  showFascists(fascists);
        fascists.forEach(p -> sendMessage(p, numPlayers, eb));
    }

    private void sendMessage (Player p , int numPlayers, EmbedBuilder eb) {
        p.getUser().openPrivateChannel().queue(privateChannel -> {
            //shows all the fascists to everyone except hitler when players are 7 or above
            if(!p.getRole().getSecretRole().equals("Hitler") || numPlayers < 7) {
                EmbededHelper.sendEmbed(privateChannel, eb,false);
            }
        });
    }

    private EmbedBuilder showFascists(ArrayList<Player> fascists) {
        return EmbededHelper.createEmbeded("Your Team", Color.red, "Work with your team to elect Hitler or play 6 policy's on the Fascist Board.", PlayerList.toFascistField(fascists));
    }

    /**
     * creates a private message containing the users role
     * @param role the users role
     * @param player the player who needs a message
     */
    private void showRole (Role role, Player player) {
        player.getUser().openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder eb = EmbededHelper.createEmbededImage("Your Role", player.getPublicRoleColor(),EmbededHelper.generateField("PublicRole", role.getPublicRole()), "PublicRole.png");
            EmbededHelper.sendEmbed(privateChannel, new File( DiscordBot.FILE_PATH + role.getPublicImage()), eb, "PublicRole.png");
            eb = EmbededHelper.createEmbededImage("Your Secret Role", player.getSecretRoleColor(),EmbededHelper.generateField("SecretRole", role.getSecretRole()), "SecretRole.png");
            EmbededHelper.sendEmbed(privateChannel, new File( DiscordBot.FILE_PATH + role.getPrivateImage()), eb, "SecretRole.png");
        });
    }

    /**
     * creates a hitler role
     * @return Hitler
     */
    private Role createHitler (){
            return new Role (RoleType.Fascist, RoleType.Hitler,"FascistParty.png","SecretHitler.png");
    }

    /**
     * creates a Liberal
     * @return Liberal
     */
    private  Role createLiberal (){
        return new Role(RoleType.Liberal, RoleType.Liberal,"LiberalParty.png","SecretLiberal.png");
    }

    /**
     * creates a Fascist
     * @return Fascist
     */
    private Role createFascist (){
        return new Role (RoleType.Fascist, RoleType.Fascist,"FascistParty.png","SecretFascist.png");
    }

    /**
     * creates a randomized array of Roles based on the number of players
     * @param numRoles the number of players
     * @return the Roles List
     */
    private ArrayList<Role> createRoles(int numRoles) {
        ArrayList<Role> roles = initializeRoles();
        for (int i = 6; i <= numRoles; i++) {
            roles.add((i % 2 == 0) ? createLiberal(): createFascist());
        }
        Collections.shuffle(roles);
        return roles;
    }

    /**
     * creates the initial list of roles
     * @return the initial list
     */
    private ArrayList<Role> initializeRoles () {
        ArrayList<Role> roles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            roles.add(createLiberal());
        }
        roles.add(createFascist());
        roles.add(createHitler());
        return roles;
    }
}