package org.academiadecodigo.bootcamp;


import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by codecadet on 01/03/17.
 */
public class Player {


    private String name;
    private List<String> hand;
    private List<String> table;
    private Game game;
    private boolean czar = false;
    private int score = 0;
    private BufferedWriter outCards;
    private Socket playerSocket;
    private BufferedReader inCards;
    private String card;


    public Player() {
        this.name = name;
        hand = new ArrayList<>();
        table = new ArrayList<>();
        play();
    }

   /* public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose User: ");
        name = scanner.nextLine();
        play();
    }*/

    public void addCard(String card) {
        this.card = card;
        this.hand.add(card);

    }


    public boolean isCzar() {
        return czar;

    }

    public void play() {
        for (int i = 0; i < 10; i++) {
            addCard(card);
        }

        while (true) {
            if (!isCzar()) { ////// server will choose czar and creat a get method of the boolean
                try {
                    getPlayedCard();
                    hand.remove(getPlayedCard());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                addCard(card);

            } else {
                chooseWinningCard();
                for (int i = 0; i < table.size(); i++) {
                    table.remove(i);
                }

            }
        }
    }

    private String chooseWinningCard() {
        Scanner scanner = new Scanner(System.in);///////Czar chooses winning card
        System.out.println("Choose card");
        int choice = scanner.nextInt();
        if (choice <= table.size()) {
            for (int i = 0; i < table.size(); i++) {
                System.out.println("The winning card is: " + table.get(i));
                return table.get(i);
            }
        }
        System.out.println("Thats not a valid choice");
        return chooseWinningCard();
    }

    public String getPlayedCard() throws IOException {///Choose the white card from hand
        for (int i = 0; i < hand.size(); i++) {
            if (i == chooseCardInHand()) {
                System.out.println("You choose the card: " + hand.get(i));
                return hand.get(i);
                ////Sending car to tabel deck;
            }
        }
        System.out.println("Can't find the card!!");
        return getPlayedCard();
    }

    public int chooseCardInHand() throws IOException { ////////Choose number of available cards
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


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

}
