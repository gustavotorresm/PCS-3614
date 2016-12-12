package br.garca.model.game;

import java.util.HashSet;
import java.util.Set;

public class Player {

    private Set<Card> hand = new HashSet<>();
    private Game currentGame;
    private String name;
    private int id;

    public void setHand(Set<Card> hand) {
        this.hand = hand;
    }

    public Set<Card> getHand() {
        return hand;
    }

    public void setCurrentGame(Game currentGame) {
        this.currentGame = currentGame;
    }

    public void play(Card card) {
        if (!hand.contains(card)) {
            throw new GameException("Card not present in player's hand");
        }

        hand.remove(card);
        currentGame.play(card);
    }

    public Player(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean equals(Player player) {
        return player.getId() == id;
    }
}
