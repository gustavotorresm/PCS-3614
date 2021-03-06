package br.garca.model.game;

import com.google.common.collect.HashBiMap;

import java.util.*;
import java.util.stream.Collectors;

public class Round {

    private HashMap<Card, Player> currentGame;

    private Set<Card> discardPile = new HashSet<>();

    private List<Player> players;
    private Player winner = null;

    private Card vira;

    private HashMap<Player, Integer> bets = new HashMap<>();
    private HashMap<Player, Integer> score = new HashMap<>();
    private HashMap<Player, Integer> differenceMap;

    private final int TURNS_NUMBER;
    private int firstPlayerIndex;
    private boolean ready = false;

    private int currentPlayerOffset;
    private boolean turnFinished = false;
    private boolean roundFinished = false;
    private int turn = 0;

    public Round(ArrayList<Player> players, Card vira) {
        this.players = players;
        this.vira = vira;

        currentGame = new HashMap<>();

        firstPlayerIndex = 0;
        currentPlayerOffset = 0;

        TURNS_NUMBER = players.get(0).getHand().size();
    }

    public Player getCurrentPlayer() {
        return players.get((firstPlayerIndex + currentPlayerOffset) % players.size());
    }

    public boolean isReady() {
        return ready;
    }

    public Card getVira() {
        return vira;
    }

    public void bet(int bet) {
        Player player = getCurrentPlayer();
        bet(player, bet);
    }

    private void bet(Player player, int bet) {
        if (ready) throw new GameException("Cannot bet now");

        // Last player
        if (currentPlayerOffset == players.size() - 1) {
            int betsSum = bets.values().parallelStream().collect(Collectors.summingInt(b -> b == null ? 0 : b));
            if (bet + betsSum == TURNS_NUMBER) {
                throw new GameException("Bets must not sum as the amount of players", Code.INVALID_BET);
            }
        }

        bets.put(player, bet);

        if (players.size() == ++currentPlayerOffset) {
            ready = true;
            turnFinished = true;
        }
    }

    public void startNewTurn() {
        if (!roundFinished && turn == TURNS_NUMBER) {
            finishRound();
            return;
        }

        if (!(turnFinished && ready) || roundFinished) {
            throw new GameException("Cannot start a new turn");
        }

        if (winner != null) {
            firstPlayerIndex = players.indexOf(winner);
        }
        turnFinished = false;
        currentPlayerOffset = 0;

        for (Iterator<Card> it = currentGame.keySet().iterator(); it.hasNext();) {
            Card card = it.next();
            discardPile.add(card);
        }

        currentGame.clear();
    }

    public boolean isTurnFinished() {
        return turnFinished;
    }

    public void play(Card card) {
        if (!ready) throw new GameException("Cannot play the card");

        Player currentPlayer = getCurrentPlayer();
        if (!currentPlayer.getHand().contains(card)) {
            throw new GameException("Cannot play the card");
        }

        currentPlayer.getHand().remove(card);

        currentGame.put(card, currentPlayer);

        if (players.size() == ++currentPlayerOffset) {
            finishTurn();
        }
    }

    private void finishTurn() {
        turnFinished = true;
        turn++;

        Card highest = getHighestCard();
        if (highest != null) {
            winner = currentGame.get(highest);
            int playerScore = score.getOrDefault(winner, 0);
            score.put(winner, playerScore + 1);
        }
    }

    private void finishRound() {
        roundFinished = true;

        discardPile.add(vira);

        differenceMap = new HashMap<>();

        players.forEach(
                p -> {
                    int difference = Math.abs(score.getOrDefault(p, 0) - bets.get(p));
                    differenceMap.put(p, difference);
                }
        );
    }

    private Card getHighestCard() {
        List<Card> cards = new LinkedList<>(currentGame.keySet());
        Collections.sort(cards, new CardComparator(vira));
        Collections.reverse(cards);

        ListIterator<Card> it = cards.listIterator();
        while (it.hasNext()) {
            Card card = it.next();

            if (CardComparator.isManilha(card, vira)) {
                return card;
            }

            Card next = it.next();
            if (card.getNumber() != next.getNumber()) {
                return card;
            }

            while (it.hasNext()) {
                if (card.getNumber() != it.next().getNumber()) {
                    it.previous();
                    break;
                }
            }
        }

        return null;
    }

    public Map<Player,Set<Card>> getVisibleHands(Player player) {
        Map<Player, Set<Card>> hands = new HashMap<>();

        players.forEach(
                p -> {
                    if (player.equals(p)) {
                        if (TURNS_NUMBER != 1) {
                            hands.put(p, p.getHand());
                        }  else {
                            hands.put(p, new HashSet<>());
                        }
                    } else {
                        if (TURNS_NUMBER == 1) {
                            hands.put(p, p.getHand());
                        } else {
                            hands.put(p, new HashSet<>());
                        }
                    }
                }
        );

        return hands;
    }

    public Map<Player,Card> getPlayedCards() {
        return HashBiMap.create(currentGame).inverse();
    }

    public HashMap<Player,Integer> getBets() {
        return bets;
    }

    public Player getWinner() {
        if (turnFinished) return winner;

        return null;
    }

    public Set<Card> getDiscardPile() {
        return discardPile;
    }

    public HashMap<Player,Integer> getScore() {
        return score;
    }

    public boolean isFinished() {
        return TURNS_NUMBER == turn;
    }
}
