package com.mokhovav.justAGame.userControl;

import com.mokhovav.base_spring_boot_project.baseClasses.BaseValid;
import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.justAGame.authentication.database.DAOService;
import com.mokhovav.justAGame.authentication.user.User;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class UserSessionService {
    private final DAOService daoService;
    private final BaseValid baseValid;

    public UserSessionService(DAOService daoService, BaseValid baseValid) {
        this.daoService = daoService;
        this.baseValid = baseValid;
    }

    public UserSession getByUser(User user) {
        return user == null ? null : (UserSession) daoService.findObject("from UserSession where user_id = '" + user.getId() + "'");
    }

    public UserSession getByUser(Long id) {
        return id <= 0 ? null : (UserSession) daoService.findObject("from UserSession where user_id = '" + id + "'");
    }

    public UserSession getBySecretId(String secretId) {
        return baseValid.nullOrEmpty(secretId) ? null : (UserSession) daoService.findObject("from UserSession where user_secret_id = '" + secretId + "'");
    }

    public UserSession save(User user, Status status) throws ValidException {
        UserSession userSession = getByUser(user);
        if (userSession != null) {
            Status userStatus = userSession.getStatus();
            if (userStatus == Status.PLAYING || userStatus == Status.WAITING || userStatus == Status.WAITSOCKET)
                throw new ValidException("This user is already playing in this game");
            if (userStatus == Status.LOST) {
                throw new ValidException("This user is lost");
            }
        }
        userSession = new UserSession(user, status);
        daoService.save(userSession);
        return userSession;
    }

    public void delete (UserSession userSession){
        daoService.delete(userSession);
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

    public void setStatus(long userId, Status status) throws ValidException {
        UserSession userSession = getByUser(userId);
        if (userSession == null) throw new ValidException("User sessions not found");
        userSession.setStatus(status);
        daoService.update(userSession);
    }


    public Status getStatus(User user) throws ValidException {
        UserSession userSession = getByUser(user);
        if (userSession == null) throw new ValidException("User sessions not found");
        return userSession.getStatus();
    }

    public Timestamp getStatusTime(User user) throws ValidException {
        UserSession userSession = getByUser(user);
        if (userSession == null) throw new ValidException("User sessions not found");
        return userSession.getStatusTime();
    }

    public void setGameSession(User user, String gameSessionId) throws ValidException {
        UserSession userSession = getByUser(user);
        if (userSession == null) throw new ValidException("User sessions not found");
        userSession.setSessionId(gameSessionId);
        daoService.update(userSession);
    }

}
