package br.garca.controller;

import br.garca.model.game.*;
import br.garca.model.spring.*;
import br.garca.model.spring.request.PlayRequest;
import br.garca.model.spring.request.StartRequest;
import br.garca.model.spring.response.ApiResponse;
import br.garca.model.spring.response.BetsResponse;
import br.garca.model.spring.response.GameResponse;
import br.garca.model.spring.response.OverviewResponse;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/api")
public class MainController {

    private HttpSession session;

    @RequestMapping("/addPlayer")
    public @ResponseBody
    PlayerResponse addPlayer(@RequestBody PlayerJSON player, HttpServletRequest request) {
        PlayerResponse response = new PlayerResponse();
        GameManager gm = GameManager.getInstance();

        if (gm.hasPlayer(request.getRemoteAddr().hashCode())) {
            response.setPlayer((PlayerJSON) gm.getPlayer(request.getRemoteAddr().hashCode()));
            response.setOk(false);
            response.setMessage("PLAYER ALREADY EXISTS");
        } else {
            player.setId(0);
            GameManager.getInstance().addPlayer(player);
            response.setPlayer(player);
            response.setOk(true);
        }

        return response;
    }

    @RequestMapping("/start")
    public @ResponseBody
    GameResponse start(@RequestBody StartRequest request) {
        GameResponse response = new GameResponse();

        List<Player> players = new LinkedList<>();

        request.getPlayers().forEach(
                p -> {
                    if (!GameManager.getInstance().hasPlayer(p.getId())) {
                        return;
                    }
                    PlayerJSON playerJSON = GameManager.getInstance().getPlayer(p.getId());

                    Player player = new Player(playerJSON.getId());
                    players.add(player);

                    GameManager.getInstance().mapPlayer(p.getId(), player);
                }
        );

        if (players.size() != request.getPlayers().size()) {
            response.setOk(false);
            response.setMessage("INVALID PLAYER");

            GameManager.getInstance().removePlayersMap(players);

            return response;
        }

        Game game = Game.setUp(players);
        game.startRound();

        GameJSON gameJSON = GameManager.getInstance().addGame(game);
        request.getPlayers().forEach(
                p -> GameManager.getInstance().getPlayer(p.getId()).setCurrentGame(gameJSON.getId())
        );

        response.setGame(gameJSON);
        response.setOk(true);
        return response;
    }

    @RequestMapping("/overview")
    public @ResponseBody
    OverviewResponse overview(@RequestBody PlayerJSON playerRequest) {
        OverviewResponse response = generateOverview(playerRequest.getId());

        return response;
    }

    @RequestMapping("/bet")
    public  @ResponseBody
    BetsResponse bet(@RequestBody BetJSON request) {
        BetsResponse response = new BetsResponse();
        PlayerJSON playerJSON = GameManager.getInstance().getPlayer(request.getPlayerId());

        Player player = GameManager.getInstance().getGamePlayer(request.getPlayerId());
        if (player == null) {
            response.setOk(false);
            response.setMessage("PLAYER DOES NOT EXIST");
            return response;
        }

        Game game = GameManager.getInstance().getGame(playerJSON.getCurrentGame());
        if (game == null) {
            response.setOk(false);
            response.setMessage("PLAYER IS NOT IN A GAME");
            return response;
        }

        if (! game.getCurrentPlayer().equals(player)) {
            response.setOk(false);
            response.setMessage("NOT YOUR TURN");

            return response;
        }

        try {
            game.bet(request.getBet());
        } catch (GameException e) {
            if (e.getCode() == Code.INVALID_BET) {
                response.setOk(false);
                response.setMessage("INVALID BET");

                return response;
            }
        }

        Map<Player, Integer> bets = game.getBets();
        List<BetJSON> betsJson = new LinkedList<>();
        bets.entrySet().forEach(
                e -> {
                    Integer playerId = GameManager.getInstance().getIdFromPlayer(e.getKey());
                    BetJSON bet = new BetJSON();

                    bet.setPlayerId(playerId);
                    bet.setBet(e.getValue());

                    betsJson.add(bet);
                }
        );

        response.setCurrentPlayer(game.getCurrentPlayer().getId());

        response.setBets(betsJson);

        if (game.isTurnFinished()) {
            game.nextTurn();
        }

        response.setOk(true);
        return response;
    }

