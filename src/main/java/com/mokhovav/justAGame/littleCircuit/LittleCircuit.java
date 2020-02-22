package com.mokhovav.justAGame.littleCircuit;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.base_spring_boot_project.exception.ValidException;
import com.mokhovav.inspiration.board.Board;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.inspiration.dice.Dice;
import com.mokhovav.inspiration.dice.DiceService;
import com.mokhovav.inspiration.field.Field;
import com.mokhovav.inspiration.field.FieldService;
import com.mokhovav.inspiration.item.Item;
import com.mokhovav.inspiration.link.LinkService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class LittleCircuit extends Board {

    public LittleCircuit(FieldService fieldService, BoardService boardService, LinkService linkService, DiceService diceService) {
        super(fieldService, boardService, linkService, diceService);
        createBoard();
    }

    @Tracking
    public void createBoard(){
        try {

            fieldList = fieldService.createFields(29, "F");
            fieldList.add(new Field("finish"));
            /* Create properties */
            int i = 0;
            Color[] colors = Color.values();

            for (Field field : fieldList) {
                field.addProperty("color", colors[i % colors.length].name());
                field.addProperty("position", "" + (i * 50 + 50) + ";" + (40 + (i % 2) * 40) + ";" + 0);
                i++;
            }
            linkList = linkService.createLineLinks(fieldList, "L");
            itemList = Arrays.asList(new Item("dog", fieldList.get(0)));
            diceList = Arrays.asList(diceService.createSimpleDice("first",6));


            boardService.convertToFile(this,"D:\\javaProjects\\just_a_game\\src\\main\\resources\\littleCircuit.map");

            Board board = boardService.getBoardFromFile("D:\\javaProjects\\just_a_game\\src\\main\\resources\\littleCircuit.map");
            this.fieldList = board.getFieldList();
            this.linkList = board.getLinkList();
            this.itemList = board.getItemList();
            this.diceList = board.getDiceList();

        }catch (ValidException | IOException e){
            System.out.println(e.getMessage());
        }
        return;
    }
}