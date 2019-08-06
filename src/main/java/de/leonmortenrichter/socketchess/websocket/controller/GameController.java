package de.leonmortenrichter.socketchess.websocket.controller;

import de.leonmortenrichter.socketchess.chess.GameModel;
import de.leonmortenrichter.socketchess.chess.GameModelRepository;
import de.leonmortenrichter.socketchess.websocket.messages.MoveMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
    public GameModel move(@DestinationVariable Long id, MoveMessage move) {
        GameModel game = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("Client made makeMove");
        logger.info(move.toString());
        // move
        Boolean moveMade = game.makeMove(move.getFrom(), move.getTo());

        logger.info(moveMade ? "Move is legal" : "Move is illegal");

        repository.save(game);
        return game;
    }

    @MessageMapping("join/{id}")
    @SendTo("/chess/state/{id}")
    public GameModel join(@DestinationVariable Long id) {
        GameModel game = repository.findById(id).orElseThrow(EntityNotFoundException::new);
        logger.info("Client joined!");
        return game;
    }

}
