package com.info6250.eventpawz.model.user;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
        CriteriaQuery<AppUser> criteriaQuery = sessionFactory.getCurrentSession().getCriteriaBuilder().createQuery(AppUser.class);
        Root<AppUser> root = criteriaQuery.from(AppUser.class);
        criteriaQuery.select(root);

        return sessionFactory.getCurrentSession().createQuery(criteriaQuery).getResultList();
    }

    public AppUser findByUsername(String username) {
        return sessionFactory.getCurrentSession().get(AppUser.class, username);
    }

    public AppUser findByEmail(String email) {
        return sessionFactory.getCurrentSession().get(AppUser.class, email);
    }

    //Update user
    public void update(AppUser appUser) {
        sessionFactory.getCurrentSession().update(appUser);
    }

    //Delete user
    public void delete(AppUser appUser) {
        sessionFactory.getCurrentSession().delete(appUser);
    }


}
