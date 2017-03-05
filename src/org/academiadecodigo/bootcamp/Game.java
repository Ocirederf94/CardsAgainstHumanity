package org.academiadecodigo.bootcamp;

import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 02/03/17.
 */
public class Game {
    private WhiteDeck whiteDeck;
    private BlackDeck czarCard;
    //private ConcurrentHashMap<String, String> whiteDeckList;
    //private ConcurrentHashMap<String, String> blackDeckList;
    private int[] sockets = new int[5];
    private Server server;




   /* public void setDeckLists(ConcurrentHashMap<String, String> whiteDeckList, ConcurrentHashMap<String, String> blackDeckList) {
        this.whiteDeckList = whiteDeckList;
        this.blackDeckList = blackDeckList;
        whiteDeck = new WhiteDeck();
        czarCard = new BlackDeck();
    }*/

    public void start(int winsAt) {
        whiteDeck = new WhiteDeck();
        czarCard = new BlackDeck();

        System.out.println("aqui antes do server");
        server = new Server();
        server.start();


        System.out.println("aqui");


    }



        /*while(player.getScore() != winsAt){
             startRound();
        }
    }*/

    private void startRound() {

        if (server.getList().size() == 5) {
            whiteDeck.makeDeck();
            int counter = 1;
            System.out.println("tou");

            for (Socket player: server.getList().keySet()) {
                server.sendToPlayer((whiteDeck.giveCard(10)), server.getList().get(player));
            }

        }
    }

/*    private void giveCzar() {


    }*/

    private void handPlayer() {

    }


    /*
    -ver se estão 5 jogadores -> metodo do server
    - o Game diz ao servidor qual é o servidor o cliente que é o czar.

    começar o jogo (metodo start):
    ----atribuir um czar,
    -dar cartas aos jogadores,
    -começar ronda:
    ----escolher uma carta preta do deck e enviar para todos,
    ----bloquear a escolha de cartas do czar,
    ----esperar que todos os jogadores escolham uma carta,
    ----ver as escolhas de todos os jogadores
    ----enviar as escolhas de cada jogador para o czar,
    ----esperar que o czar faça uma escolha,
    ----atribuir um ponto ao dono da carta escolhida,
    ----mostrar um score a todos os jogadores,
    ----atribuir uma nova carta a cada jogador,
    ----atribuir o novo czar.
    ----fim do round.
    -novo round até alguem ter um score de x,
    -mostrar a todos quem é o winner.
     */
    public static void main(String[] args) {
        Game g = new Game();
        System.out.println("mas aqui chega");
        g.start(1);
        System.out.println("não deve chegar aqui");
        g.startRound();
    }

}
