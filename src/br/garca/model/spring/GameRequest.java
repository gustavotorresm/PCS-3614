package br.garca.model.spring;

import br.garca.model.game.Player;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameRequest {

    private Action action;

    private List<PlayerJSON> players;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<PlayerJSON> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerJSON> players) {
        this.players = players;
    }
}

