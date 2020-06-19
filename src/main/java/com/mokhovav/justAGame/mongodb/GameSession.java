package com.mokhovav.justAGame.mongodb;

import com.mokhovav.inspiration.board.BoardFileData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "game_sessions")
public class GameSession extends BoardFileData {
    @Id
    private String id;
    // userID -> Item
    private Map<Long, String> users;
    private int boats;

    public GameSession(BoardFileData boardFileData, Map<Long, String> users) {
        this.users = users;
        this.setDiceList(boardFileData.getDiceList());
        this.setFieldList(boardFileData.getFieldList());
        this.setFieldsPropertiesList(boardFileData.getFieldsPropertiesList());
        this.setItemsList(boardFileData.getItemsList());
        this.setItemsPropertiesList(boardFileData.getItemsPropertiesList());
        this.setLinkDataList(boardFileData.getLinkDataList());
    }

    public GameSession() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<Long, String> getUsers() {
        return users;
    }

    public void setUsers(Map<Long, String> users) {
        this.users = users;
    }

    public int getBoats() {
        return boats;
    }

    public void setBoats(int boats) {
        this.boats = boats;
    }
}
