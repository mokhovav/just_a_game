package com.mokhovav.justAGame;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.base_spring_boot_project.exception.ValidException;
import com.mokhovav.inspiration.board.BoardDrawData;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.inspiration.dice.Dice;
import com.mokhovav.inspiration.dice.DiceService;
import com.mokhovav.justAGame.Chess.Chess;
import com.mokhovav.justAGame.littleCircuit.LittleCircuit;
import com.mokhovav.justAGame.monopoly.Monopoly;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController{

    private final BoardService boardService;
    private final DiceService diceService;
    private final LittleCircuit littleCircuit;
    private final Monopoly monopoly;
    private final Chess chess;
    private final Logger logger;

    public BoardController(BoardService boardService, DiceService diceService, LittleCircuit littleCircuit, Monopoly monopoly, Chess chess, Logger logger) {
        this.boardService = boardService;
        this.diceService = diceService;
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
        System.out.println(text);

        if (text.equals("\"littleCircuit\"")) {
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
    @Tracking
    public BoardDrawData roll(@PathVariable String text) {
        try {
            if (text.equals("\"dice\"")) {
                Dice dice = littleCircuit.getDiceList().get(0);
                littleCircuit.getItemList().get(0).setField(littleCircuit.getFieldList().get(diceService.rollTheDice(dice)));
                return boardService.convertToBoardDrawData(littleCircuit, "position");
            }
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
        return null;
    }
}