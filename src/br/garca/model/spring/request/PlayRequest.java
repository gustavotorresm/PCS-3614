package br.garca.model.spring.request;

import br.garca.model.spring.CardJSON;

public class PlayRequest {

    private CardJSON card;
    private int playerId;

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public CardJSON getCard() {
        return card;
    }

    public void setCard(CardJSON card) {
        this.card = card;
    }
}
