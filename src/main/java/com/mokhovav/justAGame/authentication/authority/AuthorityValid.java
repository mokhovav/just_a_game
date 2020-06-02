package com.mokhovav.justAGame.authentication.authority;

import com.mokhovav.base_spring_boot_project.baseClasses.BaseValid;
import org.springframework.stereotype.Service;

@Service
public class AuthorityValid extends BaseValid {
    public boolean nullOrEmpty(Authority authority){
        return authority==null || nullOrEmpty(authority.getAuthority());
    }
}
