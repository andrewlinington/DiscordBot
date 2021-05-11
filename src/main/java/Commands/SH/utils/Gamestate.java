package Commands.SH.utils;


import Commands.SH.Commands.SecretHitlerLobby;
import Commands.SH.Helper.EmbededHelper;
import Commands.SH.Helper.MessageHelper;
import Commands.SH.utils.enums.GameStage;
import Commands.SH.utils.enums.SecretHitlerStatus;
import lombok.Getter;
import lombok.Setter;
import main.utils.ServerGame;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.apache.commons.io.FileUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static main.DiscordBot.FILE_PATH;
//TODO: REFACTOR
@Getter
@Setter
public class Gamestate {




    //TODO: rejection status if rejected 3 times


    private GameStage gameStage;

    //TODO: pass object containing all the player types in and use that for general use
    private ArrayList<Player> players;


    //change to list of Fields maybe
    private ArrayList<Player> fascists;

    private int yeet;
    private int yeetnt;
    private int playerCount;


    //TODO: combine info into one object maybe?
    private int presidentLocation;
    private int nextLocation;
    private int chancellorLocation;
    private int prevPresident;
    private int prevChancellor;

    private int electionTracker;

    private Board board;




    //ToDO: make this maybe easier to interact with as one object in board maybe?
    private Deck deck;
    private Deck discard;
    private Deck hand;

    private int deadCount;

    public Gamestate () {
        players = new ArrayList<>();
        fascists = new ArrayList<>();

        deck = new Deck();
        discard = new Deck();
        hand = new Deck();

        reset();
    }

    private void reset () {
        gameStage = GameStage.Idle;
        players.clear();
        fascists.clear();
        yeet = 0;
        yeetnt = 0;
        resetDecks();
    }

    private void resetDecks () {
        deck.reset();
        discard.reset();
        hand.reset();
    }

