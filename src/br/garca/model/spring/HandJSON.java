package br.garca.model.spring;

import java.util.List;

public class HandJSON {

    private int playerId;
    private List<CardJSON> cards;
    private int quantity;

    public List<CardJSON> getCards() {
        return cards;
    }

    public void setCards(List<CardJSON> cards) {
        this.cards = cards;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
