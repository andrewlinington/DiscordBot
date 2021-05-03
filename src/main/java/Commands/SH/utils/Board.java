package Commands.SH.utils;

import Commands.SH.Commands.SecretHitlerLobby;
import Commands.SH.utils.enums.BoardType;
import Commands.SH.utils.enums.GameStage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

import static main.DiscordBot.*;

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


    public void addPolicy (Policy p, MessageReceivedEvent event) {
       EmbedBuilder eb = new EmbedBuilder();
        if(p.getRole().equals("Fascist")){
            fascist.add(p);
            hitlerWin = fascist.size() >= 3;
            eb.setColor(Color.RED);
        } else {
            liberal.add(p);
            eb.setColor(Color.BLUE);
        }
        eb.setTitle("Policy added: " + p.getRole());
        TextChannel channel = Objects.requireNonNull(API.getGuildById(config.getServer_id())).getTextChannelById(config.getBot_channel());
        channel.sendMessage(eb.build()).queue();
        performAction(channel, p);
    }

    private void performAction(TextChannel channel, Policy p) {
        if(canVeto){
            tryVeto(channel, p);
            return;
        }
        doAction(channel,p);

    }
   public void doAction (TextChannel channel, Policy p) {
        EmbedBuilder eb  = new EmbedBuilder();


        showBoard(channel,p);
        if(p.getRole().equals("Fascist")) {
            if(fascist.size()==5){
                canVeto = true;
            }

            if ((BoardType.MED == type && fascist.size() == 2) || (BoardType.LARGE == type && (fascist.size() == 2 || fascist.size() == 1))) {
                SecretHitlerLobby.showLobby(channel);
                Gamestate.setGameStage(GameStage.Investigate);
                eb.setColor(Color.green);
                eb.setTitle("The President must choose who to investigate");
                eb.appendDescription("use !info <@name> to investigate someone");
                channel.sendMessage(eb.build()).queue();
                return;
            } else if (fascist.size() == 4 || fascist.size() == 5) {
                Gamestate.setGameStage(GameStage.Shoot);
                SecretHitlerLobby.showLobby(channel);
                eb.setColor(Color.green);
                eb.setTitle("The President must choose who to shoot");
                eb.appendDescription("use !shoot <@name> to kill someone");
                channel.sendMessage(eb.build()).queue();
                return;
            } else if ((BoardType.MED == type || BoardType.LARGE == type) && fascist.size() == 3) {
                Gamestate.setGameStage(GameStage.Pick);
                SecretHitlerLobby.showLobby(channel);
                eb.setColor(Color.green);
                eb.setTitle("The President chooses the next president");
                eb.appendDescription("use !pick <@name>");
                channel.sendMessage(eb.build()).queue();
                return;
            } else if (BoardType.SMALL == type && fascist.size() == 3) {
                eb.setColor(Color.green);
                eb.setTitle("The President gets to see the next 3 Policy's");
                channel.sendMessage(eb.build()).queue();
                eb.clear();

                eb.setTitle("Top three Policies");
                Gamestate.peekThree(eb);
                Gamestate.getPlayers().get(Gamestate.getPresidentLocation()).getUser().openPrivateChannel().queue(privateChannel -> {
                    privateChannel.sendMessage(eb.build()).queue();
                });
            }else if (fascist.size() == 6) {
                //Win condition
                eb.setTitle("Fascists Win!");
                eb.setColor(Color.red);
                eb.setDescription("All six fascist Policies have been played.");
                channel.sendMessage(eb.build()).queue();
                Gamestate.resetGame(channel);
                return;
            }
        } else if (p.getRole().equals("Liberal") && liberal.size() == 5) {
            //Win condition
            eb.setTitle("Liberals Win!");
            eb.setColor(Color.blue);
            eb.setDescription("All five liberal Policies have been played.");
            channel.sendMessage(eb.build()).queue();
            Gamestate.resetGame(channel);
            return;
        }
        eb.clear();
        Gamestate.nextPres();
        Gamestate.updateNextPres();
        SecretHitlerLobby.showLobby(channel);
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
        eb.setTitle("Liberal");
        eb.setImage("attachment://LiberalBoard.png");
        channel.sendMessage(eb.build()).addFile(in,"LiberalBoard.png").queue();
    }


    private void showFascistTrack(TextChannel channel, Policy p) {
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
        eb.setImage("attachment://FascistBoard.png");
        channel.sendMessage(eb.build()).addFile(in,"FascistBoard.png").queue();
    }

    private int getXDistFascist() {
        return 63 + 203 * (fascist.size() -1);
    }

    private int getXDistLiberal() {
        return 197 + 203 * (liberal.size() -1);
    }

    public boolean isHitlerWin() {
        return hitlerWin;
    }

    private void  tryVeto(TextChannel channel, Policy p) {
        EmbedBuilder eb = new EmbedBuilder();
        toPlay = p;
        eb.setTitle("Veto the Policy?");
        eb.setColor(Color.CYAN);
        Gamestate.setGameStage(GameStage.Veto);
        channel.sendMessage(eb.build()).queue();
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
