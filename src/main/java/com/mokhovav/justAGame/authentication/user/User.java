package com.mokhovav.justAGame.authentication.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User extends UserData {

    public User() {
        setActive(true);
        setChangePassword(true);
    }

    public User(
            @NotBlank(message = "Username cannot be empty") String userName,
            @NotBlank(message = "Password cannot be empty") String password
    ) {
        setUsername(userName);
        this.password = password;
        setActive(true);
        setChangePassword(true);
    }

    @NotBlank(message = "Password cannot be empty")
    @Column(name = "password")
    private String password;
    @Column(name = "change_password")
    private boolean changePassword;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isChangePassword() {
        return changePassword;
    }

    public void setChangePassword(boolean changePassword) {
        this.changePassword = changePassword;
    }

}
