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

    private Integer winner;
    private Integer currentPlayer;

    private boolean betting;
    private boolean roundFinished;
    private boolean turnFinished;

    private Map<Integer, Integer> score;


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

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }

    public Integer getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Integer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public boolean isBetting() {
        return betting;
    }

    public void setBetting(boolean betting) {
        this.betting = betting;
    }

    public Map<Integer, Integer> getScore() {
        return score;
    }

    public void setScore(Map<Integer, Integer> score) {
        this.score = score;
    }

    public boolean isRoundFinished() {
        return roundFinished;
    }

    public void setRoundFinished(boolean roundFinished) {
        this.roundFinished = roundFinished;
    }

    public boolean isTurnFinished() {
        return turnFinished;
    }

    public void setTurnFinished(boolean turnFinished) {
        this.turnFinished = turnFinished;
    }
}
