package de.leonmortenrichter.socketchess.chess;

import de.leonmortenrichter.socketchess.websocket.messages.GameJoinMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api")
public class GameServerController {
    private Logger logger = LoggerFactory.getLogger(GameServerController.class);

    private final GameModelRepository repository;

    public GameServerController(GameModelRepository repository) {
        this.repository = repository;
    }


    @PostMapping("/join")
    public GameJoinMessage joinGame() {

        String uuid = UUID.randomUUID().toString();
        String color = "w";
        GameModel game = repository.findFirstByHasEmptySlot(true);


        if (game == null) {
            logger.info("No waiting games, creating new game...");
            game = new GameModel(uuid);
        } else {
            logger.info("Found waiting game, joining...");
            color = game.joinGame(uuid);
        }

        logger.info(uuid);
        // update changes
        repository.save(game);
        return new GameJoinMessage(game.getId(), uuid, color, game.getHasEmptySlot());
    }
}
