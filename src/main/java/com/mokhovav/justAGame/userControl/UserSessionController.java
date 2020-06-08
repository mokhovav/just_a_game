package com.mokhovav.justAGame.userControl;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.justAGame.games.Game;
import com.mokhovav.justAGame.games.GameService;
import com.mokhovav.justAGame.authentication.user.User;
import com.mokhovav.justAGame.gamesQueues.GameQueue;
import com.mokhovav.justAGame.gamesQueues.GameQueueThread;
import org.slf4j.Logger;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.ApplicationEventPublisher;
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


    public UserSessionController(ApplicationEventPublisher publisher, UserSessionService userSessionService, GameQueue gameQueue, GameService gameService, Logger logger) {
        this.publisher = publisher;
        this.userSessionService = userSessionService;
        this.gameQueue = gameQueue;
        this.gameService = gameService;
        this.logger = logger;
    }

    @PostMapping("/{text}")
    @Tracking
    public String startSession(
            @AuthenticationPrincipal User user,
            @PathVariable String text) throws ValidException {

        try {
            if (user == null) throw new ValidException("startSession: Please login first");

            /*Class className = userSessionService.getClassByName(text);
            if (className == null) throw new ValidException("Game not found");
            Method getGameQueue = className.getDeclaredMethod("getGameQueue");

            GameQueue gameQueue = (GameQueue)getGameQueue.invoke(null);
            gameQueue.addUser(user);/**/

            if (text.length() < 3) throw new ValidException("startSession: Invalid name of a game");
            text = text.substring(1, text.length() - 1);

            Game game = gameService.getByName(text);
            if(game == null) throw new ValidException("startSession: Game not found");

            GameQueue gameQueue = gameQueueMap.get(text);
            if (gameQueue == null){
                gameQueue  = this.gameQueue;
                gameQueue.setGame(game);
                gameQueueMap.put(text, gameQueue);
                GameQueueThread gameQueueThread = new GameQueueThread(gameService, logger, userSessionService);
                gameQueueThread.setName(text);
                gameQueueThreadMap.put(text, gameQueueThread);
                gameQueueThread.start(gameQueue);
            }
            gameQueue.addUser(user);
            //publisher.publishEvent(new GameQueueEvent(gameQueue, text));
            return "startSession: Wait other users...";

        }catch (Exception e) {
            return "startSession: " + e.getMessage();
        }
    }
}
