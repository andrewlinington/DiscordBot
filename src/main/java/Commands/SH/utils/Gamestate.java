package Commands.SH.utils;


import Commands.SH.Commands.SecretHitlerLobby;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static main.DiscordBot.*;

public class Gamestate {

    private Gamestate () { }

    private static GameStage gameStage = GameStage.Idle;
    private static ArrayList<Player> players;
    private static ArrayList<Player> fascists;


    //the locations of important navigation locations
    private static int presidentLocation;
    private static int nextLocation;
    private static int chancellorLocation;
    private static int prevPresident;
    private static int prevChancellor;

    private static int playerCount;


    private static int yeet;
    private static int yeetnt;

    private static Deck deck;
    private static Deck discard;
    private static Deck hand;

    private static Board board;

    private static int deadCount;



    //TODO: rejection status if rejected 3 times



    public static void setStart(int numPlayers, ArrayList<Player> players,ArrayList<Player> fascists, int presidentLocation) {
        Gamestate.fascists = fascists;
        board = new Board(numPlayers);


        try {
            FileUtils.cleanDirectory(new File(FILE_PATH + "temp/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        deck = new Deck();
        deck.createDeck();

        discard = new Deck();
        hand = new Deck();

        gameStage = GameStage.Election;
        playerCount = numPlayers;
        Gamestate.players = players;

        Gamestate.presidentLocation = presidentLocation;
        Gamestate.nextLocation = (presidentLocation == playerCount - 1) ? 0 : presidentLocation + 1;
        Gamestate.chancellorLocation = presidentLocation;
        Gamestate.prevChancellor = presidentLocation;
        Gamestate.prevPresident = presidentLocation;

        yeet = 0;
        yeetnt = 0;
    }

    public static void setGameStage(GameStage stage) {
        gameStage = stage;
    }

    public static GameStage getGameStage() {
        return gameStage;
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static int getPlayerCount() {
        return playerCount;
    }

    public static void setPlayerCount(int playerCount) {
        Gamestate.playerCount = playerCount;
    }

    public static Player findPlayer (String id){
        for (Player p: players) {
            if(p.hasID(id)) {
                return p;
            }
        }
        return null;
    }


    private static void checkVote(MessageReactionAddEvent event) {
        if (yeet + yeetnt == playerCount - deadCount) {
            for (Player p : players) {
                p.removeVote();
                p.setFailedToVote(false);
            }
            EmbedBuilder eb = new EmbedBuilder();
            if (yeetnt >= yeet) {
                //TODO: fix eligibility
                //election ticker
                nextPres();
                Gamestate.nextLocation = (presidentLocation == playerCount - 1) ? 0 : presidentLocation + 1;

                eb.setTitle("The vote has failed!");
                eb.addField("Yeet:", String.valueOf(yeet), false);
                eb.addField("Yeetn't:", String.valueOf(yeetnt), false);
                eb.setDescription("The presidency has been passed to the next player");

                event.getChannel().sendMessage(eb.build()).queue();
                SecretHitlerLobby.showLobby(event.getChannel());
                SecretHitlerLobby.requestElection(event.getTextChannel());

            } else {
                eb.setTitle("The vote has Passed!");
                eb.addField("Yeet:", String.valueOf(yeet), false);
                eb.addField("Yeetn't:", String.valueOf(yeetnt), false);
                eb.setDescription("The president and chancellor will now Legislate a policy");

                gameStage = GameStage.LegislationPres;

                event.getChannel().sendMessage(eb.build()).queue();
                SecretHitlerLobby.showLobby(event.getChannel());
                checkHitlerWin(event);
            }
            Message m = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete();
            m.delete().queue();
            yeetnt = 0;
            yeet = 0;
        }
    }

    public static void nextPres() {
        if( players.get(prevPresident).getStatus().equals(SecretHitlerStatus.Past_President)) {
            players.get(prevPresident).setStatus(SecretHitlerStatus.Alive);
        }
        if( players.get(prevChancellor).getStatus().equals(SecretHitlerStatus.Past_Chancellor)) {
            players.get(prevPresident).setStatus(SecretHitlerStatus.Alive);
        }
        if( players.get(presidentLocation).getStatus().equals(SecretHitlerStatus.President)) {
            players.get(presidentLocation).setStatus(SecretHitlerStatus.Past_President);
        }
        if( players.get(chancellorLocation).getStatus().equals(SecretHitlerStatus.Chancellor)) {
            players.get(chancellorLocation).setStatus(SecretHitlerStatus.Past_Chancellor);
        }
        prevPresident = presidentLocation;
        prevChancellor = chancellorLocation;
        presidentLocation = nextLocation;
        while (players.get(presidentLocation).getStatus().equals(SecretHitlerStatus.Dead)) {
            if (presidentLocation >= playerCount) {
                presidentLocation = 0;
            } else {
                presidentLocation++;
            }
        }
        presidentLocation = (presidentLocation >= playerCount) ? 0 : presidentLocation;
        players.get(presidentLocation).setStatus(SecretHitlerStatus.President);
    }


    private static void checkHitlerWin(MessageReactionAddEvent event) {
        if(players.get(chancellorLocation).getRole().getSecretRole().equals("Hitler") && board.isHitlerWin() ){
            TextChannel channel = API.getGuildById(config.getServer_id()).getTextChannelsByName("general",true).get(0);
            //fascists win!
            //Win condition
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Fascists Win!");
            eb.setDescription("Hitler has been elected.");
            channel.sendMessage(eb.build()).queue();
            eb.clear();
            Gamestate.resetGame(channel);
        } else {
            messagePresident(event);
        }

    }

    private static void messagePresident(MessageReactionAddEvent event) {

        Player president = players.get(presidentLocation);

        president.getUser().openPrivateChannel().queue(privateChannel -> {
          // draw 3 policies and reshuffle if missing any

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Policies have been drawn");
            //can beadjusted later to be more dynamic in size for the initial hand
            for (int i = 0; i < 3; i++) {
                if(deck.isEmpty()){
                    deck = discard;
                    Collections.shuffle(deck.deck);
                    discard = new Deck();
                }
                Policy p = deck.draw();
                String type = p.getRole();
                eb.addField("Policy " + (i + 1), type , false);
                hand.discard(p);
            }
            eb.setColor(Color.ORANGE);
            eb.setDescription("Please select a policy to remove using !policy <#>");
            privateChannel.sendMessage(eb.build()).queue();
        });


    }

    public static void removeVote(boolean vote) {
        if (vote) {
            yeet--;
        } else {
            yeetnt--;
        }
    }

    public static void addVote (boolean vote, MessageReactionAddEvent event) {
        if (vote) {
            yeet++;
        } else {
            yeetnt++;
        }
        checkVote(event);
    }

    public static int getPresidentLocation() {
        return presidentLocation;
    }

    public static void setChancellorLocation(int chancellorLocation) {
        Gamestate.chancellorLocation = chancellorLocation;
    }

    public static void nextHand(int i, MessageReceivedEvent event) {
        SecretHitlerStatus status = findPlayer(event.getAuthor().getId()).getStatus();
        if(status.equals(SecretHitlerStatus.President) && gameStage == GameStage.LegislationPres) {
            discard.discard(hand.remove(i -1));
            legislateChancellor();
        }
        else if (status.equals(SecretHitlerStatus.Chancellor) && gameStage == GameStage.LegislationChancellor){
            board.addPolicy(hand.remove(i -1), event);
            int k = getHandSize();
            for (int j = 0; j < k; j++) {
                discard.discard(hand.draw());
            }
        } else {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("You are not able to pick a policy at this time");
            event.getPrivateChannel().sendMessage(eb.build()).queue();
        }

    }

    private static void legislateChancellor() {
        players.get(chancellorLocation).getUser().openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Your Policy's");
            eb.setColor(Color.ORANGE);
            for (int i = 0; i < getHandSize(); i++) {
                eb.addField("Policy "  + (i + 1), hand.deck.get(i).getRole(), false);
            }
            gameStage = GameStage.LegislationChancellor;
            eb.setDescription("Please select a policy to play using !policy <#>");
            privateChannel.sendMessage(eb.build()).queue();
        });

    }

    public static int getHandSize() {
       return hand.deck.size();
    }

    public static void peekThree(EmbedBuilder eb) {
        if(deck.deck.size() < 3){
            Collections.shuffle(discard.deck);
            deck.deck.addAll(discard.deck);
            discard = new Deck();
        }
        ArrayList<Policy> policyList = deck.peek();
        for (int i = 0; i < 3 ; i++) {
            eb.addField("Policy ", policyList.get(i).getRole(), false);
        }

    }

    public static void printFascists(EmbedBuilder eb) {
        for (Player p: fascists) {
            eb.addField(p.getName(), p.getRole().getSecretRole(), false);
        }
    }

    public static void updateNextPres() {
        nextLocation = (presidentLocation >= playerCount - 1) ? 0 : presidentLocation + 1;
    }

    public static void updateNextChosenPres(User pres) {
        nextLocation = players.indexOf(findPlayer(pres.getId()));
    }
    public static void updateNextChosenPres() {
        nextLocation = (prevPresident >= playerCount - 1) ? 0 : prevPresident + 1;
    }

    public static void resetGame (TextChannel channel) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Fascists:");
        printFascists(eb);
        channel.sendMessage(eb.build()).queue();
        PlayerList.resetPlayers();
        gameStage = GameStage.Idle;
        eb.clear();
        SecretHitlerLobby.showLobby(channel);
        eb.setTitle("To play again with all players in lobby, type !start");
        channel.sendMessage(eb.build()).queue();
    }


    public static void addVeto(boolean vote, MessageReactionAddEvent event) {
        if (vote) {
            yeet++;
        } else {
            yeetnt++;
        }

        if(yeet + yeetnt == 2) {
            Policy p = board.vetoPolicy();
            EmbedBuilder eb = new EmbedBuilder();
            if(yeet == 2) {
                //election ticker
                eb.setTitle("Veto'd the " + p.getRole() + " policy");
                discard.discard(p);
    //TODO: fix this to diff func call
                Gamestate.nextPres();
                Gamestate.updateNextPres();
                event.getTextChannel().sendMessage(eb.build()).queue();
                SecretHitlerLobby.showLobby(event.getChannel());
                SecretHitlerLobby.requestElection(event.getTextChannel());
            } else {
                eb.setTitle("Failed to veto the policy");
                event.getTextChannel().sendMessage(eb.build()).queue();
                board.doAction(event.getTextChannel(), p);
            }

        }
    }

    public static void removeVeto(boolean vote) {
        if (vote) {
            yeet--;
        } else {
            yeetnt--;
        }
    }

    public static void DeadCountAdd() {
        Gamestate.deadCount++;
    }
}
