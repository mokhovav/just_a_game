package com.mokhovav.justAGame.mongodb;

import com.mokhovav.inspiration.board.BoardFileData;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "game_sessions")
public class GameSession extends BoardFileData {
    @Id
    private String id;
    // userID -> Item
    private Map<Long, String> users;
    // moveUserNumb -> userID
    private long[] usersId;
    private int boats;
    private int moveUserNumb;
    private long finisherId;

    public GameSession(BoardFileData boardFileData, Map<Long, String> users, long[] usersId){
        setBoardFileData(boardFileData);
        this.users = users;
        this.usersId = usersId;
        this.finisherId = 0;
    }

    public void setBoardFileData(BoardFileData boardFileData){
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

    public int getMoveUserNumb() {
        return moveUserNumb;
    }

    public long getMoveUserId() {
        return usersId[moveUserNumb];
    }

    public void setNextUserNumb() {
        if (moveUserNumb == usersId.length - 1) moveUserNumb = 0;
        else moveUserNumb++;
    }

    public void setMoveUserNumb(int moveUserNumb) {
        this.moveUserNumb = moveUserNumb;
    }

    public long[] getUsersId() {
        return usersId;
    }

    public void setUsersId(long[] usersId) {
        this.usersId = usersId;
    }

    public long getFinisherId() {
        return finisherId;
    }

    public void setFinisherId(long finisherId) {
        this.finisherId = finisherId;
    }
}
