package com.mokhovav.justAGame.userControl;

import com.mokhovav.base_spring_boot_project.baseClasses.BaseEntity;
import com.mokhovav.justAGame.authentication.user.User;

import javax.persistence.*;

@Entity
@Table(name = "user_sessions")
public class UserSession extends BaseEntity{

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User user;
    private Status status;
    @Column(name = "is_send")
    private boolean isSend;
    @Column (name = "session_id")
    private String sessionId;

    public UserSession() {
    }

    public UserSession(User user) {
        this.user = user;
        this.status = Status.WAITING;
        this.isSend = false;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
