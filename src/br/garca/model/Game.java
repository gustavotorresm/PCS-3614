package br.garca.model;

import java.util.*;
import java.util.stream.IntStream;

public class Game {

    private  List<Player> players;
    private  Stack<Card> pack;
    private int round;

    private Game(List<Player> players) {
        this.players = players;
        this.pack = CardFactory.createPack();

        round = 0;
    }

    public void startRound() {
        shufflePack();
        distributeCards();
    }

    private void distributeCards() {
        Player firstPlayer = players.get(round % players.size());
        Player player = firstPlayer;

        int playerIndex = 0;
        do {
            Set<Card> hand = new HashSet<>();

            IntStream.range(0, 3).forEach(i -> hand.add(pack.pop()));
            player.setHand(hand);

            player = players.get(++playerIndex % players.size());
        } while (! player.equals(firstPlayer));
    }

    private void shufflePack() {
        Collections.shuffle(pack);
    }

    public static Game setUp(List<Player> players) {
        if (players.size() < 2 || players.size() > 8) {
            throw new GameException("O jogo deve conter entre 2 a 8 jogadores");
        }

        return new Game(players);
    }
}
