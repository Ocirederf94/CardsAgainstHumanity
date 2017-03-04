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
    //TODO Qnd se fizer o jar é suposto saber se quem inicia vai ser jogador ou servidor

    private ConcurrentHashMap<Socket, String> list;
    private Socket clientSocket;
    int counter = 1;
    boolean isCzar;

    public static void main(String[] args) {
        Server server = new Server();
        server.start();
        //server.startGame();
    }

    private void start() {

        int portNumber = 9090;
        clientSocket = null;
        ServerSocket serverSocket = null;
        list = new ConcurrentHashMap();

//creates the sockets used and the thread clientHandler, puts the client sockets in a hashmap
        try {
            serverSocket = new ServerSocket(portNumber);

            while (true) {

                clientSocket = serverSocket.accept();
                Thread client = new Thread(new ClientHandler(clientSocket));
                client.start();
                list.put(clientSocket, "player" + counter);
                counter++;
                //System.out.println(list.keySet());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    // starts the game
   /* private void startGame() {
        if (list.size() == 5) {
            Game game = new Game();
            game.start();
        }
    }*/

    //kind of a garbage collector, removes the closed sockets from the hashmap
    private void checkConnection() {
        Iterator<Socket> iterator = list.keySet().iterator();
        synchronized (list) {
            while (iterator.hasNext()) {
                Socket socket = iterator.next();
                if (socket.isClosed()) {
                    list.remove(socket);
                }
            }
        }
    }

    //finds one specific player socket
    private Socket findPlayer(String stringValue) {
        synchronized (list) {
            Iterator<Socket> it = list.keySet().iterator();
            Socket socket = null;

            while (it.hasNext()) {
                Socket current = it.next();

                if (list.get(current).equals(stringValue.toLowerCase())) {
                    socket = current;
                    break;
                }
            }
            return socket;
        }
    }


    //clientHandler thread
    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        private ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        //sends a message to a specific player
        private void sendToPlayer(String string, String stringValue) {

            PrintWriter out = null;

            try {
                out = new PrintWriter(findPlayer(stringValue).getOutputStream(), true);
                out.println(string);
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        //sends message to all players
        private void sendToAll(String string) {
            synchronized (list) {
                PrintWriter out = null;
                Iterator<Socket> it = list.keySet().iterator();
                while (it.hasNext()) {
                    if (!clientSocket.isClosed()) {

                        try {
                            Socket tmp = it.next();
                            if (tmp != clientSocket)
                                new PrintWriter(tmp.getOutputStream(), true).println(list.get(clientSocket) + ": " + string);


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        // closes a client socket
        private void closeClient(String string) {
            if (string == null) {
                try {
                    clientSocket.close();
                    checkConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //implements methods used by chat commands
        private boolean parser(String string) {

            boolean sendToAll = false;

            synchronized (list) {
                String[] parts = string.split(" ");

                switch (parts[0]) {

                    case ("/exit"):
                        try {
                            clientSocket.close();
                            checkConnection();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case "@:":
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

                            list.put(clientSocket, playerAlias.toLowerCase());
                        }
                        break;

                    case "/white":
                        //TODO implementar carta e referencia
                        sendToAll = true;
                        break;

                    case "/isCzar":
                        //TODO enviar mensagem ao cliente q vai ser o czar
                        sendToAll = true;
                        break;

                    case"/winner":
                        //TODO enviar mensagem a todos quem ganhou o round e a carta
                        break;

                    case"/score":
                        //TODO enviar o score aos players
                        break;

                    case"/black":
                        //TODO enviar uma carta preta
                        break;

                    case"/whoIsCzar":
                        //TODO enviar aos jogadores quem vai ser o czar
                        break;

                }
            }
            return sendToAll;
        }

        //run override AKA what the new thread does
        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg;

                while (true) {
                    msg = in.readLine();
                    closeClient(msg);
                    boolean sendToAll = parser(msg);
                    checkConnection();
                    if (!clientSocket.isClosed()) {
                        System.out.println(msg);
                        //receber os dados do client e decidir o q fazer com eles no metodo implement methods(acima)
                        if (sendToAll) {
                            continue;
                        }
                        sendToAll(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




