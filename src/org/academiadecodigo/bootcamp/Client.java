package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by codecadet on 04/03/17.
 */
public class Client {
    private PrintWriter outMessage;
    private Socket playerSocket;
    private BufferedReader in;
    private String message;


    public Client() {
    }

    public void start() throws IOException {
        playerSocket = new Socket("localhost", 9090);
        Thread thread = new Thread(new ServerListener());
        thread.start();
        outMessage = new PrintWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
      /*  while (true) {
            message();
        }*/

    }

    public void message() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Message:");
        message = scanner.nextLine();
        outMessage.println(message);
        outMessage.flush();
    }


    public void writeMessage(String messeageToSend) {
            outMessage.println(messeageToSend);
            outMessage.flush();
    }


    public String getMessageFromServer() {
        String messageFromServer = null;
        try {
            while ((messageFromServer = in.readLine()) != null || in.readLine().isEmpty()) {

                messageFromServer = messageFromServer + "\n";
               // System.out.println(messageFromServer);
                return messageFromServer;

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("I am in get message: " + messageFromServer);
        return null;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ServerListener implements Runnable {
        public ServerListener() throws IOException {
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
        }

        @Override
        public void run() {

            while (true) {
                getMessageFromServer();
            }
        }
    }
}
