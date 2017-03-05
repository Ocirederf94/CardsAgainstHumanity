package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by codecadet on 04/03/17.
 */
public class Client {
    private BufferedWriter out;
    private Socket playerSocket;
    private BufferedReader in;
    private String messageFromServer;


    public Client() {
    }

    public void start() throws IOException {
        playerSocket = new Socket("localhost", 9090);
        Thread thread = new Thread(new ServerListener());
        thread.start();
        /*try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));

    }

    public String message() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Message:");
        String message = scanner.nextLine();
        return message;
    }


    public void writeMessage(String messeageToSend) {

        try {
            out.write(messeageToSend);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public String getMessageFromServer() {
        try {
            while ((messageFromServer = in.readLine()) != null || in.readLine().isEmpty() ) {

                messageFromServer = messageFromServer + "\n";
                System.out.println(messageFromServer);
                System.out.println("Write Message: ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messageFromServer;
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
