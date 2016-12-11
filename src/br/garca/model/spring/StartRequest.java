package br.garca.model.spring;

import br.garca.model.game.Player;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StartRequest {

    private List<PlayerJSON> players;

    public List<PlayerJSON> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerJSON> players) {
        this.players = players;
    }
}

