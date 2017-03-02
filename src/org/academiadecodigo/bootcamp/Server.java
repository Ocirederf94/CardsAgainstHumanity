package org.academiadecodigo.bootcamp;

import com.sun.xml.internal.bind.v2.TODO;

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
    private ConcurrentHashMap list;
    private Socket clientSocket;

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

        try {
            serverSocket = new ServerSocket(portNumber);
            int counter = 1;
            while (true) {

                clientSocket = serverSocket.accept();
                Thread client = new Thread(new ClientHandler(clientSocket));
                client.start();
                list.put("player "+ counter , clientSocket);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   /* private void startGame() {
        if (list.size() == 4) {
            Game game = new Game();
            game.start();
        }
    }*/

    private void checkConnection() {
        Iterator<Socket> iterator = list.values().iterator();
        synchronized (list) {
            while (iterator.hasNext()) {
                Socket socket = iterator.next();
                if (socket.isClosed()) {
                    list.remove(socket);
                }
            }
        }
    }

    private void sendToPlayer(String string) {
        synchronized (list) {
            PrintWriter out = null;
            Iterator<Socket> it = list.values().iterator();
            while (it.hasNext()){

            try {
                out = new PrintWriter(it.next().getOutputStream(), true);
                System.out.println(out);
                out.println(string);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        }
    }


    private class ClientHandler implements Runnable {
        private Socket clientSocket;

        private ClientHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        private void implementMethods(String string) {

            if (string == null) {
                try {
                    clientSocket.close();
                    checkConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                String msg;

                while (true) {

                    if (!clientSocket.isClosed()) {

                        msg = in.readLine();
                        implementMethods(msg);
                        System.out.println(msg);
                        //receber os dados do client e decidir o q fazer com eles no metodo implement methods(acima)
                        sendToPlayer(msg);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
