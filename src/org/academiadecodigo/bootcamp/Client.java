package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by codecadet on 04/03/17.
 */
public class Client {
    private String name;
    private List<String> hand;
    private List<String> table;
    private Game game;
    private boolean czar = false;
    private int score = 0;
    private BufferedWriter outCards;
    private Socket playerSocket;
    private BufferedReader inCards;
    private String tenCards;
    private String oneCard;

    public Client() {

        hand = new ArrayList<>();
        table = new ArrayList<>();
        start();
    }

    public void start() {
        try {
            playerSocket = new Socket("localhost", 9090);
            Thread thread = new Thread(new Client.ServerListener());
            Player player = new Player();
            thread.start();
            outCards = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public String parserIn() {
        return null;
    }

    public String parserOut(){
        return null;
    }

    public String getTenCards() {
        return tenCards;
    }

    public String getOneCard() {
        return oneCard;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ServerListener implements Runnable {
        public ServerListener() throws IOException {
            inCards = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
        }

        @Override
        public void run() {
            getinCards();

        }

        public String getinCards() {
            oneCard = null;
            try {
                while ((oneCard = inCards.readLine()) != null && !oneCard.isEmpty()) {
                    oneCard = oneCard + "\n";
                    System.out.println(oneCard);
                    System.out.println("Write Message: ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return oneCard;
        }


    }

}
