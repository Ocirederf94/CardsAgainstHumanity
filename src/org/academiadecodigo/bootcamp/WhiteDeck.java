package org.academiadecodigo.bootcamp;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 01/03/17.
 */
public class WhiteDeck {
    private ReadFile readFile;
    private ConcurrentHashMap<String, String> whiteDeck;
    private int numberOfWhiteCardsInDeck = 4562;
    private String hand;


    public WhiteDeck() {
        whiteDeck = new ConcurrentHashMap<>();
        readFile = new ReadFile("resources/white");
    }

    public void makeDeck() {

        String card;
        while ((card = readFile.retreive()) != null) {
            String id = card.split("\t")[0];
            whiteDeck.put(id, card);
        }
    }

    public String giveCard(int howMany) {

        Iterator<String> it = whiteDeck.keySet().iterator();
        String hand = "";
        String id = "";
        String card = "";
        for (int i = 0; i < howMany; i++) {
            int rndm = (int) (Math.random() * numberOfWhiteCardsInDeck);
            id = "" + rndm;
            card = whiteDeck.get(id);
            whiteDeck.remove(id);
            hand += card + "\n";
        }
        System.out.println(hand);
        return hand;
    }
}