package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


/**
 * Created by codecadet on 05/03/17.
 */
public class Player2 {
    private Client client;
    private String cards;
    private String tableCards;
    private ArrayList<java.lang.String> hand;
    private ArrayList<java.lang.String> table;
    private int intGet = 0;
    private String blackCard;

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
        cards = null;
        System.out.println("Before cards = client.getMessageFromServer();\n");
        cards = client.getMessageFromServer();
        System.out.println("After cards = client.getMessageFromServer();\n");
        hand.add(cards);
        System.out.println("Adding to hand: " + hand);
    }

    public void addBlackCard() {
        System.out.println("I am in black card!!!");
        blackCard = null;
        if ((blackCard = client.getMessageFromServer()).contains(">black ")) {
            blackCard += "\n";
            System.out.println("BlacK Card: " + blackCard);
        }
    }

    public void play() {
        while (true) {

            if (client.getMessageFromServer().contains(">isCzar")) {
                System.out.println("I am THE CZAR!!!!");
                chooseCard();
                System.out.println("After chooseCard()");
                for (int i = 0; i < table.size() ; i++) {
                    System.out.println("Removing from table: " + table.remove(i));
                }
                while (!client.getMessageFromServer().contains(">winnerCard")) {
                    try {
                        System.out.println("Waiting for winner IN CZAR");
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
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
            while (!client.getMessageFromServer().contains(">winnerCard")) {
                try {
                    System.out.println("Waiting for winner");
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private String chooseCard() { /////// METHOD FROM CZAR
        int cardToChoose = -1;
        addBlackCard();
        getTableCards();
        while (table.size() < 4){
            System.out.println("Waiting for players to Play");
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("CZAR TABLE: " + table);
        System.out.println("Choose the Winner Card!!!");
        Scanner scanner = new Scanner(System.in);
        cardToChoose = scanner.nextInt();
        if (cardToChoose <= table.size()) {
            System.out.println("You choose the card: " + table.get(cardToChoose));
            client.writeMessage(table.get(cardToChoose));
            return table.get(cardToChoose);
        } else {
            System.out.println("Can´ find the card");
            chooseCard();
        }
        return null;
    }

    private void getTableCards() { ///// METHOD FOR CZAR TO GET CARD TO TABLE

        if ((tableCards = client.getMessageFromServer()).contains(">table")){
            table.add(tableCards);
        }

    }

    private String playCard() {
        int cardToPlay = -1;
        System.out.println("YOUR HAND: " + hand);
        System.out.println("Size of the hand: " + hand.size());
        addBlackCard();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose Card: ");
        cardToPlay = scanner.nextInt();
        intGet = cardToPlay;
        if (cardToPlay <= hand.size()) {
            System.out.println("You choose the card: " + hand.get(cardToPlay));
            client.writeMessage(hand.get(cardToPlay));
            return hand.get(cardToPlay);

        } else {
            System.out.println("Can't find the card");
            playCard();

        }
        return null;
    }


    public static void main(String[] args) {
        Player2 player = new Player2();
        player.play();
    }
}
