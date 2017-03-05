package org.academiadecodigo.bootcamp;

import com.sun.xml.internal.bind.v2.TODO;

import java.net.Socket;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by codecadet on 02/03/17.
 */
public class Game {
    private WhiteDeck whiteDeck;
    private BlackDeck czarCard;
    private int[] sockets = new int[5];
    private Server server;
    int gameOver;


    public void start(int winsAt) {
        whiteDeck = new WhiteDeck();
        czarCard = new BlackDeck();
        gameOver = winsAt;
        server = new Server();
        server.start();

    }

    private void startRound() {

        if (server.getList().size() == 5) {
            //faz um deck
            whiteDeck.makeDeck();

            //escolhe o czar, cada vez q passa faz it.next e muda para o proximo
            Iterator<String> it = server.getList().values().iterator();
            server.sendToPlayer("<isCzar ", it.next());

            //manda 10 cartas brancas para todos
            for (Socket player : server.getList().keySet()) {
                server.sendToPlayer("<white " + (whiteDeck.giveCard(10)), server.getList().get(player));
            }

            //manda mais uma carta branca a cada um (a cada ronda, tb a contar com a primeira
            for (Socket player : server.getList().keySet()) {
                server.sendToPlayer("<white " + (whiteDeck.giveCard(1)), server.getList().get(player));
            }

            //manda a todos uma carta preta
            server.sendToAll("<black");

            //TODO meter os metodos q faltam

            //if(playerscore(ta mal!!) != gameOver){
            startRound();
        }

        //TODO enviar o winner aos players
    }

    /*
    -ver se estão 5 jogadores -> metodo do server DONE

    começar o jogo (metodo start):DONE
    ----atribuir um czar DONE
    -dar cartas aos jogadores, *ta no round, pareceu me melhor DONE

    -começar ronda:
    ----escolher uma carta preta do deck e enviar para todos,DONE
    ----bloquear a escolha de cartas do czar, ACHO Q TEM DE SER NO PLAYER
    TODO----esperar que todos os jogadores escolham uma carta,
    TODO----ver as escolhas de todos os jogadores
    TODO----enviar as escolhas de cada jogador para o czar,
    TODO----esperar que o czar faça uma escolha,
    TODO----atribuir um ponto ao dono da carta escolhida,
    TODO----mostrar um score a todos os jogadores,


    ----fim do round.DONE
    -novo round até alguem ter um score de x,DONE
    TODO-mostrar a todos quem é o winner.
     */
    public static void main(String[] args) {
        Game g = new Game();
        System.out.println("mas aqui chega");
        g.start(1);
        System.out.println("não deve chegar aqui");
        g.startRound();
    }

}
