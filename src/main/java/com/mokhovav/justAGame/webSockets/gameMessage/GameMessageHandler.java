package com.mokhovav.justAGame.webSockets.gameMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameMessageHandler extends TextWebSocketHandler {
    private List<WebSocketSession> establishedSessions = new CopyOnWriteArrayList<>();

    // Called after a connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("afterConnectionEstablished");
        establishedSessions.add(session);
    }

    // Called after a connection is closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        establishedSessions.remove(session);
    }

    // Called after receiving a message
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        establishedSessions.forEach(establishedSession
                -> sendMessageToClient(message, establishedSession));
    }

    private void sendMessageToClient(TextMessage message,
                                     WebSocketSession establishedSession) {
        try {
            GameMessage gameMessage = new GameMessage();
            gameMessage.setMessage("Hello");
            gameMessage.setCommand("wait");
            gameMessage.setText(message.getPayload());
            ObjectMapper objectMapper = new ObjectMapper();
            establishedSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(gameMessage)));
        } catch (IOException e) {
            System.out.println("Failed to send message." + e.getMessage());
        }
    }
}