    @RequestMapping("/getBets")
    public @ResponseBody
    BetsResponse getBets(@RequestBody PlayerJSON playerRequest) {
        BetsResponse response = new BetsResponse();
        PlayerJSON playerJSON = GameManager.getInstance().getPlayer(playerRequest.getId());

        Player player = GameManager.getInstance().getGamePlayer(playerJSON.getId());
        Game game = GameManager.getInstance().getGame(playerJSON.getCurrentGame());

        Map<Player, Integer> bets = game.getBets();
        List<BetJSON> betsJson = new LinkedList<>();
        bets.entrySet().forEach(
                e -> {
                    Integer playerId = GameManager.getInstance().getIdFromPlayer(e.getKey());
                    BetJSON bet = new BetJSON();

                    bet.setPlayerId(playerId);
                    bet.setBet(e.getValue());

                    betsJson.add(bet);
                }
        );

        response.setCurrentPlayer(game.getCurrentPlayer().getId());

        response.setBets(betsJson);

        response.setOk(true);
        return response;
    }

    @RequestMapping("/finishRound")
    public @ResponseBody
    OverviewResponse finishRound(@RequestBody PlayerJSON request) {
        OverviewResponse response = new OverviewResponse();

        Player player = GameManager.getInstance().getGamePlayer(request.getId());
        if (player == null) {
            response.setOk(false);
            response.setMessage("PLAYER DOES NOT EXIST");
            return response;
        }

        PlayerJSON playerJSON = GameManager.getInstance().getPlayer(player.getId());
        Game game = GameManager.getInstance().getGame(playerJSON.getCurrentGame());
        if (game == null) {
            response.setOk(false);
            response.setMessage("PLAYER IS NOT IN A GAME");
            return response;
        }

        if (!game.isTurnFinished()) {
            response.setOk(false);
            response.setMessage("TURN IS NOT YET FINISHED");
            return response;
        }

        game.startRound();

        response = generateOverview(player.getId());
        return response;
    }

    @RequestMapping("/finishTurn")
    public @ResponseBody
    OverviewResponse finishTurn(@RequestBody PlayerJSON request) {
        OverviewResponse response = new OverviewResponse();

        Player player = GameManager.getInstance().getGamePlayer(request.getId());
        if (player == null) {
            response.setOk(false);
            response.setMessage("PLAYER DOES NOT EXIST");
            return response;
        }

        PlayerJSON playerJSON = GameManager.getInstance().getPlayer(player.getId());
        Game game = GameManager.getInstance().getGame(playerJSON.getCurrentGame());
        if (game == null) {
            response.setOk(false);
            response.setMessage("PLAYER IS NOT IN A GAME");
            return response;
        }

        if (!game.isTurnFinished()) {
            response.setOk(false);
            response.setMessage("TURN IS NOT YET FINISHED");
            return response;
        }

        game.nextTurn();

        response = generateOverview(player.getId());
        return response;
    }

    @RequestMapping("/play")
    public @ResponseBody
    OverviewResponse play(@RequestBody PlayRequest request) {
        OverviewResponse response = new OverviewResponse();

        Player player = GameManager.getInstance().getGamePlayer(request.getPlayerId());
        if (player == null) {
            response.setOk(false);
            response.setMessage("PLAYER DOES NOT EXIST");
            return response;
        }

        PlayerJSON playerJSON = GameManager.getInstance().getPlayer(player.getId());
        Game game = GameManager.getInstance().getGame(playerJSON.getCurrentGame());
        if (game == null) {
            response.setOk(false);
            response.setMessage("PLAYER IS NOT IN A GAME");
            return response;
        }

        if (! game.getCurrentPlayer().equals(player)) {
            response.setOk(false);
            response.setMessage("NOT YOUR TURN");
            return response;
        }

        try {
            if (request.getCard() == null) {
                game.play(player);
            } else {
                int number = request.getCard().getNumber();
                Suit suit = request.getCard().getSuit();

                Card card = new Card(number, suit);
                game.play(card);
            }
        } catch (GameException e) {
            response.setOk(false);
            response.setMessage(e.getMessage());
            return response;
        }

        response = generateOverview(player.getId());

        return response;
    }

