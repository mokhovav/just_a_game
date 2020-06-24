package com.mokhovav.justAGame.webSockets.gameMessage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.justAGame.authentication.user.User;
import com.mokhovav.justAGame.littleCircuit.MasterEvent;
import com.mokhovav.justAGame.mongodb.GameSession;
import com.mokhovav.justAGame.mongodb.GameSessionRepository;
import com.mokhovav.justAGame.userControl.Status;
import com.mokhovav.justAGame.userControl.UserSession;
import com.mokhovav.justAGame.userControl.UserSessionService;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
public class GameMessageHandler extends TextWebSocketHandler {

    private static Map<Long, WebSocketSession> establishedSessionsLong = new HashMap<>();
    private static Map<WebSocketSession, Long> establishedSessionsSocket = new HashMap<>();

    //private List<WebSocketSession> establishedSessions = new CopyOnWriteArrayList<>();

    private final UserSessionService userSessionService;
    private final ApplicationEventPublisher publisher;
    private final Logger logger;
    private final MongoTemplate mongoTemplate;
    private final BoardService boardService;
    private final GameSessionRepository repository;

    public GameMessageHandler(UserSessionService userSessionService, ApplicationEventPublisher publisher, Logger logger, MongoTemplate mongoTemplate, BoardService boardService, GameSessionRepository repository) {
        this.userSessionService = userSessionService;
        this.publisher = publisher;
        this.logger = logger;
        this.mongoTemplate = mongoTemplate;
        this.boardService = boardService;
        this.repository = repository;
    }

    // Called after a connection is established
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        //establishedSessions.add(session);
        GameMessage gameMessage = new GameMessage();
        gameMessage.setCommand("getUserId");
        ObjectMapper objectMapper = new ObjectMapper();
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(gameMessage)));
    }

    // Called after the connection is closed
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws ValidException {
        long userId = establishedSessionsSocket.get(session);
        establishedSessionsSocket.remove(session);
        establishedSessionsLong.remove(userId);
        userSessionService.setStatus(userId, Status.LOST);
        System.out.println("CLOSE");
}

    // Called after receiving a message
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws ValidException, IOException {
        GameMessage outputMessage = new GameMessage();
        ObjectMapper objectMapper = new ObjectMapper();

        GameMessage inputMessage = objectMapper.readValue(message.getPayload(), GameMessage.class);
        if (inputMessage.getCommand().equals("getUserId")) {
            UserSession userSession = userSessionService.getBySecretId(inputMessage.getMessage());
            try {
                if (userSession == null) throw new ValidException("Socket: user session does not exist");
                User user = userSession.getUser();
                Status status = userSessionService.getStatus(user);

                if (status == Status.WAITSOCKET) {
                    userSessionService.setStatus(user, Status.WAITING);
                    establishedSessionsLong.put(user.getId(), session);
                    establishedSessionsSocket.put(session, user.getId());
                    outputMessage.setCommand("info");
                    outputMessage.setMessage("Waiting for other users");
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(outputMessage)));
                }

                if (status == Status.RELOAD) {
                    userSessionService.setStatus(user, Status.PLAYING);
                    WebSocketSession oldSession = getSession(user.getId());
                    establishedSessionsLong.remove(user.getId());
                    establishedSessionsSocket.remove(oldSession);
                    establishedSessionsLong.put(user.getId(), session);
                    establishedSessionsSocket.put(session, user.getId());
                    outputMessage.setCommand("info");
                    outputMessage.setMessage("Reconnect");
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(outputMessage)));

                    String sessionId = userSessionService.getByUser(user).getSessionId();
                    GameSession gameSession = mongoTemplate.findById(sessionId, GameSession.class);
                    //Optional<GameSession> optionalGameSession = repository.findById(sessionId);
                    //boolean res = optionalGameSession.isPresent();
                    if (gameSession == null) throw new ValidException("Socket: game session does not exist");

                    outputMessage.setCommand("board");
                    outputMessage.setMessage(objectMapper.writeValueAsString(boardService.convertToBoardDrawData(boardService.getBoardFromBoardFileData(gameSession), "position")));
                    session.sendMessage(new TextMessage(objectMapper.writeValueAsString(outputMessage)));

                }
            } catch (ValidException e) {
                outputMessage.setCommand("error");
                outputMessage.setMessage(e.getMessage());
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(outputMessage)));
            }


        } else {
            publisher.publishEvent(new MasterEvent(inputMessage, establishedSessionsSocket.get(session), inputMessage.getCommand(), establishedSessionsLong));
        }
    }

      /*  establishedSessions.forEach(establishedSession
                -> sendMessageToClient(message, establishedSession));/**/


    public WebSocketSession getSession(Long userId) {
        return establishedSessionsLong.get(userId);
    }

    public Long getUserId(WebSocketSession session) {
        return establishedSessionsSocket.get(session);
    }

    public void closeSession(Long userId) throws IOException {
        WebSocketSession session = getSession(userId);
        if (session != null) {
            session.close();
            //establishedSessionsSocket.remove(session);
           // establishedSessionsLong.remove(userId);
        }
    }
}
