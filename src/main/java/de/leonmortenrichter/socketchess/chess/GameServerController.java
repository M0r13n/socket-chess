package de.leonmortenrichter.socketchess.chess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class GameServerController {
    private Logger logger = LoggerFactory.getLogger(GameServerController.class);

    private final GameModelRepository repository;

    public GameServerController(GameModelRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/games")
    List<GameModel> all() {
        return repository.findAll();
    }


    @PostMapping("/join")
    public GameModel joinGame(@RequestBody PlayerModel player) {
        // join an empty game or create a new one if no game exists
        GameModel game = repository.findFirstByPlayer2IsNull();
        String playerId = player.getPlayerId();

        if (playerId == null) {
            logger.info("No valid playerId provided. Aborting...");
            return null;
        }

        if (game == null) {
            logger.info("No waiting games, creating new game...");
            game = new GameModel(playerId);
        } else {
            logger.info("Found waiting game, joining...");
            game.setPlayer2(playerId);
        }

        // update changes
        repository.save(game);
        return game;
    }
}
