package com.mokhovav.justAGame.mongodb;

import com.mokhovav.inspiration.board.BoardFileData;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "game_sessions")
public class GameSession extends BoardFileData {
    @Id
    private long id;
}
