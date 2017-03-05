package org.academiadecodigo.bootcamp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 01/03/17.
 */
public class Server {

    private ConcurrentHashMap<Socket, String> mapOfPlayersSockets;
    private ConcurrentHashMap<Socket, String> tableOfCzarCards;
    private ConcurrentHashMap<Socket, String> winCard;

    public ConcurrentHashMap<Socket, String> getWinCard() {
        return winCard;
    }

    public ConcurrentHashMap<Socket, String> getMapOfPlayersSockets() {
        return mapOfPlayersSockets;
    }

    public ConcurrentHashMap<Socket, String> getTableOfCzarCards() {
        return tableOfCzarCards;
    }

    int counter = 1;


    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }

    public void start() {
        Socket clientSocket;
        int portNumber = 9090;
        clientSocket = null;
        ServerSocket serverSocket = null;
        mapOfPlayersSockets = new ConcurrentHashMap();
        winCard = new ConcurrentHashMap();
        tableOfCzarCards = new ConcurrentHashMap();

//creates the sockets used and the thread clientHandler, puts the client sockets in a hashmap
        try {
            serverSocket = new ServerSocket(portNumber);
            System.out.println("Server started and binded to port: " + portNumber);

            while (mapOfPlayersSockets.size() < 5) {

                clientSocket = serverSocket.accept();
                System.out.println("Server accepted a client with socket: " + clientSocket.toString());
                Thread client = new Thread(new ClientHandler(clientSocket));
                System.out.println("Server started a new thread (" + client.toString() +") a client with socket: " + clientSocket.toString());
                client.start(); // What is this?
                mapOfPlayersSockets.put(clientSocket, "player" + counter);
                System.out.println("Server added a new player (player" + counter +") to the map of socket clients with socket: " + clientSocket.toString());
                counter++;
                System.out.println("Key set list. " + mapOfPlayersSockets.keySet()+ "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Removes the closed sockets from the hashmap
    public void checkConnection() {
        Iterator<Socket> iterator = mapOfPlayersSockets.keySet().iterator();
        synchronized (mapOfPlayersSockets) {
            while (iterator.hasNext()) {
                Socket socket = iterator.next();
                if (socket.isClosed()) {
                    mapOfPlayersSockets.remove(socket);
                }
            }
        }
    }

    //finds one specific player socket
    public Socket findPlayer(String stringValue) {
        synchronized (mapOfPlayersSockets) {
            Iterator<Socket> it = mapOfPlayersSockets.keySet().iterator();
            Socket socket = null;  //?

            while (it.hasNext()) {
                Socket current = it.next();

                if (mapOfPlayersSockets.get(current).equals(stringValue)) { // removed to lowercase
                    socket = current;
                    System.out.println("Sending a message to socket: " + current);
                    break;
                }
            }
            return socket; // returns the socket of the found player?
        }
    }

    //sends a message to a specific player
    public void sendToPlayer(String message, String stringValue) {

        PrintWriter out = null;

        try {
            out = new PrintWriter(findPlayer(stringValue).getOutputStream(), true); // searches the player socket by its name on the sockets map
            out.println(message);
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //sends chat messages to all players
    public void chatSendToAll(Socket msgSocket, String string) {
        synchronized (mapOfPlayersSockets) {
            PrintWriter out = null;
            Iterator<Socket> it = mapOfPlayersSockets.keySet().iterator();
            while (it.hasNext()) {
                if (!msgSocket.isClosed()) {
                    try {
                        Socket tmp = it.next();
                        if (tmp != msgSocket) {
                            out = new PrintWriter(tmp.getOutputStream(), true);
                            out.println(mapOfPlayersSockets.get(msgSocket) + ": " + string);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //sends game message to all players
    public void sendToAll(String message) {
        synchronized (mapOfPlayersSockets) {
            PrintWriter out;
            Iterator<Socket> it = mapOfPlayersSockets.keySet().iterator();
            while (it.hasNext()) {
                Socket tmp = it.next();
                if (!tmp.isClosed()) {

                    try {
                        out = new PrintWriter(tmp.getOutputStream(), true);
                        out.println(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    // closes a client socket
    public void closeClient(Socket msgSocket, String string) {
        if (string == null) {
            try {
                msgSocket.close();
                checkConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //implements methods used by chat commands
    public boolean parser(Socket msgSocket, String string) { // The message (string) received from this socket is parsed
        String winningCard = "";
        boolean sendToAll = false;


        synchronized (mapOfPlayersSockets) {
            String[] parts = string.split(" ");

            switch (parts[0]) {

                case ("/exit"): // the client wants to exit the chat. The connection is closed exits the game also
                    try {
                        msgSocket.close();
                        checkConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case "<@:": // Sends chat message to a specific player
                    if (parts.length > 2) {
                        String playerName = parts[1];
                        String message = "";
                        for (int i = 2; i < parts.length; i++) {
                            message += parts[i] + " ";
                        }
                        sendToPlayer(message, playerName);
                        sendToAll = true;
                    }
                    break;

                case "/setAlias":  // This should be: /setAlias to be consistent with the /exit command
                    if (parts.length > 1) {
                        String playerAlias = "";
                        for (int i = 1; i < parts.length; i++) {
                            playerAlias += parts[i];
                        }

                        mapOfPlayersSockets.put(msgSocket, playerAlias.toLowerCase()); // Change the name of the player to his alias
                    }
                    break;

                case ">Table": // Game command to indicate that it is the table /group of card) of the czar
                    if (parts.length > 2) {
                        String whiteCard = "";
                        for (int i = 0; i < parts.length; i++) {
                            whiteCard += parts[i];
                        }
                        tableOfCzarCards.put(msgSocket, whiteCard); // this message (submitted card)is put into the czarTable map with
                                                                    // a socket key and message value from the player that send it
                        synchronized (tableOfCzarCards) {
                            if (tableOfCzarCards.size() == 4) {
                                tableOfCzarCards.notifyAll();
                            }
                        }

                    }
                    sendToAll = true;
                    break;

                case ">winnerCard":// adds the winning card to the wincard Map
                    if (parts.length > 2) {
                        for (int i = 0; i < parts.length; i++) {
                            winningCard += parts[i];
                        }
                        tableOfCzarCards.remove(msgSocket, winningCard); // removes the winning card and socket from the table of czar map.

                        winCard.put(msgSocket, winningCard); // the removed card and socket is placed in the winning card map.

                    }
                    break;
            }
        }
        return sendToAll;
    }

    //clientHandler thread
    public class ClientHandler implements Runnable {
        private Socket clientSocket;

        private ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        //run override AKA what the new thread does
        @Override
        public void run() {

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg;

                while (true) {
                    msg = in.readLine();
                    closeClient(clientSocket, msg);
                    boolean sendToAll = parser(clientSocket, msg);
                    checkConnection();
                    if (!clientSocket.isClosed()) {
                        System.out.println(msg);
                        //receives the message from the server and decides what to do with ir
                        if (sendToAll) {
                            continue;
                        }
                        chatSendToAll(clientSocket, msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




