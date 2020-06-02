package com.mokhovav.justAGame.authentication.user;

import com.mokhovav.base_spring_boot_project.baseClasses.BaseValid;
import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.justAGame.authentication.authority.Authority;
import com.mokhovav.justAGame.authentication.database.DAOService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final BaseValid baseValid;
    private final DAOService daoService;
    private final PasswordEncoder passwordEncoder;

    public UserService(BaseValid baseValid, DAOService daoService, PasswordEncoder passwordEncoder) {
        this.baseValid = baseValid;
        this.daoService = daoService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return (UserDetails) getByUserName(username);   //need add UserDetails to User
    }

    public User getByUserName(String name) {
        return name.isEmpty() ? null : (User) daoService.findObject("from User where name='" + name + "'");
    }


    public User getById(Long id) {
        return id > 0 ? (User) daoService.findObject("from User where id=" + id) : null;
    }

    public boolean update(User user) throws ValidException {
        if (user == null || getById(user.getId()) == null || user.getUsername().isEmpty() || user.getPassword().isEmpty())
            throw new ValidException("User Data incorrect");
        daoService.update(user);
        return true;
    }

    public boolean isExist(String name) {
        return name != null && (!name.isEmpty() && ((User) daoService.findObject("from User where name='" + name + "'")) != null);
    }

    public boolean isExist(User user) {
        return user != null && isExist(user.getUsername());
    }

    public boolean isExist(Long id) {
        return getById(id) != null;
    }

    public void save(User user) throws ValidException {
        if (isExist(user)) throw new ValidException("This user already exists");
        daoService.save(user);
    }

    public boolean isHaveAuthority(User user, Authority authority) {
        if (user != null && authority != null)
            for (Authority r : user.getAuthorities())
                if (r.getAuthority().equals(authority.getAuthority()))
                    return true;
        return false;
    }

    public void userDataCheck(UserData userData) throws ValidException {
        if (userData == null || baseValid.nullOrEmpty(userData.getUsername())) throw new ValidException("User name incorrect");
        passwordsCheck(userData.getSignPwd(), userData.getConfirmSignPwd());
    }

    private void passwordsCheck(String password, String confirm) throws ValidException {
        if (baseValid.nullOrEmpty(password)) throw new ValidException("Password incorrect");
        if (!password.equals(confirm)) throw new ValidException("Passwords do not match");
    }

    public User createUser(UserData userData) throws ValidException {
        userDataCheck(userData);
        User user = new User();
        user.setUsername(userData.getUsername());
        user.setPassword(passwordEncoder.encode(userData.getSignPwd()));
        return user;
    }


}
