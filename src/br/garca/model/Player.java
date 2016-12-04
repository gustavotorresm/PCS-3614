package br.garca.model;

import java.util.HashSet;
import java.util.Set;

public class Player {

    Set<Card> hand = new HashSet<>();

    public void setHand(Set<Card> hand) {
        this.hand = hand;
    }

    public Set<Card> getHand() {
        return hand;
    }
}
