package org.academiadecodigo.bootcamp;

import java.net.Socket;
import java.util.Iterator;

/**
 * Created by codecadet on 02/03/17.
 */
public class Game {
    private WhiteDeck whiteDeck;
    private BlackDeck blackDeck;
    private int[] sockets = new int[5];
    private Server server;
    int gameOver;

    public void start(int winsAt) {
        whiteDeck = new WhiteDeck();
        whiteDeck.makeDeck();
        blackDeck = new BlackDeck();
        blackDeck.makeDeck();

        gameOver = winsAt;
        server = new Server();
        server.start();

        if(server.getList().size() != 5){
            server.sendToAll("Waiting fo all players...");
        }
        //ve se estao 5 jogadores
        if (server.getList().size() == 5) {
            //da as cartas aos jogadores
            for (Socket player : server.getList().keySet()) {
                server.sendToPlayer((whiteDeck.giveCard(10)), server.getList().get(player));
                startRound();
            }
        }
    }


    private void startRound() {

        //escolhe o czar
        Iterator<String> it = server.getList().values().iterator();
        server.sendToPlayer("<isCzar ", it.next());

        //da uma carta preta a todos
        for (Socket player : server.getList().keySet()) {
            server.sendToPlayer(blackDeck.giveCard(1), server.getList().get(player));
        }

        //TODO faltam metodos

        //dá mais uma carta a todos
        for (Socket player : server.getList().keySet()) {
            server.sendToPlayer((whiteDeck.giveCard(10)), server.getList().get(player));
        }

        /*if(playerScore(some shit like thiz) != gameOver){
            startRound();
        }*/


    }


    /*
    -ver se estão 5 jogadores --DONE
    - o Game diz ao servidor qual é o servidor o cliente que é o czar. //deve ser o q fiz no round --DONE

    começar o jogo (metodo start):  --DONE
    ----atribuir um czar,  //deve ser o que fiz no round --DONE
    -dar cartas aos jogadores, --DONE

    -começar ronda:  --DONE
    ----escolher uma carta preta do deck e enviar para todos, --DONE
    ----bloquear a escolha de cartas do czar, //Acho q tem de ser no player!
    TODO----esperar que todos os jogadores escolham uma carta, ///acho q tenho de criar uma tableMap no game e dar lhe as cartas atraves do server, arranca qnd estiver com x cartas
    TODO----ver as escolhas de todos os jogadores ///mostrar as cartas da table
    TODO----enviar as escolhas de cada jogador para o czar, /// enviar para todos as escolhas
    TODO----esperar que o czar faça uma escolha,
    TODO----atribuir um ponto ao dono da carta escolhida, //encontrar a partir da card, onde é q vai estar o score? aposto no game...
    TODO----mostrar um score a todos os jogadores, //mostrar esse score
    ----atribuir uma nova carta a cada jogador, --DONE
    ----atribuir o novo czar. //DA BARRACA, E MAIS FACIL CHEGAR AO FIM E SETAR TODOS OS CZAR PARA FALSE, QND A ROUND RECOMEÇA ESCOLHE O PROXIMO
    ----fim do round. //this -> }
    -novo round até alguem ter um score de x, --ALMOST DONE!!!!
    -mostrar a todos quem é o winner. // fazer server.sendAll o nome do player q ganhou EZ PZ
     */
    public static void main(String[] args) {
        Game g = new Game();
        System.out.println("mas aqui chega");
        g.start(1);
        System.out.println("não deve chegar aqui");
        g.startRound();
    }

}
