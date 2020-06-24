package com.mokhovav.justAGame.littleCircuit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.base_spring_boot_project.baseClasses.BaseValid;
import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.inspiration.board.BoardDrawData;
import com.mokhovav.inspiration.board.BoardFileData;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.inspiration.dice.Dice;
import com.mokhovav.inspiration.dice.DiceService;
import com.mokhovav.justAGame.authentication.user.UserService;
import com.mokhovav.justAGame.mongodb.GameSession;
import com.mokhovav.justAGame.userControl.UserSession;
import com.mokhovav.justAGame.userControl.UserSessionService;
import com.mokhovav.justAGame.webSockets.gameMessage.GameMessage;
import org.slf4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

@Component
public class Master {

    private final MongoTemplate mongoTemplate;
    private final UserSessionService userSessionService;
    private final BaseValid baseValid;
    private final DiceService diceService;
    private final Logger logger;
    private final BoardService boardService;
    private final UserService userService;

    public Master(MongoTemplate mongoTemplate, UserSessionService userSessionService, BaseValid baseValid, DiceService diceService, Logger logger, BoardService boardService, UserService userService) {
        this.mongoTemplate = mongoTemplate;
        this.userSessionService = userSessionService;
        this.baseValid = baseValid;
        this.diceService = diceService;
        this.logger = logger;
        this.boardService = boardService;
        this.userService = userService;
    }

    @Async
    @EventListener//(condition = "#event.name eq 'littleCircuit'")
    @Tracking
    public void handleGameQueueEvent(MasterEvent event) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        GameMessage outputMessage = new GameMessage();
        GameMessage inputMessage = (GameMessage) event.getObject();
        Long userId = event.getUserId();
        try {

            UserSession userSession = userSessionService.getByUser(userId);
            if (userSession == null) throw new ValidException("Master: user session does not exist");
            String sessionId = userSession.getSessionId();
            if (baseValid.nullOrEmpty(sessionId)) throw new ValidException("Master: sessionId session does not exist");
            GameSession gameSession = mongoTemplate.findById(sessionId, GameSession.class);
            if (gameSession == null) throw new ValidException("Master: game session does not exist");
            WebSocketSession session = null;

            if (inputMessage.getCommand().equals("dice")) {
                if (gameSession.getMoveUserId() == userId) {
                    Dice dice = gameSession.getDiceList().get(0);
                    int rollValue = diceService.rollTheDice(dice);
                    logger.debug("roll value = " + rollValue);
                    //Board board = boardService.getBoardFromBoardFileData(gameSession);

                    if (gameSession.getFinisherId() == 0) {
                        BoardFileData boardFileData = boardService.makeAMove(gameSession, gameSession.getUsers().get(userId), rollValue);
                        gameSession.setBoardFileData(boardFileData);
                    }

                    for (String[] item : gameSession.getItemsList()) {
                        if (item[1].equals("finish")) gameSession.setFinisherId(userId);
                    }

                    BoardDrawData boardDrawData =
                            boardService.convertToBoardDrawData(boardService.getBoardFromBoardFileData(gameSession), "position");

                    if (gameSession.getFinisherId() == 0) {
                        boardDrawData.getMessages().put("dice", String.valueOf(rollValue));
                    } else {
                        boardDrawData.getMessages().put("dice", "The " + userService.getById(userId).getUsername() + " won!!!");
                    }
                    gameSession.setNextUserNumb();


                    mongoTemplate.save(gameSession);
                    outputMessage.setCommand("board");
                    outputMessage.setMessage(objectMapper.writeValueAsString(boardDrawData));


                    for (long l : gameSession.getUsersId()) {
                        session = event.getEstablishedSessionsLong().get(l);
                        if (session != null)
                            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(outputMessage)));
                    }
                }
            }
            else if (inputMessage.getCommand().equals("check")) {
                checkGameSession(gameSession);
            }
        } catch (ValidException e) {
            logger.info("handleGameQueueEvent exception: " + e.getMessage());
        }
        return;
    }

    public void checkGameSession(GameSession gameSession){
        if (gameSession != null) {
            UserSession currentUserSession = null;
            for (long currentUserId : gameSession.getUsersId()) {
                currentUserSession = userSessionService.getByUser(currentUserId);
                if (currentUserSession == null || !currentUserSession.getSessionId().equals(gameSession.getId())) {
                    gameSession.setBoats(gameSession.getBoats() + 1);
                    gameSession.getUsers().remove(currentUserId);
                    if (currentUserId == gameSession.getMoveUserId()) gameSession.setNextUserNumb();
                    if (currentUserId == gameSession.getMoveUserId()) {
                        mongoTemplate.remove(gameSession);
                        logger.info("END Of THE GAME");
                    } else {

                        long[] usersId = new long[gameSession.getUsers().size()];
                        int i = 0;
                        for (long id : gameSession.getUsersId()) {
                            if (id != currentUserId) usersId[i++] = id;
                        }
                        gameSession.setUsersId(usersId);
                        mongoTemplate.save(gameSession);
                    }
                }
            }
        }
    }

    public void checkGameSession(String id){
        GameSession gameSession = mongoTemplate.findById(id, GameSession.class);
        checkGameSession(gameSession);
    }

}
