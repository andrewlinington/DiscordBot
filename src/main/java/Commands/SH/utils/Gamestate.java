package Commands.SH.utils;

import Commands.SH.Commands.SecretHitlerLobby;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;

import java.awt.*;
import java.util.ArrayList;

public class Gamestate {

    private Gamestate () { }

    private static GameStage gameStage = GameStage.Idle;
    private static ArrayList<Player> players;


    //the locations of important navigation locations
    private static int presidentLocation;
    private static int nextLocation;
    private static int chancellorLocation;

    private static int playerCount;


    private static int yeet;
    private static int yeetnt;

    private static Deck deck;
    private static Deck discard;
    private static Deck hand;

    private static Board board;


    public static void setStart(int numPlayers, ArrayList<Player> players, int presidentLocation) {

        deck = new Deck();
        deck.createDeck();

        discard = new Deck();
        hand = new Deck();

        gameStage = GameStage.Election;
        playerCount = numPlayers;
        Gamestate.players = players;

        Gamestate.presidentLocation = presidentLocation;
        Gamestate.nextLocation = (presidentLocation == playerCount - 1) ? 0 : presidentLocation + 1;

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
        if (yeet + yeetnt == playerCount) {
            for (Player p : players) {
                p.removeVote();
                p.setFailedToVote(false);
            }
            EmbedBuilder eb = new EmbedBuilder();
            if (yeetnt >= yeet) {
                players.get(presidentLocation).setStatus(SecretHitlerStatus.Alive);
                players.get(chancellorLocation).setStatus(SecretHitlerStatus.Alive);

                presidentLocation = nextLocation;
                players.get(presidentLocation).setStatus(SecretHitlerStatus.President);

                Gamestate.nextLocation = (presidentLocation == playerCount - 1) ? 0 : presidentLocation + 1;

                eb.setTitle("The vote has failed!");
                eb.addField("Yeet:", String.valueOf(yeet), false);
                eb.addField("Yeetn't:", String.valueOf(yeetnt), false);
                eb.setDescription("The presidency has been passed to the next player");
                gameStage = GameStage.Election;

            } else {
                eb.setTitle("The vote has Passed!");
                eb.addField("Yeet:", String.valueOf(yeet), false);
                eb.addField("Yeetn't:", String.valueOf(yeetnt), false);
                eb.setDescription("The president and chancellor will now Legislate a policy");
                gameStage = GameStage.LegislationPres;
                checkHitlerWin(event);
            }
            event.getChannel().sendMessage(eb.build()).queue();
            SecretHitlerLobby.showLobby(event.getChannel());
            Message m = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete();
            m.delete().queue();
            yeetnt = 0;
            yeet = 0;
        }
    }


    private static void checkHitlerWin(MessageReactionAddEvent event) {
        if(players.get(chancellorLocation).getRole().getSecretRole().equals("Hitler") && board.isHitlerWin() ){
            //fascists win!
            System.out.println("Hitler wins");
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
            for (int i = 0; i < 3; i++) {
                if(deck.isEmpty()){
                    deck = discard;
                    discard = new Deck();
                }
                Policy p = deck.draw();
                String type = p.getRole();
                eb.addField("Policy " + (i +1), type , false);
                hand.discard(p);
            }
            eb.setColor(Color.ORANGE);
            eb.setDescription("Please select a policy using !policy <#1-3>");
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

    public static void setPresidentLocation(int presidentLocation) {
        Gamestate.presidentLocation = presidentLocation;
    }

    public static void setChancellorLocation(int chancellorLocation) {
        Gamestate.chancellorLocation = chancellorLocation;
    }

    public static void checkWin(MessageReceivedEvent event, int fascist, int liberal) {
        if (fascist == 6){
            gameStage = GameStage.Idle;
            //victory!!!
        } else if (liberal == 5) {
            gameStage = GameStage.Idle;
            //victory!!!
        }
    }
}
