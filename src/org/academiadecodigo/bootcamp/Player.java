package org.academiadecodigo.bootcamp;


import javax.smartcardio.Card;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by codecadet on 01/03/17.
 */
public class Player {


    private String name;
    private List<Card> hand;
    private Game game;
    private boolean czar = false;
    private Scanner scanner;
    private int score = 0;
    private int playedCard; // verificar se pode ser assim;

    public Player(String name) {
        this.name = name;
        hand = new LinkedList<>();

    }

    public void addCards(List<Card> hand) {
        this.hand.addAll(hand);
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public boolean isCzar() {
        return czar;

    }

    /*public int play() {
        if (!isCzar()) {
            return chooseCard();
        } else {
            return chooseWinningCard();
        }

    }*/

    /*private int chooseWinningCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose card");
        int choice = scanner.nextInt();
        if (!(choice > table.size())) {
            //TODO logic;
        }
        System.out.println("thats not a valid choice");
        return chooseWinningCard();
    }*/

    public int getPlayedCard() {
        return playedCard;
    }

    public int chooseCard() {
        Scanner scanner = new Scanner(System.in);
        System.out.println(" choose card: ");
        int choice = scanner.nextInt();
        if (!(choice > hand.size())) {
            System.out.println("You choosed card: " + choice);
            playedCard = choice;
            hand.remove(choice);
            return choice;
        }
        System.out.println("thats not a valid choice");
        return chooseCard();
    }
}
