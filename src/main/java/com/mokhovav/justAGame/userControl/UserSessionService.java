package com.mokhovav.justAGame.userControl;

import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.justAGame.authentication.database.DAOService;
import com.mokhovav.justAGame.authentication.user.User;
import org.springframework.stereotype.Service;

@Service
public class UserSessionService {
    private final DAOService daoService;

    public UserSessionService(DAOService daoService) {
        this.daoService = daoService;
    }

    public UserSession getByUser(User user) {
        return user == null ? null : (UserSession) daoService.findObject("from UserSession where user_id = '" + user.getId() + "'");
    }

    public void save(User user) throws ValidException {
        UserSession userSession = getByUser(user);
        if (userSession != null) throw new ValidException("This user is already playing in this game");
        userSession = new UserSession(user);
        daoService.save(userSession);
    }
    public Class getClassByName(String name) {
        try {
            String className = "com.mokhovav.justAGame.";
            if (name.length() >= 3) {
                name = name.substring(1, name.length() - 1);
                className += name + '.';
                className += name.substring(0, 1).toUpperCase();
                if (name.length() > 1) className += name.substring(1);
            }
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public void setStatus(User user, Status status) throws ValidException {
        UserSession userSession = getByUser(user);
        if (userSession == null) throw new ValidException("User sessions not found");
        userSession.setStatus(status);
        daoService.update(userSession);
    }

    public Status getStatus(User user) throws ValidException {
        UserSession userSession = getByUser(user);
        if (userSession == null) throw new ValidException("User sessions not found");
        return userSession.getStatus();
    }

    public void setGameSession(User user, Long gameSessionId) throws ValidException {
        UserSession userSession = getByUser(user);
        if (userSession == null) throw new ValidException("User sessions not found");
        userSession.setSessionId(gameSessionId);
        daoService.update(userSession);
    }

}
