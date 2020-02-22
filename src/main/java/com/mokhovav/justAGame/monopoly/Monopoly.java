package com.mokhovav.justAGame.monopoly;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.inspiration.board.Board;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.inspiration.dice.DiceService;
import com.mokhovav.inspiration.field.Field;
import com.mokhovav.inspiration.field.FieldService;
import com.mokhovav.inspiration.link.LinkService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy
public class Monopoly extends Board {
    public Monopoly(FieldService fieldService, BoardService boardService, LinkService linkService, DiceService diceService) {
        super(fieldService, boardService, linkService, diceService);
        createBoard();
    }

    @Tracking
    private Board createBoard(){
        Board board = null;
        try {
            fieldList = fieldService.createFields(36, "F");
            //fieldList.add(new Field("finish"));
            /* Create properties */
            int i = 0;

            for (Field field : fieldList) {
                field.addProperty("color", "orange");
                if (i < 10)
                    field.addProperty("position", "" + (i * 100 + 50) + ";" + 50 + ";" + 0);
                else if (i<19)
                    field.addProperty("position", "" + 950 + ";" + (50 + (i-9) *100)+ ";" + 0);
                else if (i< 28)
                    field.addProperty("position", "" + (950 - (i-18) * 100) + ";" + 950 + ";" + 0);
                else if (i< 36)
                    field.addProperty("position", "" + 50 + ";" + (950 - (i-27) * 100) + ";" + 0);
                i++;
            }
            linkList = linkService.createCircularLinks(fieldList);
            boardService.convertToFile(this,"D:\\javaProjects\\just_a_game\\src\\main\\resources\\Monopoly.map");

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return board;
    }
}
