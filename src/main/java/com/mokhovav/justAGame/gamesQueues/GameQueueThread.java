package com.mokhovav.justAGame.gamesQueues;


import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.inspiration.board.BoardFileData;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.justAGame.authentication.user.User;

import com.mokhovav.justAGame.games.Game;
import com.mokhovav.justAGame.games.GameService;
import com.mokhovav.justAGame.littleCircuit.LittleCircuit;
import com.mokhovav.justAGame.mongodb.GameSession;
import com.mokhovav.justAGame.userControl.Status;
import com.mokhovav.justAGame.userControl.UserSessionService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class GameQueueThread extends Thread{

    private final MongoTemplate mongoTemplate;
    private final BoardService boardService;
    private final LittleCircuit littleCircuit;

    private final GameService gameService;
    private final Logger logger;
    private final UserSessionService userSessionService;
    private GameQueue gameQueue;

    public GameQueueThread(GameService gameService, Logger logger, UserSessionService userSessionService, MongoTemplate mongoTemplate, BoardService boardService, LittleCircuit littleCircuit) {
        this.gameService = gameService;
        this.logger = logger;
        this.userSessionService = userSessionService;
        this.mongoTemplate = mongoTemplate;
        this.boardService = boardService;
        this.littleCircuit = littleCircuit;
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
                    int boats = 0;
                    while (userCount < maxPlayers) {
                        logger.info("add bot");
                        boats++;
                        userCount++;
                    }
                    startSession(users, boats);

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

    private void startSession(List<User> users, int boats) throws ValidException {

        BoardFileData boardFileData = boardService.convertToBoardFileData(littleCircuit);
        Map<Long, String> userMap = new HashMap<>();
        String[] str = new String[]{"dog","cat","rabbit","cow"};
        int i = 0;
        for (User user : users) {
            userMap.put(user.getId(), str[i++]);
        }

        GameSession gameSession = new GameSession(boardFileData, userMap);
        gameSession.setBoats(boats);
        mongoTemplate.insert(gameSession, "Games");

        for (User user : users) {
            userSessionService.setStatus(user, Status.PLAYING);
            userSessionService.setGameSession(user, gameSession.getId());
        }
        logger.info("Start game session");
    }
}
