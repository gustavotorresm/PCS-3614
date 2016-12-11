package br.garca.model.spring.response;

import br.garca.model.game.Game;
import br.garca.model.spring.GameJSON;

public class GameResponse extends ApiResponse {

    private GameJSON game;

    public GameJSON getGame() {
        return game;
    }

    public void setGame(GameJSON game) {
        this.game = game;
    }
}
