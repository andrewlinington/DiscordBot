package Commands.SH.utils;

import Commands.SH.Commands.SecretHitlerLobby;
import Commands.SH.Helper.EmbededHelper;
import Commands.SH.utils.enums.BoardType;
import Commands.SH.utils.enums.GameStage;
import main.utils.ServerGame;
import net.dv8tion.jda.api.EmbedBuilder;
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
public class Board {

    private final ArrayList<Policy> fascist;
    private final ArrayList<Policy> liberal;
    private final BoardType type;
    private boolean hitlerWin = false;
    private boolean canVeto = false;


    private Policy toPlay;


    public Board (int numPlayers) {
        if (numPlayers < 7) {
            type = BoardType.SMALL;
        } else if (numPlayers < 9){
            type = BoardType.MED;
        } else {
            type = BoardType.LARGE;
        }
        fascist = new ArrayList<>();
        liberal = new ArrayList<>();
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
            hitlerWin = fascist.size() >= 3;
        } else {
            liberal.add(p);
        }
        EmbededHelper.sendEmbed(channel ,  EmbededHelper.createEmbeded("Policy added: " + p.getRole(), ((p.getRole().equals("Fascist")) ? Color.red :Color.BLUE)),false);
    }

    private void performAction(TextChannel channel, Policy p) {
        if(canVeto){
            tryVeto(channel, p);
            return;
        }
        doAction(channel,p);
    }

    public void addForcedPolicy (Policy p, TextChannel channel) {
        addPolicyToBoard(p, channel);
        showBoard(channel,p);
        checkWinConditions(channel);
    }

    private boolean checkWinConditions( TextChannel channel) {
        Gamestate gs = ServerGame.getGames().get(channel.getGuild().getIdLong()).getGamestate();
        if (fascist.size() == 6) {
            //Win condition
            EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("Fascists Win!" , Color.red,"All six fascist Policies have been played."),true);
            gs.resetGame(channel);
            return true;
        }
        else if (liberal.size() == 5) {
            //Win condition
            EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("Liberals Win!", Color.BLUE, "All five liberal Policies have been played."),false);
            gs.resetGame(channel);
            return true;
        }
        return false;
    }

    public void doAction (TextChannel channel, Policy p) {
        Gamestate gs = ServerGame.getGames().get(channel.getGuild().getIdLong()).getGamestate();
        showBoard(channel,p);
        if(checkWinConditions(channel)) {
            return;
        }
        if(p.getRole().equals("Fascist")) {
            if(fascist.size()==5){
                canVeto = true;
            }

            if ((BoardType.MED == type && fascist.size() == 2) || (BoardType.LARGE == type && (fascist.size() == 2 || fascist.size() == 1))) {
                gs.setGameStage(GameStage.Investigate);
                EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("The President must choose who to investigate" ,Color.green , "use !info <@name> to investigate someone"),false);
                return;
            } else if (fascist.size() == 4 || fascist.size() == 5) {
                gs.setGameStage(GameStage.Shoot);
                EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("The President must choose who to shoot" ,Color.green , "use !shoot <@name> to kill someone"),false);
                return;
            } else if ((BoardType.MED == type || BoardType.LARGE == type) && fascist.size() == 3) {
                gs.setGameStage(GameStage.Pick);
                EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("The President must choose the next president" ,Color.green , "use !pick <@name>"),false);
                return;
            } else if (BoardType.SMALL == type && fascist.size() == 3) {
                EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("The President gets to see the next 3 Policy's" , Color.green),true);
                gs.getPlayers().get(gs.getPresidentLocation()).getUser().openPrivateChannel().queue(privateChannel -> {
                    EmbededHelper.sendEmbed(privateChannel, EmbededHelper.createEmbeded("Top three Policies" , Color.yellow,"", gs.peekThree() ),true);
                });
            }
        }
        gs.nextPres();
        gs.updateNextPres();
        SecretHitlerLobby.requestElection(channel);
    }

    private void showBoard(TextChannel channel, Policy p) {
        showFascistTrack(channel,p);
        showLiberalTrack(channel,p);
    }


    private void showLiberalTrack(TextChannel channel, Policy p) {
        File in = new File (FILE_PATH + "temp/LiberalTrack.png");
        if(!in.isFile()) {
            in = new File(FILE_PATH + "LiberalTrack.png");
        }
        if(p.getRole().equals("Liberal")) {
            try {
                BufferedImage b = ImageIO.read(in);
                Graphics2D g = b.createGraphics();
                g.drawImage(b, 0, 0, null);
                g.drawImage(p.getImage(), getXDistLiberal(), 89, null);
                g.dispose();
                File out = new File (FILE_PATH + "temp/LiberalTrack.png");
                ImageIO.write(b,"PNG", out);
                in = new File (FILE_PATH + "temp/LiberalTrack.png");
            } catch (IOException e) {
                System.out.println("Error on file for some reason");
            }
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setColor(Color.BLUE);
        eb.setTitle("Liberal");
        eb.setImage("attachment://LiberalBoard.png");
        channel.sendMessage(eb.build()).addFile(in,"LiberalBoard.png").queue();
    }


    private void showFascistTrack(TextChannel channel, Policy p) {
        //TODO: show other types of tracks for 7-8 and 9-10
        File in = new File (FILE_PATH + "temp/FascistTrack.png");
        if(!in.isFile()) {
            in = new File(FILE_PATH + "FascistTrack.png");
        }
        if(p.getRole().equals("Fascist")) {
            try {
                BufferedImage b = ImageIO.read(in);
                Graphics2D g = b.createGraphics();
                g.drawImage(b, 0, 0, null);
                g.drawImage(p.getImage(), getXDistFascist(), 63, null);
                g.dispose();
                File out = new File (FILE_PATH + "temp/FascistTrack.png");
                ImageIO.write(b,"PNG", out);
                in = new File (FILE_PATH + "temp/FascistTrack.png");
            } catch (IOException e) {
                System.out.println("Error on file for some reason");
            }
        }

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Fascist");
        eb.setColor(Color.red);
        eb.setImage("attachment://FascistBoard.png");
        channel.sendMessage(eb.build()).addFile(in,"FascistBoard.png").queue();
    }

    private int getXDistFascist() {
        return 63 + 143 * (fascist.size() -1);
    }

    private int getXDistLiberal() {
        return 197 + 203 * (liberal.size() -1);
    }

    public boolean isHitlerWin() {
        return hitlerWin;
    }

    private void  tryVeto(TextChannel channel, Policy p) {
        toPlay = p;
        EmbededHelper.sendEmbed(channel, EmbededHelper.createEmbeded("Veto the Policy?", Color.cyan),false);
        ServerGame.getGames().get(channel.getGuild().getIdLong()).getGamestate().setGameStage(GameStage.Veto);
    }

   public Policy vetoPolicy () {
        if(toPlay.getRole().equals("Fascist")){
            fascist.remove(toPlay);
        } else {
            liberal.remove(toPlay);
        }
        return toPlay;
    }

}
