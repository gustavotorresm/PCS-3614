package br.garca.model.spring.response;

import br.garca.model.spring.CardJSON;
import br.garca.model.spring.HandJSON;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class OverviewResponse extends ApiResponse {

    private List<HandJSON> hands;
    private CardJSON vira;
    private Map<Integer, CardJSON> playedCards;


    public List<HandJSON> getHands() {
        return hands;
    }

    public void setHands(List<HandJSON> hands) {
        this.hands = hands;
    }

    public CardJSON getVira() {
        return vira;
    }

    public void setVira(CardJSON vira) {
        this.vira = vira;
    }

    public Map<Integer, CardJSON> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(Map<Integer, CardJSON> playedCards) {
        this.playedCards = playedCards;
    }
}
