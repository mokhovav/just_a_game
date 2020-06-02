package com.mokhovav.justAGame.authentication.authority;

import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.justAGame.authentication.database.DAOService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService {

    private final DAOService daoService;
    private final AuthorityValid authorityValid;

    public AuthorityService(DAOService daoService, AuthorityValid authorityValid) {
        this.daoService = daoService;
        this.authorityValid = authorityValid;

    }

    public boolean isExist(String name) {
        return !name.isEmpty() && (Authority) daoService.findObject("from Authority where authority='" + name + "'") != null;
    }

    public boolean isExist(Authority authority){
        return authority != null && isExist(authority.getAuthority());
    }

    public boolean isExist(Long id) {
        return getById(id) != null;
    }

    public void save(Authority authority) throws ValidException {
        if(authorityValid.nullOrEmpty(authority)) throw new ValidException("Authority should not be empty");
        if(isExist(authority)) throw new ValidException("Authority is already exist");
        daoService.save(authority);
    }

    public void update(Authority authority) throws ValidException {
        if (authorityValid.nullOrEmpty(authority)) throw new ValidException("Authority should not be empty");
        Authority authorityObj = getById(authority.getId());
        if (authorityObj == null) throw new ValidException("Authority does not exist");
        daoService.update(authority);
    }

    public boolean delete(Long id) throws ValidException {
        Authority authority = getById(id);
        if(authority == null ) throw new ValidException("Authority does not exist");
        return daoService.delete(authority);
    }

    public Authority getByAuthority(String name){
        return name.isEmpty()? null : (Authority) daoService.findObject("from Authority where authority='"+name+"'");
    }

    public Authority getById(Long id){
        return id > 0 ? (Authority) daoService.findObject("from BaseAuthority where id="+id) : null;
    }

    public List<Authority> getAll(){
        return (List<Authority>) daoService.findAll(Authority.class);
    }
}
