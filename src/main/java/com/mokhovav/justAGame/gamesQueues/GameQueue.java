package com.mokhovav.justAGame.gamesQueues;

import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.justAGame.authentication.user.User;
import com.mokhovav.justAGame.games.Game;
import com.mokhovav.justAGame.userControl.Status;
import com.mokhovav.justAGame.userControl.UserSessionService;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

@Service
public class GameQueue {
    private Game game;
    private Queue<User> waitingList;
    private final UserSessionService userSessionService;

    public GameQueue(UserSessionService userSessionService) {
        this.userSessionService = userSessionService;
        waitingList = new ArrayBlockingQueue<>(10000);
    }

    public void addUser(User user) throws ValidException {
       waitingList.add(user);
    }

    public User getUser() throws ValidException {
        return waitingList.poll();
    }

    public int getSize(){
        return waitingList.size();
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }
}
