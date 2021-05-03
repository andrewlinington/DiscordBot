package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;
import main.DiscordBot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    
    public ArrayList<Policy> deck ;
    
    public Deck () {
        deck = new ArrayList<>();
    }



    public void createDeck () {
        BufferedImage liberal = null;
        BufferedImage fascist = null;
        try {
            liberal = ImageIO.read(new File( DiscordBot.FILE_PATH + "Liberal.png"));
            fascist = ImageIO.read(new File( DiscordBot.FILE_PATH + "Fascist.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 6; i++) {
            deck.add(new Policy(RoleType.Liberal,liberal));
        }
        for (int i = 0; i < 11; i++) {
            deck.add(new Policy(RoleType.Fascist,fascist));
        }
        Collections.shuffle(deck);
    }

    public Policy draw() {
        return deck.remove(0);
    }

    public ArrayList<Policy> peek () {
        return deck;
    }

    public void discard (Policy card) {
        deck.add(card);
    }

    public boolean isEmpty() {
        return deck.isEmpty();
    }

    public Policy remove(int i) {
        return deck.remove(i);
    }
}
