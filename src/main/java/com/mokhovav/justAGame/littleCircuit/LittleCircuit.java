package com.mokhovav.justAGame.littleCircuit;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.inspiration.board.Board;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.inspiration.dice.DiceService;
import com.mokhovav.inspiration.field.Field;
import com.mokhovav.inspiration.field.FieldService;
import com.mokhovav.inspiration.item.Item;
import com.mokhovav.inspiration.link.LinkService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
@Lazy
public class LittleCircuit extends Board {

    public LittleCircuit(FieldService fieldService, BoardService boardService, LinkService linkService, DiceService diceService, Logger logger) {
        super(fieldService, boardService, linkService, diceService, logger);
        createBoard();
    }

    @Tracking
    public void createBoard(){
        try {
            fieldList.clear();
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

            linkList.clear();
            linkList = linkService.createLineLinks(fieldList, "L");
            Item dog = new Item("dog", fieldList.get(0));
            Item cat = new Item("cat", fieldList.get(0));
            Item rabbit = new Item("rabbit", fieldList.get(0));
            Item cow = new Item("cow", fieldList.get(0));
            dog.addProperty("offsetX", "-10");
            dog.addProperty("offsetY", "-10");
            dog.addProperty("color", "black" );
            cat.addProperty("offsetX", "-10");
            cat.addProperty("offsetY", "10");
            cat.addProperty("color", "orange" );
            rabbit.addProperty("offsetX", "10");
            rabbit.addProperty("offsetY", "-10");
            rabbit.addProperty("color", "gray" );
            cow.addProperty("offsetX", "10");
            cow.addProperty("offsetY", "10");
            cow.addProperty("color", "white" );

            itemList.clear();
            itemList.add(dog);
            itemList.add(cat);
            itemList.add(rabbit);
            itemList.add(cow);

            diceList.clear();
            diceList = Arrays.asList(diceService.createSimpleDice("first",6));

            boardService.convertToFile(this,".\\littleCircuit.map");
            //Board board = boardService.getBoardFromFile("\\littleCircuit.map");
            //this.fieldList = board.getFieldList();
            //this.linkList = board.getLinkList();
            //this.itemList = board.getItemList();
            //this.diceList = board.getDiceList();

        }catch (ValidException | IOException e){
           logger.debug("Error in LittleCircuit: " + e.getMessage());
        }
        return;
    }
}