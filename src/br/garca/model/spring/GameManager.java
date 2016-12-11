package br.garca.model.spring;

import br.garca.model.game.Game;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    private static GameManager ourInstance = new GameManager();

    private ConcurrentHashMap<Integer, Game> games;
    private Integer id;

    public static GameManager getInstance() {
        return ourInstance;
    }

    private GameManager() {
        id = 0;
        games = new ConcurrentHashMap<>();
    }

    public GameJSON addGame(Game game) {
        Integer id = generateId();

        GameJSON gameJson = new GameJSON();
        gameJson.setId(id);

        games.put(id, game);

        return gameJson;
    }

    private synchronized Integer generateId() {
        return ++id;
    }
}
