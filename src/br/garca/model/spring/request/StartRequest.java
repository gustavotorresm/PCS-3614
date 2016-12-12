package br.garca.model.spring.request;

import br.garca.model.game.Player;
import br.garca.model.spring.PlayerJSON;
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

