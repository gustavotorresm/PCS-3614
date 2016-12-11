package br.garca.controller;

import br.garca.model.game.Game;
import br.garca.model.game.Player;
import br.garca.model.spring.*;
import br.garca.model.spring.response.ApiResponse;
import br.garca.model.spring.response.GameResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.LinkedList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainController {

    private HttpSession session;

    @RequestMapping("/start")
    public @ResponseBody
    GameResponse main(@RequestBody StartRequest request) {

        ApiResponse response;

        return start(request.getPlayers());
    }

    @RequestMapping("/")
    public @ResponseBody
    ApiResponse main() {
        return new ApiResponse(false, "INVALID METHOD");
    }

    private GameResponse start(List<PlayerJSON> playersJSON) {
        GameResponse response = new GameResponse();

        List<Player> players = new LinkedList<>();
        playersJSON.forEach(
                p -> {
                    Player player = new Player(p.getName());
                    players.add(player);
                }
        );

        Game game = Game.setUp(players);
        GameJSON gameJSON = GameManager.getInstance().addGame(game);

        response.setGame(gameJSON);
        response.setOk(true);
        return response;
    }

}
