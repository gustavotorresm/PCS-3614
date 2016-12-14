package br.garca.model.game;

import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.IntStream;

public class Game {

    private  List<Player> players;
    private  Stack<Card> pack;
    private int gameNumber;

    private Map<Player, Integer> score;

    private Round round;
    private Player firstPlayer;

    private Game(List<Player> players) {
        this.players = players;
        this.pack = CardFactory.createPack();
        this.score = new HashMap<>();

        players.forEach(p -> {
            p.setCurrentGame(this);
            score.put(p, 0);
        });
        gameNumber = -1;
    }

    public void startRound() {
        gameNumber++;

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

    public Map<Player, Set<Card>> getVisibleHands(Player player) {
        return round.getVisibleHands(player);
    }

    public Map<Player,Integer> getCardsNumber() {
        Map<Player, Integer> cardsNumber = new HashMap<>();

        players.forEach(
                p -> cardsNumber.put(p, p.getHand().size())
        );

        return cardsNumber;
    }

    public void finishRound() {
        pack.addAll(round.getDiscardPile());

        Map<Player, Integer> roundScore = round.getScore();
        roundScore.entrySet().forEach( e -> {
            int current = score.get(e.getKey());
            score.put(e.getKey(), current + e.getValue());
        });
    }

    public Map<Player,Card> getPlayedCards() {
        return round.getPlayedCards();
    }

    public Map<Player,Integer> getBets() {
        Map bets = round.getBets();

        players.forEach(
                p -> {
                    if (! bets.containsKey(p)) {
                        bets.put(p, null);
                    }
                }
        );

        return  bets;
    }

    public Player getWinner() {
        return round.getWinner();
    }

    public void play(Player player) {
        if (player.getHand().size() == 1) {
            Card card = player.getHand().stream().findAny().get();
            round.play(card);
        } else {
            throw new GameException("YOU MUST CHOOSE A CARD");
        }
    }

    public Map<Player,Integer> getScore() {
        return score;
    }

    public boolean isRoundFinished() {
        return round.isFinished();
    }
}
