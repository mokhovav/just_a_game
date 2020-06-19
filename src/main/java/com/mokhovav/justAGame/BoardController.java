package com.mokhovav.justAGame;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.inspiration.board.BoardDrawData;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.inspiration.dice.Dice;
import com.mokhovav.inspiration.dice.DiceService;
import com.mokhovav.inspiration.item.Item;
import com.mokhovav.inspiration.item.ItemService;
import com.mokhovav.justAGame.Chess.Chess;
import com.mokhovav.justAGame.authentication.user.User;
import com.mokhovav.justAGame.littleCircuit.LittleCircuit;
import com.mokhovav.justAGame.monopoly.Monopoly;
import org.slf4j.Logger;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController{

    private final BoardService boardService;
    private final DiceService diceService;
    private final ItemService itemService;
    private final LittleCircuit littleCircuit;
    private final Monopoly monopoly;
    private final Chess chess;
    private final Logger logger;

    private int step = 0;
    private Item finisher = null;

    public BoardController(BoardService boardService, DiceService diceService, ItemService itemService, LittleCircuit littleCircuit, Monopoly monopoly, Chess chess, Logger logger) {
        this.boardService = boardService;
        this.diceService = diceService;
        this.itemService = itemService;
        this.littleCircuit = littleCircuit;
        this.monopoly = monopoly;
        this.chess = chess;
        this.logger = logger;
    }


    @GetMapping("/test")
    @Tracking
    public String boardGetRequest(
            @RequestParam(defaultValue = "Hi") String text1,
            @RequestParam(defaultValue = "iH") String text2
    ) {
        logger.info(text1 + " " + text2);
        return "Hello!";

    }

    @PostMapping("/test/{text}")
    @Tracking
    public BoardDrawData boardPostRequest(@PathVariable String text) {
        if (text.equals("\"littleCircuit\"")) {
            step = 0;
            return boardService.convertToBoardDrawData(littleCircuit, "position");
        }
        if (text.equals("\"Monopoly\"")) {
            return boardService.convertToBoardDrawData(monopoly, "position");
        }
        if (text.equals("\"Chess\"")) {
            return boardService.convertToBoardDrawData(chess, "position");
        }
        return null;
    }




    @PostMapping("/roll/{text}")
   // @MessageMapping("/roll/{text}")
   // @SendTo("/topic/greetings")     // The return value is broadcast to all subscribers of /topic/greetings
    @Tracking
    public BoardDrawData roll(@AuthenticationPrincipal User user,
                              @PathVariable String text) {
        try {
            if (text.equals("\"dice\"")) {
                Dice dice = littleCircuit.getDiceList().get(0);
                int rollValue = diceService.rollTheDice(dice);
                logger.debug("roll value = " + rollValue);



                if (finisher == null) {
                    switch (step++ % 4) {
                        case 0:
                            boardService.makeAMove(littleCircuit, "dog", rollValue);
                            break;
                        case 1:
                            boardService.makeAMove(littleCircuit, "cat", rollValue);
                            break;
                        case 2:
                            boardService.makeAMove(littleCircuit, "rabbit", rollValue);
                            break;
                        case 3:
                            boardService.makeAMove(littleCircuit, "cow", rollValue);
                    }
                }

                for (Item item : littleCircuit.getItemList()) {
                    if (item.getField().getName().equals("finish")) finisher = item;
                }

                BoardDrawData boardDrawData = boardService.convertToBoardDrawData(littleCircuit, "position");
                if(finisher == null) {
                    boardDrawData.getMessages().put("dice", String.valueOf(rollValue));
                } else {
                    boardDrawData.getMessages().put("dice", "The " + finisher.getName() + " won!!!");
                }
                return boardDrawData;
            }

            if (text.equals("\"dices\"")) {
                Dice dice1 = monopoly.getDiceList().get(0);
                Dice dice2 = monopoly.getDiceList().get(1);
                int rollValue = diceService.rollTheDice(dice1) + diceService.rollTheDice(dice2);
                boardService.makeAMove(monopoly, "Cylinder", rollValue);

                BoardDrawData boardDrawData = boardService.convertToBoardDrawData(monopoly, "position");
                boardDrawData.getMessages().put("dice", String.valueOf(rollValue));
                return boardDrawData;
            }

            if (text.equals("\"newGame\"")) {
                step = 0;
                finisher = null;
                littleCircuit.createBoard();
                return boardService.convertToBoardDrawData(littleCircuit, "position");
            }

        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }
}