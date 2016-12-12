package br.garca.model.spring.response;

import br.garca.model.spring.BetJSON;
import br.garca.model.spring.response.ApiResponse;

import java.util.List;

public class BetsResponse extends ApiResponse {

    private List<BetJSON> bets;
    private int currentPlayer;

    public List<BetJSON> getBets() {
        return bets;
    }

    public void setBets(List<BetJSON> bets) {
        this.bets = bets;
    }

    public int getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
}
