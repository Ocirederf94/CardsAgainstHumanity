package org.academiadecodigo.bootcamp;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Created by codecadet on 01/03/17.
 */
public class Player extends Client {

    private List<String> hand;
    private List<String> table;
    private boolean czar = false;
    private int score = 0;
    private String card;
    private String cardUsed;
    private String cardToClient;
    private String winningCard;


    public Player() {
        hand = new ArrayList<>();
        table = new ArrayList<>();
        play();
    }


    public void blackCard(){ //get black card
        String blackCard ;
        if ((blackCard = getMessageFromServer()).contains("> black")){
            blackCard = blackCard + "\n";
            System.out.println("Black Card: " + blackCard);
        }
    }


    public void addCard() {
        if (getMessageFromServer().contains("> white")) {
            this.card = getMessageFromServer();
            this.hand.add(card);
        }
        if (getMessageFromServer().contains("<")) {
            System.out.println("Chat Message: " + getMessageFromServer());
        }
    }


    public boolean isCzar() {
        return czar;

    }

    public void play() {
        for (int i = 0; i < 10; i++) {
            addCard();
        }

        while (true) {

            if (!isCzar()) { ////// server will choose czar and creat a get method of the boolean
                try {
                    cardUsed = getPlayedCard();
                    cardToClient = cardUsed;
                    hand.remove(cardUsed);
                    System.out.println("Your Score: " + getScore());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                addCard();

            } else {
                winningCard = chooseWinningCard();
                for (int i = 0; i < table.size(); i++) {
                    table.remove(i);
                }

            }
        }
    }

    public String getTable() { /// in case we want to send all the cards at the same time
        String cards = null;
        Iterator<String> it = table.iterator();
        while (it.hasNext()) {
            cards = cards + it.next();

        }
        return cards;
    }

    public String getCardToClient() { ///Send players cards to client
        return cardToClient;
    }

    public String getWinningCard() {/// show card chosen by Czar
        return winningCard;
    }

    private String chooseWinningCard() {///////Czar chooses winning card
        System.out.println("You are the Czar, choose the winning card!!");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose card: ");
        int choice = scanner.nextInt();
        if (choice <= table.size()) {
            for (int i = 0; i < table.size(); i++) {
                if (choice == i) {
                    System.out.println("The winning card is: " + table.get(i));
                    return table.get(i);
                }
                System.out.println("Can't find the card!!");
            }
        }
        System.out.println("That's not a valid choice");
        return chooseWinningCard();
    }

    public String getPlayedCard() throws IOException {///Choose the white card from hand
        for (int i = 0; i < hand.size(); i++) {
            if (i == chooseCardInHand()) {
                System.out.println("You choose the card: " + hand.get(i));
                table.add(hand.get(i)); // Adding card to the table
                return hand.get(i);

            }
        }
        System.out.println("Can't find the card!!");
        return getPlayedCard();
    }

    public int chooseCardInHand() throws IOException { ////////Choose number from available cards
        blackCard();
        System.out.println(hand);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Chose card: ");
        int choice = scanner.nextInt();
        if (choice <= hand.size()) {
            System.out.println("You choose the number: " + choice);
            return choice;
        }
        System.out.println("That's not a valid choice");
        return chooseCardInHand();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = this.score + score;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
