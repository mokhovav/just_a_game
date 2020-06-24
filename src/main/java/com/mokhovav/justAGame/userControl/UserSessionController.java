package com.mokhovav.justAGame.userControl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.base_spring_boot_project.baseClasses.BaseValid;
import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.justAGame.games.Game;
import com.mokhovav.justAGame.games.GameService;
import com.mokhovav.justAGame.authentication.user.User;
import com.mokhovav.justAGame.gamesQueues.GameQueue;
import com.mokhovav.justAGame.gamesQueues.GameQueueThread;
import com.mokhovav.justAGame.littleCircuit.LittleCircuit;
import com.mokhovav.justAGame.littleCircuit.Master;
import com.mokhovav.justAGame.webSockets.gameMessage.GameMessage;
import com.mokhovav.justAGame.webSockets.gameMessage.GameMessageHandler;
import org.slf4j.Logger;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/session")
public class UserSessionController {

    private final ApplicationEventPublisher publisher;
    private final UserSessionService userSessionService;
    private static Map<String, GameQueue> gameQueueMap = new ManagedMap<>();
    private static Map<String, GameQueueThread> gameQueueThreadMap = new ManagedMap<>();
    private final GameQueue gameQueue;
    private final GameService gameService;
    private final Logger logger;
    private final MongoTemplate mongoTemplate;
    private final BoardService boardService;
    private final LittleCircuit littleCircuit;
    private final BaseValid baseValid;
    private final GameMessageHandler gameMessageHandler;
    private final Master master;


    public UserSessionController(ApplicationEventPublisher publisher, UserSessionService userSessionService, GameQueue gameQueue, GameService gameService, Logger logger, MongoTemplate mongoTemplate, BoardService boardService, LittleCircuit littleCircuit, BaseValid baseValid, GameMessageHandler gameMessageHandler, Master master) {
        this.publisher = publisher;
        this.userSessionService = userSessionService;
        this.gameQueue = gameQueue;
        this.gameService = gameService;
        this.logger = logger;
        this.mongoTemplate = mongoTemplate;
        this.boardService = boardService;
        this.littleCircuit = littleCircuit;
        this.baseValid = baseValid;
        this.gameMessageHandler = gameMessageHandler;
        this.master = master;
    }

    @PostMapping("{gameName}/{request}")
    @Tracking
    public GameMessage startSession(
            @AuthenticationPrincipal User user,
            @PathVariable String gameName, @PathVariable String request) throws ValidException, JsonProcessingException {

        GameMessage outputMessage = new GameMessage();
        try {

            if (user == null) throw new ValidException("Please login first");

            /*Class className = userSessionService.getClassByName(text);
            if (className == null) throw new ValidException("Game not found");
            Method getGameQueue = className.getDeclaredMethod("getGameQueue");

            GameQueue gameQueue = (GameQueue)getGameQueue.invoke(null);
            gameQueue.addUser(user);/**/

            if (baseValid.nullOrEmpty(gameName)) throw new ValidException("Invalid name of a game");
            //if (text.length() < 3) throw new ValidException("startSession: Invalid name of a game");
            //text = text.substring(1, text.length() - 1);

            Game game = gameService.getByName(gameName);
            if(game == null) throw new ValidException("Game not found");

            switch (request){
                case "start" :
                    GameQueue gameQueue = gameQueueMap.get(gameName);
                    if (gameQueue == null){
                        gameQueue  = this.gameQueue;
                        gameQueue.setGame(game);
                        gameQueueMap.put(gameName, gameQueue);
                        GameQueueThread gameQueueThread = new GameQueueThread(gameService, logger, userSessionService, mongoTemplate, boardService, littleCircuit, gameMessageHandler);
                        gameQueueThread.setName(gameName);
                        gameQueueThreadMap.put(gameName, gameQueueThread);
                        gameQueueThread.start(gameQueue);
                    }
                    UserSession session = userSessionService.save(user, Status.WAITSOCKET);

                    outputMessage.setCommand("startSession");
                    outputMessage.setMessage(session.getUserSecretId());
                    gameQueue.addUser(user);
                    break;

                case "onload" :
                    UserSession onloadSession = userSessionService.getByUser(user);
                    if (onloadSession != null) {
                        outputMessage.setCommand("startSession");
                        outputMessage.setMessage(onloadSession.getUserSecretId());
                        userSessionService.setStatus(user, Status.RELOAD);
                    }
                    else{
                        outputMessage.setCommand("info");
                        outputMessage.setMessage("Hello, " + user.getUsername()+ " !");
                    }
                    break;
                case "exit" :
                    UserSession userSession = userSessionService.getByUser(user);
                    gameMessageHandler.closeSession(user.getId());
                    if (userSession != null) {
                        String sessionId = userSession.getSessionId();
                        userSessionService.delete(userSession);
                        master.checkGameSession(sessionId);
                    }
                    outputMessage.setCommand("info");
                    outputMessage.setMessage("Exit");
                    break;

                default: new ValidException("Unknown request");
            }
            //publisher.publishEvent(new GameQueueEvent(gameQueue, text));
            return outputMessage;
        }catch (Exception e) {
            outputMessage.setCommand("error");
            outputMessage.setMessage(e.getMessage());
            return outputMessage;
        }
    }
}
