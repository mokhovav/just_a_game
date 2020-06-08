package com.mokhovav.justAGame.gamesQueues;

import org.springframework.context.ApplicationEvent;

public class GameQueueEvent extends ApplicationEvent {
    private String name;

    public GameQueueEvent(Object source, String name) {
        super(source);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Object getObject(){
        return source;
    }


}
