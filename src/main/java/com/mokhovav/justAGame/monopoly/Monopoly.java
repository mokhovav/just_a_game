package com.mokhovav.justAGame.monopoly;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.base_spring_boot_project.exception.ValidException;
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
import java.util.*;

@Component
@Lazy
public class Monopoly extends Board {

    public Monopoly(FieldService fieldService, BoardService boardService, LinkService linkService, DiceService diceService, Logger logger) {
        super(fieldService, boardService, linkService, diceService, logger);
        createBoard();
    }

    @Tracking
    public void createBoard(){
        try {
            int sizeX = 9;
            int sizeY = 9;
            int tempX = sizeX - 1;
            int tempY = sizeY - 1;

            fieldList = fieldService.createFields((tempX + tempY)*2, "F");
            int i = 0;
            /* Create properties */
            for (Field field : fieldList) {
                field.addProperty("color", "orange");
                if (i <= tempX)
                    field.addProperty("position", "" + (i * 100 + 50) + ";" + 50 + ";" + 0);
                else if (i <= tempX + tempY)
                    field.addProperty("position", "" + (tempX * 100 + 50) + ";" + (50 + (i-tempX) *100)+ ";" + 0);
                else if (i <= tempX*2 + tempY)
                    field.addProperty("position", "" + ((tempX * 100 + 50) - (i - (tempX + tempY)) * 100) + ";" + (tempY  * 100 + 50)+ ";" + 0);
                else if (i < (tempX + tempY)*2)
                    field.addProperty("position", "" + 50 + ";" + ((tempY * 100 + 50) - (i-(tempX*2 + tempY)) * 100) + ";" + 0);
                i++;
            }
            linkList = linkService.createCircularLinks(fieldList);
            Item cylinder = new Item("Cylinder", fieldList.get(0));
            cylinder.getProperties().put("color", "silver");
            itemList.add(cylinder);
            diceList.add(diceService.createSimpleDice("first",6));
            diceList.add(diceService.createSimpleDice("second",6));

            Map<Integer, String> fortune = new HashMap<>();
            fortune.put(1, "The house is free! If you own all the plots of the same color, build a house on any of them for free. Use if necessary.");
            fortune.put(2, "Raw deal! Steal the Owner’s card from one of the players. You can not steal the owner’s card from the full set. Use if necessary.");
            fortune.put(3, "Raw deal! Steal the Owner’s card from one of the players. You can not steal the owner’s card from the full set. Use if necessary.");

            fortune.put(101, "Be generous! Buy or auction this site, then give all other players 20K. Use immediately");
            fortune.put(102, "Be generous! Buy or auction this site, then give all other players 20K. Use immediately");
            fortune.put(103, "Ride a luxury limousine. Buy or auction this site, then go to the \"Go!\". Use immediately");

            boardService.convertToFile(this,".\\maps\\Monopoly.map");
            boardService.getBoardFromFile(".\\Monopoly.map");

        }catch (ValidException | IOException e){
            logger.debug("Error in Monopoly: " + e.getMessage());
        }
        return;
    }
}
