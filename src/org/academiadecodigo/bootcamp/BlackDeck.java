package org.academiadecodigo.bootcamp;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 01/03/17.
 */
public class BlackDeck {

    private final ReadFile readFile;
    private int numberOfBlackCardsInDeck = 1448;
    private ConcurrentHashMap<String, String> blackDeck;

    public BlackDeck() {
        blackDeck = new ConcurrentHashMap<>();
        readFile = new ReadFile("resources/black");
    }

    public void makeDeck() {
        String card;
        while ((card = readFile.retreive()) != null) {
            String id = card.split("\t")[0];
            blackDeck.put(id, card);
        }
    }

    public String giveCard() {

        String id = "";
        String card = "";

        int rndm = (int) ((Math.random() * numberOfBlackCardsInDeck) + 1);
        id = "" + rndm;

        card = blackDeck.get(id);
        blackDeck.remove(id);

        String hand = card;

        return hand;
    }
}
