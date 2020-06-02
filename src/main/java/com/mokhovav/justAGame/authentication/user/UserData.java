package com.mokhovav.justAGame.authentication.user;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.mokhovav.base_spring_boot_project.baseClasses.BaseEntity;

import com.mokhovav.justAGame.authentication.authority.Authority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;


@MappedSuperclass
public class UserData extends BaseEntity implements UserDetails {
    public UserData() {
    }
    @Column(name = "name")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    // Create a simple table
    @ManyToMany(targetEntity = Authority.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "users_authorities", joinColumns = @JoinColumn(name = "users_id"))
    private Set<Authority> authorities;

    @Column(name = "active")
    @JsonIgnore
    private boolean active;

    @Transient
    private String signPwd;

    @Transient
    private String confirmSignPwd;


    @Override
    public Set<Authority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive();
    }


    public boolean isAdmin(){
        Set<Authority> baseAuthorities = getAuthorities();
        for (Authority baseAuthority : baseAuthorities) {
            if (baseAuthority.getAuthority().equals("admin")) return true;
        }
        return false;
    }

    public boolean isActive() {
        return active;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getSignPwd() {
        return signPwd;
    }

    public void setSignPwd(String signPwd) {
        this.signPwd = signPwd;
    }

    public String getConfirmSignPwd() {
        return confirmSignPwd;
    }

    public void setConfirmSignPwd(String confirmSignPwd) {
        this.confirmSignPwd = confirmSignPwd;
    }


}
