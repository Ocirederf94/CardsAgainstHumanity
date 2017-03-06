package org.academiadecodigo.bootcamp;

import org.omg.CORBA.PRIVATE_MEMBER;

import java.net.Socket;
import java.util.Iterator;

/**
 * Created by codecadet on 02/03/17.
 */
public class Game {
    private WhiteDeck whiteDeck;
    private BlackDeck blackDeck;
    private Server server;
    int gameOver;
    private int gameRound;


    public void start(int winsAt) {
        whiteDeck = new WhiteDeck();
        whiteDeck.makeDeck();
        blackDeck = new BlackDeck();
        blackDeck.makeDeck();
        gameOver = winsAt;
        server = new Server();
        server.start();

    }

    //metodos q vao acontecer durante a ronda(nao sei se os thread.sleeps funcionam!) FALTA
// METER A TRABALHAR PARA VER SE PERCEBO COMO AS RESOLVER *** NAO VAI COMPILAR, ESTOU tramado com F
    private void startRound() {
        gameRound++;
        String whoIsTheCzar = "";

        if (server.getMapOfPlayersSockets().size() == 5) {

            //escolhe o czar, cada vez q passa faz it.next e muda para o proximo
            Iterator<String> it = server.getMapOfPlayersSockets().values().iterator();

            //envia mensagem para o player q vai ser o czar
            //TODO o cliente(FRED) tem de lidar com esta string

            // the player should receive a Yes or no, after the keyword for him to set its boolean
            whoIsTheCzar = it.next();
            server.sendToPlayer(">Czar ", whoIsTheCzar); // who is the czar is the socket of the czar

            //manda a todos uma carta preta
            server.sendToAll(">black " + (blackDeck.giveBlackCard(1)));

            //TODO os clientes têm de apagar os comandos, fred!


            //manda 10 cartas brancas para todos
            for (Socket player : server.getMapOfPlayersSockets().keySet()) {
                if (gameRound ==1){
                    // for (int i = 0; i <10; i++) {
                        server.sendToPlayer(">white " + (whiteDeck.giveCard(10)), server.getMapOfPlayersSockets().get(player));
                    }
                //}
            }

            //manda mais uma carta branca a cada um (a cada ronda, tb a contar com a primeira
            for (Socket player : server.getMapOfPlayersSockets().keySet()) {
                if (gameRound > 1) {
                    server.sendToPlayer(">white " + (whiteDeck.giveCard(1)), server.getMapOfPlayersSockets().get(player));
                }
            }

            //TODO os clientes têm de apagar os comandos, fred!

            //esperar que todos os jogadores escolham a carta
            //nullpointer
            synchronized (server.getTableOfCzarCards()) {
                while (server.getTableOfCzarCards().size() != 4) {
                    try {
                        server.getTableOfCzarCards().wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            //ver as escolhas dos jogadores
            for (String values : server.getMapOfPlayersSockets().values()) {
                server.sendToAll(">white " + (server.getMapOfPlayersSockets().values()));
            }

            //enviar as escolhas de cada jogador para o czar
            server.sendToPlayer(">white " + server.getTableOfCzarCards().values(), whoIsTheCzar);

            //esperar que o czar faça a escolha
            //qnd o czar escolhe guardo essa carta, retiro todas da mesa e o jogo avança

 // Working on it

            while (server.getTableOfCzarCards().size() != 3) {
                server.sendToPlayer(">winnerCard ", whoIsTheCzar);
            }


            //TODO atribuir um ponto ao vencedor: papel e caneta acho q tenho de pegar na carta escolhida,
            //TODO ver de quem é e aumentar um ponto ao dono dela,-> solve that and... profit!!!
            server.sendToAll(">checkScore ");
            if(server.score!= gameOver){
                server.sendToAll("" + server.score);

            }
            if(server.score != gameOver){
                startRound();
            }

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
    esperar que todos os jogadores escolham uma carta, --MIGHT BE DONE
    ----ver as escolhas de todos os jogadores
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
        g.start(1);
        g.startRound();
    }

}
