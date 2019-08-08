package de.leonmortenrichter.socketchess.websocket.listeners;

import de.leonmortenrichter.socketchess.chess.GameModel;
import de.leonmortenrichter.socketchess.chess.GameModelRepository;
import de.leonmortenrichter.socketchess.websocket.messages.GameDisconnectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class SessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent> {


    private final GameModelRepository repository;

    private final SimpMessagingTemplate template;

    private static final Logger logger = LoggerFactory.getLogger(SessionDisconnectedEventListener.class);

    public SessionDisconnectedEventListener(GameModelRepository repository, SimpMessagingTemplate template) {
        this.repository = repository;
        this.template = template;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());

        String playerId = headerAccessor.getSessionId();

        if (playerId == null)
            return;

        GameModel game = repository.findByPlayer1IdOrPlayer2Id(playerId, playerId);

        if (game == null)
            return;

        game.disconnectById(playerId);
        logger.info("Client " + playerId + " has disconnected!");

        // delete games without active players
        if (game.getNumPlayer() == 0)
            repository.delete(game);
        else
            repository.save(game);

        template.convertAndSend("/chess/state/" + game.id, new GameDisconnectMessage(game.isPlayer1(playerId) ? "player1" : "player2"));
    }

}
