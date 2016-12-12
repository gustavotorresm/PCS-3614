package br.garca.model.spring;

import br.garca.model.spring.response.ApiResponse;

public class PlayerResponse extends ApiResponse {

    private PlayerJSON player;

    public PlayerJSON getPlayer() {
        return player;
    }

    public void setPlayer(PlayerJSON player) {
        this.player = player;
    }
}
