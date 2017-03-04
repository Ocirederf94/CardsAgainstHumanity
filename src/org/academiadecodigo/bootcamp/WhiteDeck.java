package org.academiadecodigo.bootcamp;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 01/03/17.
 */
public class WhiteDeck {
    private ReadFile readFile;
    private ConcurrentHashMap<String, String> deck;
    private int numberOfWhiteCardsInDeck = 4959;
    private String hand;


    public WhiteDeck() {
        deck = new ConcurrentHashMap<>();
        readFile = new ReadFile("resources/white");
    }

    public void makeDeck() {

        String card;
        while ((card = readFile.retreive()) != null) {
            String id = card.split("\t")[0];
            deck.put(id, card);
        }
    }

    public String giveCard(int howMany) {

        Iterator<String> it = deck.keySet().iterator();
        String hand = "";
        String id = "";
        String card = "";
        for (int i = 0; i < howMany; i++) {
            int rndm = (int) ((Math.random() * numberOfWhiteCardsInDeck) + 1);
            id = "" + rndm;
            card = deck.get(id);
            deck.remove(id);
            hand += card + "\n";
        }
        return hand;
    }
}