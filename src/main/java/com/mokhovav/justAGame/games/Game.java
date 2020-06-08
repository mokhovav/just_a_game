package com.mokhovav.justAGame.games;

import com.mokhovav.base_spring_boot_project.baseClasses.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table (name = "games")
public class Game extends BaseEntity {
    @Column(unique = true)
    private String name;

    @Column(name = "max_players")
    private int maxPlayers;
    @Column(name = "waiting_time")
    private int waitingTime;
    @Column(name = "using_bots")
    private boolean usingBots;

    public Game() {
    }

    public Game(String name, int maxPlayers, int waitingTime, boolean usingBots) {
        this.name = name;
        this.maxPlayers = maxPlayers;
        this.waitingTime = waitingTime;
        this.usingBots = usingBots;
    }

    public String getName() {
        return name;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public boolean isUsingBots() {
        return usingBots;
    }
}
