package org.academiadecodigo.bootcamp;


import javax.smartcardio.Card;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
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


    public Player() {
        this.name = name;
        hand = new ArrayList<>();
        table = new ArrayList<>();
        start();
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose User: ");
        name = scanner.nextLine();

        try {
            playerSocket = new Socket("localhost", 9090);
            Thread thread = new Thread(new ServerListener());
            thread.start();
            outCards = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void addCard() {

        try {
            this.hand.add(incomeCard());
        } catch (IOException e) {
            e.printStackTrace();
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
                    getPlayedCard();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                addCard();

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
                System.out.println("The winning card is: " + table.get(i).toString());
                return table.get(i);
            }
        }
        System.out.println("Thats not a valid choice");
        return chooseWinningCard();
    }

    public String getPlayedCard() throws IOException {///Choose the white card from hand
        for (int i = 0; i < hand.size(); i++) {
            if (i == chooseCardInDeck()) {
                System.out.println("You choose the card: " + hand.get(i).toString());
                outCards.write(hand.get(i)); ////Sending car to tabel deck;
                return hand.get(i);
            }
        }
        System.out.println("Can't find the card!!");
        return null;
    }

    public int chooseCardInDeck() throws IOException { ////////Choose number of available cards
        Scanner scanner = new Scanner(System.in);
        System.out.println("Chose card: ");
        int choice = scanner.nextInt();
        if (choice <= hand.size()) {
            System.out.println("You choose the number: " + choice);
            return choice;
        }
        System.out.println("That's not a valid choice");
        return chooseCardInDeck();
    }

    public String incomeCard() throws IOException {
        String line = null;

        try {
            System.out.println(inCards);
            while ((line = inCards.readLine()) != null && !line.isEmpty()) {
                line = line + "\n";
                System.out.println(line);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ServerListener implements Runnable {
        public ServerListener() throws IOException {
            inCards = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
        }

        @Override
        public void run() {
            try {
                incomeCard();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public static void main(String[] args) {
        Player player = new Player();
        player.play();

    }
}
