package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by codecadet on 05/03/17.
 */
public class Player2 {
    private Client client;
    private java.lang.String cards;
    private ArrayList<java.lang.String> hand;
    private ArrayList<java.lang.String> table;
    private int intGet = 0;

    public Player2() {
        client = new Client();
        try {
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        hand = new ArrayList<>();
        table = new ArrayList<>();

    }

    public void addCardsFirstPlay() {
        cards = client.getMessageFromServer();
        for (int i = 0; i < 10; i++) {
            hand.add(cards);
            System.out.println("Adding to hand: " + hand);

        }
    }

    public void addCards() {
        System.out.println("Before cards = client.getMessageFromServer();\n");
        cards = client.getMessageFromServer();
        System.out.println("After cards = client.getMessageFromServer();\n");
        hand.add(cards);
        System.out.println("Adding to hand: " + hand);
    }

    public void play() {
        while (true) {
            System.out.println("In play");
            addCards();
            System.out.println("After addCards");
            playCard();
            System.out.println("After playCard()");
            System.out.println("You removed: " + hand.remove(intGet));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            addCards();
        }
    }

    private String playCard() {
        int cardToPlay = -1;
        System.out.println("YOUR HAND: " + hand);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Chose Card: ");
        cardToPlay = scanner.nextInt();
        intGet = cardToPlay;
        if (cardToPlay <= hand.size()) {
            System.out.println("You choose the card: " + hand.get(cardToPlay));
            client.writeMessage(hand.get(cardToPlay));
            return hand.get(cardToPlay);

        }
        System.out.println("Can't find the card");
        playCard();
        return null;
    }


    public static void main(String[] args) {
        Player2 player = new Player2();
        player.play();
    }
}
