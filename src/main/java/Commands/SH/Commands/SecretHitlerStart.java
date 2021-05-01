package Commands.SH.Commands;

import Commands.Command;
import Commands.SH.utils.*;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.RoleType;
import Commands.SH.utils.enums.SecretHitlerStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SecretHitlerStart extends Command {
    Random rand;

    public SecretHitlerStart(String key, String desc) {
        super(key, desc);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        int numPlayers = PlayerList.getPlayerCount();
        rand = new Random();

        if(Gamestate.getGameStage().equals(GameStage.Idle)) {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Game has already started");
            event.getChannel().sendMessage(eb.build()).queue();
        }

//        if(PlayerList.getPlayerCount() < 5){
//            EmbedBuilder eb = new EmbedBuilder();
//            eb.setTitle("Not enough players for a round");
//            event.getChannel().sendMessage(eb.build()).queue();
//            return;
//        }
        ArrayList<Role> roles =  createRoles(numPlayers);
        ArrayList<Player> players = PlayerList.getPlayers();
        ArrayList<Player> fascists = new ArrayList<>();

        for (int i = 0; i < numPlayers; i++){
            players.get(i).setStatus(SecretHitlerStatus.Alive);
            players.get(i).setRole(roles.get(i));
            showRole(roles.get(i), players.get(i).getUser());
            if(players.get(i).getRole().getPublicRole().equals("Fascist")) {
                fascists.add(players.get(i));
            }
        }
        int presidentLoc = rand.nextInt(numPlayers);

        players.get(presidentLoc).setStatus(SecretHitlerStatus.President);
        showFascists(numPlayers, fascists);
        SecretHitlerLobby.showLobby(event.getChannel());
        Gamestate.setStart(numPlayers, players,presidentLoc);

    }

    private void showFascists(int numPlayers, ArrayList<Player> fascists) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Your Teammates");
        for (Player p: fascists) {
            eb.addField(p.getName(), p.getRole().getSecretRole(), false);
        }
        for (Player p: fascists) {
            p.getUser().openPrivateChannel().queue(privateChannel -> {
                if(!p.getRole().getSecretRole().equals("Hitler") || numPlayers < 7) {
                    privateChannel.sendMessage(eb.build()).queue();
                }
            });
        }
    }


    /**
     * creates a private message containing the users role
     * @param role the users role
     * @param user the player who needs a message
     */
    private void showRole (Role role, User user) {
        user.openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Your Role");
            eb.addField("Public Role", role.getPublicRole(), false);
            eb.addField("Secret Role", role.getSecretRole(), false);
            privateChannel.sendMessage(eb.build()).queue();
        });
    }


    private Role createHitler (){
        return new Role (RoleType.Fascist, RoleType.Hitler);
    }

    private  Role createLiberal (){
        return new Role (RoleType.Liberal, RoleType.Liberal);
    }

    private Role createFascist (){
        return new Role (RoleType.Fascist, RoleType.Fascist);
    }

    private ArrayList<Role> createRoles(int numRoles) {
        ArrayList<Role> roles = new ArrayList<>();
        switch (numRoles) {
            case 10:
                roles.add(createLiberal());
            case 9:
                roles.add(createFascist());
            case 8:
                roles.add(createLiberal());
            case 7:
                roles.add(createFascist());
            case 6:
                roles.add(createLiberal());
            default:
                roles.add(createLiberal());
                roles.add(createLiberal());
                roles.add(createLiberal());
                roles.add(createFascist());
                roles.add(createHitler());
        }
        Collections.shuffle(roles);
        return roles;
    }




}