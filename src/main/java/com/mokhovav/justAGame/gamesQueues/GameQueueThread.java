package com.mokhovav.justAGame.gamesQueues;


import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.mokhovav.justAGame.webSockets.gameMessage.GameMessage;
import com.mokhovav.justAGame.webSockets.gameMessage.GameMessageHandler;
import org.slf4j.Logger;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

import java.sql.Timestamp;
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

    private final GameMessageHandler gameMessageHandler;

    public GameQueueThread(GameService gameService, Logger logger, UserSessionService userSessionService, MongoTemplate mongoTemplate, BoardService boardService, LittleCircuit littleCircuit, GameMessageHandler gameMessageHandler) {
        this.gameService = gameService;
        this.logger = logger;
        this.userSessionService = userSessionService;
        this.mongoTemplate = mongoTemplate;
        this.boardService = boardService;
        this.littleCircuit = littleCircuit;
        this.gameMessageHandler = gameMessageHandler;
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
                Timestamp timeNow = null;

                while (userCount < maxPlayers && System.currentTimeMillis() < endTime) {

                    while (gameQueue.getSize() > 0 && userCount < maxPlayers) {
                        User user = gameQueue.getUser();
                        Status status = null;
                        try {
                            status = userSessionService.getStatus(user);
                            timeNow = new Timestamp(System.currentTimeMillis());
                            if (timeNow.getTime() - userSessionService.getStatusTime(user).getTime() > game.getMoveTime()) throw new Exception("To Long");
                        } catch(Exception e){
                            status = Status.LOST;
                        }
                        if (status == Status.WAITSOCKET)
                            gameQueue.addUser(user);
                        else if (status == Status.WAITING) {
                            users.add(user);
                            userCount++;
                        }
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
        long[] userId = new long[users.size()];
        String[] str = new String[]{"dog","cat","rabbit","cow"};
        int i = 0;
        for (User user : users) {
            userMap.put(user.getId(), str[i]);
            userId[i++] = user.getId();
        }

        GameSession gameSession = new GameSession(boardFileData, userMap, userId);
        gameSession.setBoats(boats);
        gameSession.setMoveUserNumb(0);
        mongoTemplate.insert(gameSession, "game_sessions");

        for (User user : users) {
            userSessionService.setStatus(user, Status.PLAYING);
            userSessionService.setGameSession(user, gameSession.getId());

            try {
                GameMessage gameMessage = new GameMessage();
                ObjectMapper objectMapper = new ObjectMapper();

                gameMessage.setCommand("info");
                gameMessage.setMessage("Start game session");
                gameMessageHandler.getSession(user.getId()).sendMessage(new TextMessage(objectMapper.writeValueAsString(gameMessage)));

                gameMessage.setCommand("board");
                gameMessage.setMessage(objectMapper.writeValueAsString(boardService.convertToBoardDrawData(boardService.getBoardFromBoardFileData(gameSession),"position")));
                gameMessageHandler.getSession(user.getId()).sendMessage(new TextMessage(objectMapper.writeValueAsString(gameMessage)));

            } catch (Exception e) {
                logger.error("Failed to send message." + e.getMessage());
            }
        }
    }
}
