package org.academiadecodigo.bootcamp;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 01/03/17.
 */
public class WhiteDeck {
    private ConcurrentHashMap<String, String> deck;
    private int numberCards = 200;
    private String hand;

    public void makeDeck() {
        String card = ReadFile.retreive("resources/white");
        while (!card.equals("eof")) {
            String id = card.split(" ")[0];
            deck.put(id, card);
        }
    }

    public String giveCard(int howMany) {
        Iterator<String> it = deck.keySet().iterator();
        String hand = "";
        String id = "";
        String card = "";
        for (int i = 0; i < howMany; i++) {
            id = it.next();
            card = deck.get(id);
            deck.remove(card);
            hand += id + card + "\n";
        }
        return hand;
    }
}

    /*
    Então enviamos para o cliente um string com as respectivas cartas.
    tabalhar metodo no player para receber string e enviar a linha respectiva de volta para o servidor
     */
