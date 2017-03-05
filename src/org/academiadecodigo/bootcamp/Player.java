package org.academiadecodigo.bootcamp;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created by codecadet on 01/03/17.
 */
public class Player {

    private ArrayList<String> hand = new ArrayList<>();
    private ArrayList<String> table = new ArrayList<>();
    private int score = 0;
    private String card;
    private String cardUsed;
    private String cardToClient;
    private String winningCard;
    private Client client;


    public Player() {
        try {
            /*hand = new ArrayList<>();
            table = new ArrayList<>();*/
            client = new Client();
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public void blackCard() { //get black card
        String blackCard;
        if ((blackCard = client.getMessageFromServer()).contains(">black")) {
            blackCard = blackCard + "\n";
            System.out.println("Black Card: " + blackCard);
        }
    }


    public void addCard() {
        hand.add(client.getMessageFromServer());
        /*if (client.getMessageFromServer().contains(">white")) {
        System.out.println("I am in add Card white ");
        }*/
        /*if (client.getMessageFromServer().contains("<")) {
            System.out.println("Chat Message: " + client.getMessageFromServer());
        }*/
    }

    public void play() {
        for (int i = 0; i < 10; i++) {
            addCard();
        }
        System.out.println(hand);
        System.out.println(hand.get(2));
        while (true) {

            if (client.getMessageFromServer().contains(">white")) { ////// server will choose czar and create a get method of the boolean
                try {
                    addCard();
                    System.out.println("I am in play after addcard");
                    cardUsed = getPlayedCard();
                    System.out.println("I am in play getPlayerCard");

                    cardToClient = cardUsed;
                    client.writeMessage(cardToClient);
                    hand.remove(cardUsed);
                    setScore();
                    System.out.println("Your Score: " + getScore());
                    addCard();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } /*else {
                winningCard = chooseWinningCard();
                client.writeMessage(winningCard);
                for (int i = 0; i < table.size(); i++) {
                    table.remove(i);
                }

            }*/
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


    private String chooseWinningCard() {///////Czar chooses winning card
        blackCard();
        System.out.println("You are the Czar choose the winning card!!");
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
        System.out.println("YOUR HAND!!!" + hand);
        blackCard();
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(hand.size() + "Your Hand: " + hand.get(i));
        }
        chooseCardInHand();
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

    public int chooseCardInHand() throws IOException { //////// Choose card number from available cards

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

    public void setScore() {
        if (client.getMessageFromServer().contains(">score")) {
            score = score + 1;
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
