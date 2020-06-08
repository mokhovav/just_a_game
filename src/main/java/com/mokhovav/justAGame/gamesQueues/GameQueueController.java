package com.mokhovav.justAGame.gamesQueues;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.justAGame.authentication.user.User;
import org.springframework.beans.factory.support.ManagedMap;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Component
public class GameQueueController {

    @Async
    @EventListener//(condition = "#event.name eq 'littleCircuit'")
    @Tracking
    public void handleGameQueueEvent(GameQueueEvent event) throws ValidException {
        System.out.println("Queue size : " + ((GameQueue)event.getObject()).getSize() + "  Game: " + event.getName());

        return;
    }



}
