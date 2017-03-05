package org.academiadecodigo.bootcamp;

import java.io.IOException;

/**
 * Created by codecadet on 05/03/17.
 */
public class ClientMain {
    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
