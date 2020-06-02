package com.mokhovav.justAGame.authentication.database;

import com.mokhovav.justAGame.authentication.authority.Authority;
import com.mokhovav.justAGame.authentication.user.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class HibernateConfig {

    private static SessionFactory sessionFactory;
    public HibernateConfig() {
    }

    @Bean
    public static SessionFactory getSessionFactory() throws Exception {
        try {
            if (sessionFactory == null) {
                Configuration configuration = new Configuration();
                configuration.configure("hibernate.xml");
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Authority.class);
                sessionFactory = configuration.buildSessionFactory();
            }
        }catch(Exception e){
            System.out.println("HibernateConfig: "+ e.getMessage());
        }
        return sessionFactory;
    }
}
