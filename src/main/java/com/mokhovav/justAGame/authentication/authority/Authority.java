package com.mokhovav.justAGame.authentication.authority;


import com.mokhovav.base_spring_boot_project.baseClasses.BaseEntity;
import com.mokhovav.justAGame.authentication.user.User;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Entity
@Table(name = "authorities")
public class Authority extends BaseEntity implements GrantedAuthority {
    public Authority() {
    }
    public Authority(String authority) {
        setAuthority(authority);
    }

    @NotBlank(message = "Authority cannot be empty")
    @Column(name = "authority")
    private String authority;

    @ManyToMany(mappedBy = "authorities")         // Bidirectional communication
    private Set<User> users;

    @Override
    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}
