package com.mokhovav.justAGame.mongodb;

import com.mokhovav.inspiration.board.BoardFileData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "game_sessions")
public class GameSession extends BoardFileData {
    @Id
    private String id;

    private String firstName;
    private String lastName;

    public GameSession(String firstName, String lastName, BoardFileData boardFileData) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.setDiceList(boardFileData.getDiceList());
        this.setFieldList(boardFileData.getFieldList());
        this.setFieldsPropertiesList(boardFileData.getFieldsPropertiesList());
        this.setItemsList(boardFileData.getItemsList());
        this.setItemsPropertiesList(boardFileData.getItemsPropertiesList());
        this.setLinkDataList(boardFileData.getLinkDataList());
    }

    public GameSession() {
    }

    @Override
    public String toString() {
        return String.format(
                "GameSession[id=%s, firstName='%s', lastName='%s']",
                id, firstName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
