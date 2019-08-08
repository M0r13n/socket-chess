package de.leonmortenrichter.socketchess.websocket.controller;

import de.leonmortenrichter.socketchess.chess.GameModel;
import de.leonmortenrichter.socketchess.chess.GameModelRepository;
import de.leonmortenrichter.socketchess.websocket.messages.GameStateMessage;
import de.leonmortenrichter.socketchess.websocket.messages.MoveMessage;
import de.leonmortenrichter.socketchess.websocket.messages.PlayerJoinMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;

import javax.persistence.EntityNotFoundException;

@Controller
public class GameController {
    private Logger logger = LoggerFactory.getLogger(GameController.class);

    private final GameModelRepository repository;

    public GameController(GameModelRepository repository) {
        this.repository = repository;
    }

    @MessageMapping("/makeMove/{id}")
    @SendTo("/chess/state/{id}")
    public GameStateMessage move(@DestinationVariable Long id, MoveMessage move) {
        GameModel game = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        boolean moveMade = game.makeMove(move.getFrom(), move.getTo(), move.getUuid());

        logger.info(moveMade ? "Move is legal" : "Move is illegal");
        repository.save(game);

        return new GameStateMessage(game);
    }

    @MessageMapping("join/{id}")
    @SendTo("/chess/state/{id}")
    public GameStateMessage join(@DestinationVariable Long id, PlayerJoinMessage message, StompHeaderAccessor headerAccessor) {
        GameModel game = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        game.websocketJoin(message.getUuid(), headerAccessor.getSessionId());
        logger.info("Client joined!");
        repository.save(game);
        return new GameStateMessage(game);
    }

}
