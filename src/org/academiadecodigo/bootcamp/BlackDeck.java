package org.academiadecodigo.bootcamp;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 01/03/17.
 */
public class BlackDeck {
    private ConcurrentHashMap<String, String> deck;


    public void makeDeck() {
        String card = ReadFile.retreive("resources/black");
        while (!card.equals("eof")) {
            String id = card.split(" ")[0];
            deck.put(id, card);
        }
    }

    public String giveCard() {
        Iterator<String> it = deck.keySet().iterator();

        String id = "";
        String card = "";

        id = it.next();

        card = deck.get(id);

        deck.remove(card);

        String hand = id + card;

        return hand;
    }
}
