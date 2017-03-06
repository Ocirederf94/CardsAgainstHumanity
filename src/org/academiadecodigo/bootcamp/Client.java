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
    private String messageFromServer = null;

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
        System.out.println("Sending Message: " + messeageToSend);
        outMessage.println(messeageToSend);
        outMessage.flush();
    }


    public String getMessageFromServer() {
        messageFromServer = null;
        String result = null;
        try {
            //while ((messageFromServer = in.readLine()) != null) {

               result = in.readLine();

                    //System.out.println(result);
                    return result;

                //result += messageFromServer;
               // System.out.println("XALANA ISTO É O QUE TA A CHEGAR: " + messageFromServer);




         //  }
           // System.out.println("saiu");
           // System.out.println(result);
           // return messageFromServer.toLowerCase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("I am in get message: " + messageFromServer);
        return null;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class ServerListener implements Runnable {
        public ServerListener() throws IOException {
            // System.out.println("Current Thread in Client Listener: " + Thread.currentThread().getName());
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
        }

        @Override
        public void run() {


        }
    }

    public String getMessage() {
        return messageFromServer;
     //client.geMessage();
    }


}