    @RequestMapping("/newRound")
    public @ResponseBody
    ApiResponse newRound(@RequestBody PlayerJSON request) {
        ApiResponse response = new ApiResponse();

        PlayerJSON playerJSON = GameManager.getInstance().getPlayer(request.getId());
        Game game = GameManager.getInstance().getGame(playerJSON.getCurrentGame());

        game.startRound();

        response.setOk(true);
        return response;
    }

    @RequestMapping("/")
    public @ResponseBody
    ApiResponse main() {
        return new ApiResponse(false, "INVALID METHOD");
    }

    private OverviewResponse generateOverview(Integer playerId) {
        OverviewResponse response = new OverviewResponse();

        PlayerJSON playerJSON = GameManager.getInstance().getPlayer(playerId);
        if (playerJSON == null) {
            response.setOk(false);
            response.setMessage("PLAYER DOES NOT EXIST");

            return response;
        }

        Game game = GameManager.getInstance().getGame(playerJSON.getCurrentGame());
        if (game == null) {
            response.setOk(false);
            response.setMessage("PLAYER IS NOT IN A GAME");

            return response;
        }

        Player player= GameManager.getInstance().getGamePlayer(playerJSON.getId());

        Map<Player, Set<Card>> visibleCards = game.getVisibleHands(player);
        Map<Player, Card> playedCards = game.getPlayedCards();
        Map<Player, Integer> cardsNumber = game.getCardsNumber();

        List<HandJSON> hands= new LinkedList<>();
        visibleCards.keySet().forEach(
                p -> {
                    HandJSON hand = new HandJSON();

                    hand.setQuantity(cardsNumber.get(p));

                    List<CardJSON> cards = new LinkedList<>();
                    visibleCards.get(p).forEach(
                            c -> {
                                CardJSON cardJSON = new CardJSON();
                                cardJSON.setSuit(c.getSuit());
                                cardJSON.setNumber(c.getNumber());

                                cards.add(cardJSON);
                            }
                    );
                    hand.setCards(cards);

                    hand.setPlayerId(GameManager.getInstance().getIdFromPlayer(p));

                    hands.add(hand);
                }
        );
        response.setHands(hands);

        Map<Integer, CardJSON> playedCardsJSON = new HashMap<>();
        playedCards.keySet().forEach(
                p -> {
                    Integer id = GameManager.getInstance().getIdFromPlayer(p);
                    CardJSON cardJSON = new CardJSON();
                    cardJSON.setNumber(playedCards.get(p).getNumber());
                    cardJSON.setSuit(playedCards.get(p).getSuit());

                    playedCardsJSON.put(id, cardJSON);
                }
        );
        response.setHands(hands);
        response.setPlayedCards(playedCardsJSON);

        CardJSON vira = new CardJSON();
        vira.setNumber(game.getVira().getNumber());
        vira.setSuit(game.getVira().getSuit());

        response.setBetting(game.isBetting());
        response.setRoundFinished(game.isRoundFinished());
        response.setTurnFinished(game.isTurnFinished());

        HashMap<Integer, Integer> score = new HashMap<>();
        game.getScore().entrySet().forEach(
                e -> {
                    score.put(e.getKey().getId(), e.getValue());
                }
        );
        response.setScore(score);

        if (game.getWinner() != null) {
            response.setWinner(game.getWinner().getId());
        }

        if (game.getCurrentPlayer() != null) {
            response.setCurrentPlayer(game.getCurrentPlayer().getId());
        }

        response.setVira(vira);

        response.setOk(true);
        return response;
    }
}
