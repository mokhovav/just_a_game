package com.mokhovav.justAGame.gamesQueues;


import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.justAGame.authentication.user.User;

import com.mokhovav.justAGame.games.Game;
import com.mokhovav.justAGame.games.GameService;
import com.mokhovav.justAGame.userControl.Status;
import com.mokhovav.justAGame.userControl.UserSessionService;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class GameQueueThread extends Thread{
    private final GameService gameService;
    private final Logger logger;
    private final UserSessionService userSessionService;
    private GameQueue gameQueue;

    //TODO: change generation id
    long gameSessionId = 1;


    public GameQueueThread(GameService gameService, Logger logger, UserSessionService userSessionService) {
        this.gameService = gameService;
        this.logger = logger;
        this.userSessionService = userSessionService;
    }

    public void run() {
        logger.info(this.getName() + "thread is running...");
        int userCount;
        int maxPlayers;
        long startTime;
        long endTime;
        List<User> users;
        Game game = gameService.getByName(this.getName());

        while (true) {

            try {
                maxPlayers = game.getMaxPlayers();
                users = new ArrayList();
                userCount = 0;
                startTime = System.currentTimeMillis();
                endTime = startTime + game.getWaitingTime();

                while (userCount < maxPlayers && System.currentTimeMillis() < endTime) {

                    while (gameQueue.getSize() > 0 && userCount < maxPlayers) {
                        users.add(gameQueue.getUser());
                        userCount++;
                    }
                    if (userCount < maxPlayers) {
                        Thread.sleep(1000);
                    }

                    if (userCount == 0) endTime = System.currentTimeMillis() + game.getWaitingTime();
                }

                if (userCount != 0) {
                    while (userCount < maxPlayers) {
                        logger.info("add bot");
                        userCount++;
                    }
                    logger.info("Start game session");

                    for (User user : users) {
                        userSessionService.setStatus(user, Status.PLAYING);
                        userSessionService.setGameSession(user, gameSessionId);
                    }
                    //TODO: change!
                    gameSessionId++;
                }
            } catch (InterruptedException | ValidException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public void start(GameQueue gameQueue) {
        this.gameQueue = gameQueue;
        start();
    }
}
