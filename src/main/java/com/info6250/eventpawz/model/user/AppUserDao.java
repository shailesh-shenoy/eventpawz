package com.info6250.eventpawz.model.user;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class AppUserDao {

    private final SessionFactory sessionFactory;

    @Autowired
    public AppUserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(AppUser appUser) {
        sessionFactory.getCurrentSession().save(appUser);
    }

    public List<AppUser> findAll() {
        return sessionFactory.getCurrentSession().createQuery("from AppUser").list();
    }

    public AppUser findByUsername(String username) {
        return sessionFactory.getCurrentSession().get(AppUser.class, username);
    }

}
