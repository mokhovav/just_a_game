package com.mokhovav.justAGame.webSockets.gameMessage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.standard.WebSocketContainerFactoryBean;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

@Configuration
@EnableWebSocket
public class GameMessageConfig implements WebSocketConfigurer {
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new GameMessageHandler(), "/gameMessageHandler")
                // An interceptor to copy information from the HTTP session to the "handshake attributes"
                // map to made available via WebSocketSession.getAttributes()
                .addInterceptors(new HttpSessionHandshakeInterceptor())
                // to enable SockJS
                .withSockJS();

                // SockJS URL. http://host:port/{path-to-sockjs-endpoint}/{server-id}/{session-id}/{transport}
    }

    @Bean
    public GameMessageHandler gameMessageHandler(){
        return new GameMessageHandler();
    }

    @Bean
    public WebSocketContainerFactoryBean createWebSocketContainer() {
        WebSocketContainerFactoryBean container = new WebSocketContainerFactoryBean();
        container.setMaxTextMessageBufferSize(8192);
        container.setMaxBinaryMessageBufferSize(8192);
        return container;
    }
}
