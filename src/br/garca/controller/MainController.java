package br.garca.controller;

import br.garca.model.game.Game;
import br.garca.model.game.Player;
import br.garca.model.spring.Action;
import br.garca.model.spring.GameRequest;
import br.garca.model.spring.PlayerJSON;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
public class MainController {

    @RequestMapping("/api")
    public @ResponseBody GameRequest main(@RequestBody GameRequest request) {
        //System.out.println(request.getPlayers());
        //return new ResponseEntity<PlayerJSON>(request.getPlayers().get(0), HttpStatus.ACCEPTED);

        GameRequest game = new GameRequest();
        game.setAction(Action.START);
        List<PlayerJSON> players = new LinkedList<>();
        players.add(new PlayerJSON("Aragorn"));
        players.add(new PlayerJSON("Legolas"));
        game.setPlayers(players);

        return game;
    }
}
