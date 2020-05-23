package com.mokhovav.justAGame.Chess;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.base_spring_boot_project.exception.ValidException;
import com.mokhovav.inspiration.board.Board;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.inspiration.dice.DiceService;
import com.mokhovav.inspiration.field.Field;
import com.mokhovav.inspiration.field.FieldService;
import com.mokhovav.inspiration.link.LinkService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Lazy
public class Chess extends Board {

    public Chess(FieldService fieldService, BoardService boardService, LinkService linkService, DiceService diceService, Logger logger) {
        super(fieldService, boardService, linkService, diceService, logger);
        createBoard();
    }

    @Tracking
    public void createBoard(){
        Board board = null;
        try {
            int startLetter = 'A';
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8 ; j++) {
                    fieldList.add(new Field("" + (char)(startLetter + i) + (j+1)));
                }
            }
            String[] colors = {"silver", "black"};
            int i = 0;
            for (Field field : fieldList) {
                field.addProperty("color", colors[(i+i/8)%2]);
                field.addProperty("position", "" + ((i%8)*100 + 100) + ";" + ((i/8)*100 + 100)  + ";" + 0);
                i++;
            }
            //linkList = linkService.createRectangleLinks(fieldList,"",8,6);
            boardService.convertToFile(this,"D:\\javaProjects\\just_a_game\\src\\main\\resources\\Chess.map");

        }catch (ValidException | IOException e){
            logger.debug("Error in Chess: " + e.getMessage());
        }
        return;
    }
}