package Commands.SH;

import Commands.Command;
import Commands.SH.Objects.*;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.ArrayList;
import java.util.Random;

public class SecretHitlerStart extends Command {
    Random rand;

    public SecretHitlerStart(String s) {
        super(s);
    }

    @Override
    public void start(MessageReceivedEvent event) {
        int numPlayers = PlayerList.getPlayerCount();
        rand = new Random();

//            if(Gamestate.isStarted()) {
//                EmbedBuilder eb = new EmbedBuilder();
//                eb.setTitle("Game has already started");
//                event.getChannel().sendMessage(eb.build()).queue();
//            }

        if(PlayerList.getPlayerCount() < 5){
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Not enough players for a round");
            event.getChannel().sendMessage(eb.build()).queue();
            return;
        }
        ArrayList<Role> roles =  RoleList.createRoles(numPlayers);
        ArrayList<Player> players = PlayerList.getPlayers();
        ArrayList<Player> fascists = new ArrayList<>();
        for (int i = 0; i < numPlayers; i++){
            players.get(i).setStatus(SecretHitlerStatus.ALIVE);
            players.get(i).setRole(roles.get(i));
            showRole(roles.get(i), players.get(i).getUser());
            if(players.get(i).getRole().getPublicRole().equals("Fascist")) {
                fascists.add(players.get(i));
            }
        }
        players.get(rand.nextInt(numPlayers)).setStatus(SecretHitlerStatus.PRESIDENT);
        showFascists(numPlayers, fascists);
        SecretHitlerLobby.showLobby(event);
        Gamestate.setStarted(true);
        Gamestate.setState(GameStage.Election);

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

    private void showRole (Role role, User user) {
        user.openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Your Role");
            eb.addField("Public Role", role.getPublicRole(), false);
            eb.addField("Secret Role", role.getSecretRole(), false);
            privateChannel.sendMessage(eb.build()).queue();
        });
    }



}