    private  void resetFiles () {
        try {
            FileUtils.cleanDirectory(new File(FILE_PATH + "temp/"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createPlayersLists (int numPlayers, ArrayList<Player> players,ArrayList<Player> fascists) {
        this.fascists = fascists;
        this.players = players;
        playerCount = numPlayers;
        this.board = new Board(numPlayers);
    }


    private void storePlayerLocations (int presidentLoc) {
        this.presidentLocation = presidentLoc;
        nextLocation = (presidentLocation == playerCount - 1) ? 0 : presidentLocation + 1;
        chancellorLocation = -1;
        prevChancellor = -1;
        prevPresident = -1;
    }


    public void setStart(int numPlayers, ArrayList<Player> players,ArrayList<Player> fascists, int presidentLocation) {
        createPlayersLists(numPlayers, players, fascists);
        resetFiles();
        deck.createDeck();
        storePlayerLocations(presidentLocation);
    }

    /**
     * finds a player with the given id
     * @param id the id of the player
     * @return the player if they exist null otherwise
     */
    public Player findPlayer (String id){
        return players.stream().filter(player -> player.hasID(id)).findFirst().orElse(null);
    }



    //TODO: flush out code
    public void toggleVote (int type, Long id, MessageReactionAddEvent event ) {
        String s = event.getGuild().getEmoteById(id).getName();
        if(s.equalsIgnoreCase("yeet")) {
            yeet += type;
        } else {
            yeetnt += type;
        }

        if(gameStage.equals(GameStage.Voting)){
            checkVote(event);
        } else {
            onVeto(event);
        }
    }

    public void toggleVote (int type, Emote e ) {
        if(e.getName().equalsIgnoreCase("yeet")) {
            yeet += type;
        } else {
            yeetnt += type;
        }
    }






    //TODO: work on cleaning this up
    private void checkVote(MessageReactionAddEvent event) {
        if (yeet + yeetnt == playerCount - deadCount) {
            ServerGame.getLobby().get(event.getGuild()).resetVotePlayers();
            EmbedBuilder eb = new EmbedBuilder();
            if (yeetnt >= yeet) {
                //TODO: fix eligibility
                electionTracker++;
                electionTrackerCheck(event);
                nextPassedPres();

                eb.setTitle("The vote has failed!");
                eb.setColor(Color.yellow);
                eb.addField("Yeet:", String.valueOf(yeet), false);
                eb.addField("Yeetn't:", String.valueOf(yeetnt), false);
                eb.setDescription("The presidency has been passed to the next player");

                event.getChannel().sendMessage(eb.build()).queue();
                SecretHitlerLobby.requestElection(event.getTextChannel());

            } else {
                eb.setTitle("The vote has Passed!");
                eb.setColor(Color.green);
                eb.addField("Yeet:", String.valueOf(yeet), false);
                eb.addField("Yeetn't:", String.valueOf(yeetnt), false);
                eb.setDescription("The president and chancellor will now Legislate a policy");

                gameStage = GameStage.LegislationPres;

                event.getChannel().sendMessage(eb.build()).queue();
                checkHitlerWin(event);
            }
            Message m = event.getTextChannel().retrieveMessageById(event.getMessageId()).complete();
            m.delete().queue();
            yeetnt = 0;
            yeet = 0;
        }
    }

    private void electionTrackerCheck(MessageReactionAddEvent event) {
        if(electionTracker >= 3) {
            electionTracker = 0;
            MessageHelper.sendMessage(event.getTextChannel(), "Election ticker has reached Third Position!!! The country is thrown into chaos and a policy is played");
            board.addForcedPolicy(deck.draw(), event.getTextChannel());
        } else {
            MessageHelper.sendMessage(event.getTextChannel(), "Election Ticker is in the " + (((electionTracker == 1) ? "first" : "second") + " position"));
        }
    }

    //TODO: clean code
    public void nextPres() {
        if( prevPresident >= 0 && players.get(prevPresident).getStatus().equals(SecretHitlerStatus.Past_President)) {
            players.get(prevPresident).setStatus(SecretHitlerStatus.Alive);
        }
        if( prevChancellor >= 0 && players.get(prevChancellor).getStatus().equals(SecretHitlerStatus.Past_Chancellor)) {
            players.get(prevPresident).setStatus(SecretHitlerStatus.Alive);
        }
        if( players.get(presidentLocation).getStatus().equals(SecretHitlerStatus.President)) {
            players.get(presidentLocation).setStatus(SecretHitlerStatus.Past_President);
        }
        if( chancellorLocation >= 0 && players.get(chancellorLocation).getStatus().equals(SecretHitlerStatus.Chancellor)) {
            players.get(chancellorLocation).setStatus(SecretHitlerStatus.Past_Chancellor);
        }
        prevPresident = presidentLocation;
        prevChancellor = chancellorLocation;
        changePres();
    }


    public void nextPassedPres () {
        if(players.get(presidentLocation).getStatus().equals(SecretHitlerStatus.President)) {
            players.get(presidentLocation).setStatus(SecretHitlerStatus.Alive);
        }
        if(chancellorLocation >= 0 && players.get(chancellorLocation).getStatus().equals(SecretHitlerStatus.Chancellor)) {
            players.get(chancellorLocation).setStatus(SecretHitlerStatus.Alive);
        }
        if (prevPresident >= 0 ) players.get(prevPresident).setStatus(SecretHitlerStatus.Past_President);
        if (prevChancellor >= 0) players.get(prevChancellor).setStatus(SecretHitlerStatus.Past_Chancellor);
        changePres();
        updateNextPres();
    }

    private void changePres() {
        presidentLocation = nextLocation;
        while (players.get(presidentLocation).getStatus().equals(SecretHitlerStatus.Dead)) {
            presidentLocation = (presidentLocation >= playerCount) ? 0 : presidentLocation + 1;
        }
        presidentLocation = (presidentLocation >= playerCount) ? 0 : presidentLocation;
        players.get(presidentLocation).setStatus(SecretHitlerStatus.President);
    }


    private void checkHitlerWin(MessageReactionAddEvent event) {
        if(players.get(chancellorLocation).getRole().getSecretRole().equals("Hitler") && board.isHitlerWin() ){
            TextChannel channel = event.getTextChannel();
            EmbededHelper.sendEmbed(event.getTextChannel(), EmbededHelper.createEmbeded("Fascists Win!", Color.red,"Hitler has been elected."),false);
            ServerGame.getGuildGames().get(event.getGuild()).resetGame(channel);
        } else {
            messagePresident(event);
        }

    }

    private void messagePresident(MessageReactionAddEvent event) {

        Player president = players.get(presidentLocation);

        president.getUser().openPrivateChannel().queue(privateChannel -> {
          // draw 3 policies and reshuffle if missing any

            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Policies have been drawn");
            //can beadjusted later to be more dynamic in size for the initial hand
            for (int i = 0; i < 3; i++) {
                if(deck.isEmpty()){
                    deck = discard;
                    Collections.shuffle(deck.getDeck());
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


    public void nextHand(int i, MessageReceivedEvent event) {
        SecretHitlerStatus status = findPlayer(event.getAuthor().getId()).getStatus();
        if(status.equals(SecretHitlerStatus.President) && gameStage == GameStage.LegislationPres) {
            discard.discard(hand.remove(i -1));
            legislateChancellor();
        }
        else if (status.equals(SecretHitlerStatus.Chancellor) && gameStage == GameStage.LegislationChancellor){
            board.addPolicy(hand.remove(i -1), event.getAuthor());
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

    private void legislateChancellor() {
        players.get(chancellorLocation).getUser().openPrivateChannel().queue(privateChannel -> {
            EmbedBuilder eb = new EmbedBuilder();
            eb.setTitle("Your Policy's");
            eb.setColor(Color.ORANGE);
            for (int i = 0; i < getHandSize(); i++) {
                eb.addField("Policy "  + (i + 1), hand.getDeck().get(i).getRole(), false);
            }
            gameStage = GameStage.LegislationChancellor;
            eb.setDescription("Please select a policy to play using !policy <#>");
            privateChannel.sendMessage(eb.build()).queue();
        });

    }

    public int getHandSize() {
       return hand.getDeck().size();
    }

    public ArrayList<MessageEmbed.Field> peekThree() {
        if(deck.getDeck().size() < 3){
            Collections.shuffle(discard.getDeck());
            deck.getDeck().addAll(discard.getDeck());
            discard = new Deck();
        }
        ArrayList<Policy> policyList = deck.peek();
        ArrayList<MessageEmbed.Field> fields = new ArrayList<>();
        for (int i = 0; i < 3 ; i++) {
            fields.add(EmbededHelper.generateField("Policy ", policyList.get(i).getRole()));
        }
        return fields;

    }

    public void updateNextPres() {
        nextLocation = (presidentLocation >= playerCount - 1) ? 0 : presidentLocation + 1;
    }

    public void updateNextChosenPres(User pres) {
        nextLocation = players.indexOf(findPlayer(pres.getId()));
    }
    public void updateNextChosenPres() {
        nextLocation = (prevPresident >= playerCount - 1) ? 0 : prevPresident + 1;
    }

    /**
     * resets the game to an idle state
     * @param channel the channel to send data to
     */
    public void resetGame (TextChannel channel) {
        EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("Fascists:",Color.red,"", PlayerList.toFascistField(fascists)),false);
        PlayerList pl = ServerGame.getLobby().get(channel.getGuild());
        pl.resetPlayers();
        gameStage = GameStage.Idle;
        MessageHelper.sendMessage(channel,"To play again with all players in lobby, type !start");
    }


    public void onVeto( MessageReactionAddEvent event) {

        if(yeet + yeetnt == 2) {
            Policy p = board.vetoPolicy();
            EmbedBuilder eb = new EmbedBuilder();
            if(yeet == 2) {
                //election ticker
                electionTracker++;
                electionTrackerCheck(event);
                eb.setTitle("Veto'd the " + p.getRole() + " policy");
                discard.discard(p);
    //TODO: fix this to diff func call
                nextPres();
                updateNextPres();
                event.getTextChannel().sendMessage(eb.build()).queue();
                SecretHitlerLobby.requestElection(event.getTextChannel());
            } else {
                eb.setTitle("Failed to veto the policy");
                event.getTextChannel().sendMessage(eb.build()).queue();
                board.doAction(event.getTextChannel(), p);
            }

        }
    }

    public void DeadCountAdd() {
        deadCount++;
    }
}
