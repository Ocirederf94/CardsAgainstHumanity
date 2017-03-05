package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
    private String messageFromServer;
    private Player player;

    public Client() {

        hand = new ArrayList<>();
        table = new ArrayList<>();
        start();
    }


    public void start() {
        try {
            playerSocket = new Socket("localhost", 9090);
            Thread thread = new Thread(new Client.ServerListener());
            player = new Player();
            thread.start();
            outCards = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            parserOut();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parserOut() throws IOException {
        outCards.write(player.getCardToClient());
        outCards.write("> choice "  +   player.getWinningCard());
    }

    public String getTenCards() {
        return tenCards;
    }

    public String getMessageFromServer() {
        return messageFromServer;
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
            messageFromServer = null;
            try {
                while ((messageFromServer = inCards.readLine()) != null && !messageFromServer.isEmpty()) {
                    messageFromServer = messageFromServer + "\n";
                    System.out.println(messageFromServer);
                    System.out.println("Write Message: ");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return messageFromServer;
        }
    }

}
