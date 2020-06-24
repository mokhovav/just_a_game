package com.mokhovav.justAGame.webSockets.gameMessage;

import org.springframework.stereotype.Component;

@Component
public class GameMessage {
    private String command;
    private String message;

    public GameMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
