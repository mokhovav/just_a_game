package com.mokhovav.justAGame.userControl;

import com.mokhovav.base_spring_boot_project.baseClasses.BaseEntity;
import com.mokhovav.justAGame.authentication.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Random;

@Entity
@Table(name = "user_sessions")
public class UserSession extends BaseEntity{

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    private User user;
    private Status status;
    @Column (name = "session_id")
    private String sessionId;
    @Column (name = "user_secret_id")
    private String userSecretId;
    @Column (name = "status_time")
    @UpdateTimestamp
    private Timestamp statusTime;


    public UserSession() {
    }

    public UserSession(User user, Status status) {
        this.user = user;
        this.status = status;

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        userSecretId = (new BCryptPasswordEncoder (10)).encode(generatedString);
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

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserSecretId() {
        return userSecretId;
    }

    public void setUserSecretId(String userSecretId) {
        this.userSecretId = userSecretId;
    }

    public Timestamp getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(Timestamp statusTime) {
        this.statusTime = statusTime;
    }
}
