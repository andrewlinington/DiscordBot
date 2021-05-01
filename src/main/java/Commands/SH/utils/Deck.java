package Commands.SH.utils;

import Commands.SH.utils.enums.RoleType;

import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    
    public ArrayList<Policy> deck ;
    
    public Deck () {
        deck = new ArrayList<>();
    }



    public void createDeck () {
        for (int i = 0; i < 6; i++) {
            deck.add(new Policy(RoleType.Liberal));
        }
        for (int i = 0; i < 11; i++) {
            deck.add(new Policy(RoleType.Fascist));
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
