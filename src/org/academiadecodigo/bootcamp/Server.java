package org.academiadecodigo.bootcamp;

import com.sun.xml.internal.bind.v2.TODO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.Key;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 01/03/17.
 */
public class Server {

    private ConcurrentHashMap<Socket, String> map;
    private ConcurrentHashMap<Socket, String> table;
    private ConcurrentHashMap<Socket, String> winCard;

    public ConcurrentHashMap<Socket, String> getWinCard() {
        return winCard;
    }

    public ConcurrentHashMap<Socket, String> getMap() {
        return map;
    }

    public ConcurrentHashMap<Socket, String> getTable() {
        return table;
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
        map = new ConcurrentHashMap();
        winCard = new ConcurrentHashMap();
        table = new ConcurrentHashMap();

//creates the sockets used and the thread clientHandler, puts the client sockets in a hashmap
        try {
            serverSocket = new ServerSocket(portNumber);

            while (map.size() < 5) {

                clientSocket = serverSocket.accept();
                Thread client = new Thread(new ClientHandler(clientSocket));
                client.start();
                map.put(clientSocket, "player" + counter);
                counter++;
                //System.out.println(list.keySet());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //kind of a garbage collector, removes the closed sockets from the hashmap
    public void checkConnection() {
        Iterator<Socket> iterator = map.keySet().iterator();
        synchronized (map) {
            while (iterator.hasNext()) {
                Socket socket = iterator.next();
                if (socket.isClosed()) {
                    map.remove(socket);
                }
            }
        }
    }

    //finds one specific player socket
    public Socket findPlayer(String stringValue) {
        synchronized (map) {
            Iterator<Socket> it = map.keySet().iterator();
            Socket socket = null;

            while (it.hasNext()) {
                Socket current = it.next();

                if (map.get(current).equals(stringValue.toLowerCase())) {
                    socket = current;
                    break;
                }
            }
            return socket;
        }
    }

    //sends a message to a specific player
    public void sendToPlayer(String message, String stringValue) {

        PrintWriter out = null;

        try {
            out = new PrintWriter(findPlayer(stringValue).getOutputStream(), true);
            out.println(message);
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //sends message to all players
    public void chatSendToAll(Socket msgSocket, String string) {
        synchronized (map) {
            PrintWriter out = null;
            Iterator<Socket> it = map.keySet().iterator();
            while (it.hasNext()) {
                if (!msgSocket.isClosed()) {
                    try {
                        Socket tmp = it.next();
                        if (tmp != msgSocket) {
                            out = new PrintWriter(tmp.getOutputStream(), true);
                            out.println(map.get(msgSocket) + ": " + string);
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //sends message to all players
    public void sendToAll(String message) {
        synchronized (map) {
            PrintWriter out;
            Iterator<Socket> it = map.keySet().iterator();
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
    public boolean parser(Socket msgSocket, String string) {
        String winningCard = "";
        boolean sendToAll = false;


        synchronized (map) {
            String[] parts = string.split(" ");

            switch (parts[0]) {

                case ("/exit"):
                    try {
                        msgSocket.close();
                        checkConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;

                case "<@:":
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

                case "setAlias":
                    if (parts.length > 1) {
                        String playerAlias = "";
                        for (int i = 1; i < parts.length; i++) {
                            playerAlias += parts[i];
                        }

                        map.put(msgSocket, playerAlias.toLowerCase());
                    }
                    break;

                case ">Table":
                    if (parts.length > 2) {
                        String whiteCard = "";
                        for (int i = 0; i < parts.length; i++) {
                            whiteCard += parts[i];
                        }
                        table.put(msgSocket, whiteCard);
                        synchronized (table) {
                            if (table.size() == 4) {
                                table.notifyAll();
                            }
                        }

                    }
                    sendToAll = true;
                    break;

                case ">winnerCard":
                    if (parts.length > 2) {
                        for (int i = 0; i < parts.length; i++) {
                            winningCard += parts[i];
                        }
                        table.remove(msgSocket, winningCard);

                        winCard.put(msgSocket, winningCard);

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
                        //receber os dados do client e decidir o q fazer com eles no metodo implement methods(acima)
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




