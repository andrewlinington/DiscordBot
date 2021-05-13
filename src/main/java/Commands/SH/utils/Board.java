package Commands.SH.utils;

import Commands.SH.Commands.SecretHitlerLobby;
import Commands.SH.Helper.EmbededHelper;
import Commands.SH.utils.enums.BoardType;
import Commands.SH.utils.enums.GameStage;
import lombok.Getter;
import lombok.Setter;
import main.utils.ServerGame;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static main.DiscordBot.FILE_PATH;



//TODO: REFACTOR

@Getter
@Setter
public class Board {

    //Maybe give this function a game to work off of

    private final ArrayList<Policy> fascist;
    private final ArrayList<Policy> liberal;

    private ArrayList<WinCondition> winConditions;
    private final BoardType type;

    private Policy toPlay;

    //TODO: pass in the game
    public Board (int numPlayers) {
        type = setBoardType(numPlayers);
        setWinConditions();
        fascist = new ArrayList<>();
        liberal = new ArrayList<>();
    }

    /**
     * sets the board for a given player size
     * @param numPlayers the number of players
     * @return the type of board
     */
    private BoardType setBoardType (int numPlayers) {
        return (numPlayers < 7)? BoardType.SMALL: (numPlayers < 9) ? BoardType.MED: BoardType.LARGE;
    }

    /**
     * Allows for dynamic winConditions to be added
     */
    private void setWinConditions () {
        winConditions = new ArrayList<>();
        winConditions.add(this::checkLiberalWin);
        winConditions.add(this::checkFascistWin);
    }


    public void addPolicy (Policy p, User author) {
        Guild g = ServerGame.findUserServer(author);
        TextChannel channel = g.getTextChannelById(ServerGame.getGames().get(g.getIdLong()).getConfig().getBot_channel());
        addPolicyToBoard(p, channel);
        performAction(channel, p);
    }

    private void addPolicyToBoard(Policy p, TextChannel channel) {
        if(p.getRole().equals("Fascist")){
            fascist.add(p);
        } else {
            liberal.add(p);
        }
        EmbededHelper.sendEmbed(channel ,  EmbededHelper.createEmbeded("Policy added: " + p.getRole(), ((p.getRole().equals("Fascist")) ? Color.red :Color.BLUE)),false);
    }

    private void performAction(TextChannel channel, Policy p) {
        if(fascist.size() == 5){
            tryVeto(channel, p);
        } else {
            doAction(channel, p);
        }
    }

    public void addForcedPolicy (Policy p, TextChannel channel) {
        addPolicyToBoard(p, channel);
        showBoard(channel,p);
        checkWinConditions(channel);
    }

    /**
     * checks the win conditions for both Parties
     * @param channel the channel to send a message to
     * @return if the game ended
     */
    private boolean checkWinConditions( TextChannel channel) {
        Gamestate gs = ServerGame.getGames().get(channel.getGuild().getIdLong()).getGamestate();
        return winConditions.stream().filter((w) -> w.canWin(channel,gs)).findFirst().orElse(null) != null;
    }

