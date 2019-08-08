package de.leonmortenrichter.socketchess.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private static final String MESSAGING_PREFIX = "/chess";
    private static final String[] DESTINATION_PREFIX = {"/chess"};
    private static final String STOMP_ENDPOINT = "/chess-websocket";


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(DESTINATION_PREFIX);
        config.setApplicationDestinationPrefixes(MESSAGING_PREFIX);
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(STOMP_ENDPOINT).withSockJS();
    }

}