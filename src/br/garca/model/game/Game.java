package br.garca.model.game;

import java.util.*;
import java.util.stream.IntStream;

public class Game {

    private  List<Player> players;
    private  Stack<Card> pack;
    private int gameNumber;

    private Round round;
    private Player firstPlayer;

    private Game(List<Player> players) {
        this.players = players;
        this.pack = CardFactory.createPack();

        players.forEach(p -> p.setCurrentGame(this));
        gameNumber = 0;
    }

    public void startRound() {
        shufflePack();
        distributeCards();
    }

    private void distributeCards() {
        firstPlayer = players.get(gameNumber % players.size());
        Player player = firstPlayer;
        
        int handNum = getHandQuantity();

        int playerIndex = 0;
        ArrayList<Player> roundPlayers = new ArrayList<>();
        do {
            Set<Card> hand = new HashSet<>();

            IntStream.range(0, handNum).forEach(i -> hand.add(pack.pop()));
            player.setHand(hand);

            player = players.get(++playerIndex % players.size());
            roundPlayers.add(player);
        } while (! player.equals(firstPlayer));

        Card vira = pack.pop();

        round = new Round(roundPlayers, vira);
    }

    private int getHandQuantity() {
        int max = pack.size() / players.size();
        return (gameNumber + 1) % max;
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

    public void play(Card card) {
        round.play(card);
    }

    public Player getFirstPlayer() {
        return firstPlayer;
    }

    public Player getCurrentPlayer() {
        if (round != null) {
            return round.getCurrentPlayer();
        }

        return null;
    }

    public boolean isBetting() {
        if (round != null) {
            return !round.isReady();
        }

        return false;
    }

    public Card getVira() {
        if (round != null) {
            return round.getVira();
        }

        return null;
    }

    public void bet(int bet) {
        if (round == null) {
            throw new GameException("Cannot bet now");
        }

        round.bet(bet);
    }

    public boolean isTurnFinished() {
        return round.isTurnFinished();
    }

    public void nextTurn() {
        if (round != null) {
            round.startNewTurn();
        }
    }
}
