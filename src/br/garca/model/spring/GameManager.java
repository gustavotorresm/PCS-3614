package br.garca.model.spring;

import br.garca.model.game.Game;
import br.garca.model.game.Player;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    private static GameManager ourInstance = new GameManager();

    private ConcurrentHashMap<Integer, Game> games;
    private ConcurrentHashMap<Integer, PlayerJSON> players;
    private Integer playerId;
    private Integer gameId;

    private BiMap<Integer, Player> gamePlayers;

    public static GameManager getInstance() {
        return ourInstance;
    }

    private GameManager() {
        gameId = 0;
        playerId = 0;

        games = new ConcurrentHashMap<>();
        players = new ConcurrentHashMap<>();
        gamePlayers = HashBiMap.create();
    }

    public GameJSON addGame(Game game) {
        Integer id = generateGameId();

        GameJSON gameJson = new GameJSON();
        gameJson.setId(id);

        games.put(id, game);

        return gameJson;
    }

    private synchronized Integer generatePlayerId() {
        return ++playerId;
    }

    private synchronized Integer generateGameId() {
        return ++gameId;
    }

    public void addPlayer(PlayerJSON player) {
        if (player.getId() == 0) {
            player.setId(generatePlayerId());
        }
        players.put(player.getId(), player);
    }

    public PlayerJSON getPlayer(int id) {
        return players.get(id);
    }

    public boolean hasPlayer(int id) {
        return players.containsKey(id);
    }

    public void mapPlayer(int id, Player player) {
        gamePlayers.put(id, player);
    }

    public void removePlayersMap(List<Player> players) {
        for (Player player :  players) {
            if (gamePlayers.containsValue(player)) {
                gamePlayers.remove(player);
            }
        }
    }

    public Game getGame(int id) {
        return games.get(id);
    }

    public Player getGamePlayer(int id) {
        return gamePlayers.get(id);
    }

    public Integer getIdFromPlayer(Player player) {
        return gamePlayers.inverse().get(player);
    }
}
