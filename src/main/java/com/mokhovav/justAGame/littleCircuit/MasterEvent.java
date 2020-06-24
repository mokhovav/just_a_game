package com.mokhovav.justAGame.littleCircuit;

import org.springframework.context.ApplicationEvent;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashMap;
import java.util.Map;

public class MasterEvent extends ApplicationEvent {
    private String name;
    private Long userId;
    private Map<Long, WebSocketSession> establishedSessionsLong;
    private String command;
    //private WebSocketSession socket;

    public MasterEvent(Object source, Long userId, String command, Map<Long, WebSocketSession> establishedSessionsLong) {
        super(source);
        this.name = name;
        this.userId = userId;
        this.command = command;
        this.establishedSessionsLong = establishedSessionsLong;
    }
    public String getName() {
        return name;
    }

    public Object getObject(){
        return source;
    }

    public Long getUserId() {
        return userId;
    }

    public Map<Long, WebSocketSession> getEstablishedSessionsLong() {
        return establishedSessionsLong;
    }

    public String getCommand() {
        return command;
    }
}
