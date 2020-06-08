package com.mokhovav.justAGame.games;

import com.mokhovav.base_spring_boot_project.baseClasses.BaseValid;
import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.justAGame.authentication.database.DAOService;
import org.springframework.stereotype.Service;

@Service
public class GameService {
    private final DAOService daoService;
    private final BaseValid baseValid;

    public GameService(DAOService daoService, BaseValid baseValid) {
        this.daoService = daoService;
        this.baseValid = baseValid;
    }

    public Game getByName(String name) {
        return baseValid.nullOrEmpty(name) ? null : (Game) daoService.findObject("from Game where name = '" + name + "'");
    }

    public void save(Game game) throws ValidException {
        Game gameSession = getByName(game.getName());
        if (gameSession != null) throw new ValidException("This game is already exist");
        daoService.save(game);
    }
}
