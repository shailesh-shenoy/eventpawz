package com.info6250.eventpawz.model.user;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

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

    public Optional<AppUser> findByUsername(String username) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<AppUser> criteriaQuery = criteriaBuilder.createQuery(AppUser.class);
        Root<AppUser> root = criteriaQuery.from(AppUser.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("username"), username));
        return sessionFactory.getCurrentSession().createQuery(criteriaQuery).uniqueResultOptional();
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