    /**
     * The win condition for Fascists with Policies
     * @param channel the channel to send a message too
     * @param gs the game state
     * @return if the game ended
     */
    private boolean checkFascistWin(TextChannel channel, Gamestate gs) {
        if (fascist.size() == 6) {
            //Win condition
            EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("Fascists Win!" , Color.red,"All six fascist Policies have been played."),true);
            gs.resetGame(channel);
            return true;
        }
        return false;
    }

    /**
     * The win condition for Liberals with Policies
     * @param channel the channel to send a message too
     * @param gs the game state
     * @return if the game ended
     */
    private boolean checkLiberalWin (TextChannel channel, Gamestate gs) {
        if (liberal.size() == 5) {
            EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("Liberals Win!", Color.BLUE, "All five liberal Policies have been played."),true);
            gs.resetGame(channel);
            return true;
        }
        return false;
    }

    public void doAction (TextChannel channel, Policy p) {
        Gamestate gs = ServerGame.getGames().get(channel.getGuild().getIdLong()).getGamestate();
        showBoard(channel,p);
        if(!(checkWinConditions(channel) || executiveAction(p ,gs ,channel))) {
            gs.nextPres();
            gs.updateNextPres();
            SecretHitlerLobby.requestElection(channel);
        }
    }


    private boolean executiveAction (Policy p, Gamestate gs, TextChannel channel) {
        if(p.getRole().equals("Fascist")) {
            return investegationCheck(gs,channel) || bulletCheck(gs, channel) || specialElectionCheck(gs, channel) || policyPeekCheck(gs,channel) ;
        }
        return false;
    }

    /**
     * checks if a policy can be peeked
     * @param gs the gamestate
     * @param channel the channel to use
     * @return if the action occurred
     */
    private  boolean policyPeekCheck (Gamestate gs, TextChannel channel) {
        if (BoardType.SMALL == type && fascist.size() == 3) {
            EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("The President gets to see the next 3 Policy's" , Color.green),true);
            gs.getPlayers().get(gs.getPresidentLocation()).getUser().openPrivateChannel().queue(privateChannel -> {
                EmbededHelper.sendEmbed(privateChannel, EmbededHelper.createEmbeded("Top three Policies" , Color.yellow,"", gs.peekThree() ),true);
            });
        }
        return false;
    }

    /**
     * checks for a special Election
     * @param gs the game state
     * @param channel the channel the game takes place in
     * @return true if the action occurred otherwise false
     */
    private  boolean specialElectionCheck (Gamestate gs, TextChannel channel) {
        if ((BoardType.MED == type || BoardType.LARGE == type) && fascist.size() == 3) {
            gs.setGameStage(GameStage.Pick);
            EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("The President must choose the next president" ,Color.green , "use !pick <@name>"),false);
            return true;
        }
        return false;
    }

    /**
     * checks if a bullet can be played
     * @param gs the current game
     * @param channel the current channel
     * @return
     */
    private boolean bulletCheck (Gamestate gs, TextChannel channel) {
        if (fascist.size() == 4 || fascist.size() == 5) {
            gs.setGameStage(GameStage.Shoot);
            EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("The President must choose who to shoot" ,Color.green , "use !shoot <@name> to kill someone"),false);
            return true;
        }
        return false;
    }

    /**
     * Checks if an investigation can occur
     * @param gs the gamestate
     * @param channel the channel that the message is to be sent to
     * @return the investigation
     */
    private boolean investegationCheck (Gamestate gs, TextChannel channel) {
        if ((BoardType.MED == type && fascist.size() == 2) || (BoardType.LARGE == type && (fascist.size() == 2 || fascist.size() == 1))) {
            gs.setGameStage(GameStage.Investigate);
            EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("The President must choose who to investigate" ,Color.green , "use !info <@name> to investigate someone"),false);
            return true;
        }
        return false;
    }

    /**
     * shows both the Fascist and Liberal tracks
     * @param channel the channel for the tracks to be shown
     * @param p the policy to be played
     */
    private void showBoard(TextChannel channel, Policy p) {
        showTrack(channel,p,"Liberal");
        showTrack(channel,p,"Fascist");
    }

    /**
     * the track to be shown to the user
     * @param channel the channel to be sent to
     * @param p the policy being played
     * @param roleType the track type
     */
    private void showTrack (TextChannel channel, Policy p, String roleType) {
        String filePath = FILE_PATH + "temp/" + roleType + "Track" + channel.getGuild().getId() + ".png";
        EmbededHelper.sendEmbed(channel,  updateBoard(filePath, getBoardFile(filePath,roleType), p, roleType),
                EmbededHelper.createEmbededImage(roleType,((roleType.equals("Liberal"))? Color.BLUE:Color.red), roleType + ".png"),roleType + ".png");
    }

    /**
     * Updates the File path based on the policy played
     * @param filePath the files path
     * @param in the file with the track
     * @param p the policy to be played
     * @param roleType the role of the current track
     * @return the completed policy track
     */
    private File updateBoard (String filePath, File in, Policy p, String roleType) {
        if(p.getRole().equals(roleType)) {
            try {
                ImageIO.write(generateCombinedBoard(in, p),"PNG", new File (filePath));
                in = new File (filePath);
            } catch (IOException e) {
                System.out.println("Error on file for some reason");
            }
        }
        return in;
    }

    /**
     * creates the track by combining images
     * @param in the track file
     * @param p the policy to be played
     * @return the completed board image
     * @throws IOException the image does not exist
     */
    private BufferedImage generateCombinedBoard(File in, Policy p) throws IOException {
        BufferedImage b = ImageIO.read(in);
        Graphics2D g = b.createGraphics();
        g.drawImage(b, 0, 0, null);
        g.drawImage(p.getImage(), getXDist(p), ((p.getRole().equals("Liberal")) ? 89 : 63), null);
        g.dispose();
        return b;
    }

    /**
     * Gets the boards file
     * @param filePath the path of the file
     * @param roleType the type of board
     * @return the full file where the board is located
     */
    private File getBoardFile(String filePath, String roleType) {
        File in = new File(filePath);
        if(!in.isFile()){
            in = new File(FILE_PATH + roleType + "Track" + ((!roleType.equals("Fascist")) ? "": boardSize()) +".png");
        }
        return in;
    }

    /**
     * gets the X distance for the policy placement
     * @param p the policy  to be played
     * @return the location
     */
    private int getXDist(Policy p) {
        return (p.getRole().equals("Liberal")) ? getXDistLiberal(): getXDistFascist() ;
    }

    /**
     * gets the X distance for the Fascist policy
     * @return the x location
     */
    private int getXDistFascist() {
        return 63 + 143 * (fascist.size() -1);
    }

    /**
     * gets the X distance for the Fascist policy
     * @return the x location
     */
    private int getXDistLiberal() {
        return 197 + 203 * (liberal.size() -1);
    }

    /**
     * gets the board location in a file
     * @return the board location for file parsing
     */
    private String boardSize () {
        return (type.equals(BoardType.SMALL)) ? "1" : (type.equals(BoardType.MED)) ? "2" : "3";
    }

    /**
     * returns if the board state is able to have Hitler Win
     * @return the status of possible victory
     */
    public boolean isHitlerWin(){
        return fascist.size() >= 3;
    }


    //TODO: give adjustment
    private void  tryVeto(TextChannel channel, Policy p) {
        toPlay = p;
        EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("Veto the Policy?", Color.cyan),false);
        ServerGame.getGames().get(channel.getGuild().getIdLong()).getGamestate().setGameStage(GameStage.Veto);
    }

    //TODO: give adjustment
   public Policy vetoPolicy () {
        if(toPlay.getRole().equals("Fascist")){
            fascist.remove(toPlay);
        } else {
            liberal.remove(toPlay);
        }
        return toPlay;
    }

}
