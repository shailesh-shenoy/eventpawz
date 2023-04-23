package com.info6250.eventpawz.model.user;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class AppUserDao {
    
    private final SessionFactory sessionFactory;


    public void save(AppUser appUser) {
        sessionFactory.getCurrentSession().save(appUser);
    }

    public Optional<AppUser> findById(Long id) {
        return sessionFactory.getCurrentSession().byId(AppUser.class).loadOptional(id);
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

    public Optional<AppUser> findByEmail(String email) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCurrentSession().getCriteriaBuilder();
        CriteriaQuery<AppUser> criteriaQuery = criteriaBuilder.createQuery(AppUser.class);
        Root<AppUser> root = criteriaQuery.from(AppUser.class);
        criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("email"), email));
        return sessionFactory.getCurrentSession().createQuery(criteriaQuery).uniqueResultOptional();
    }

    //Update user
    public AppUser update(AppUser appUser, AppUserDto appUserDto) {
        if (appUserDto.getEmail() != null && !appUserDto.getEmail().isBlank()) appUser.setEmail(appUserDto.getEmail());
        if (appUserDto.getName() != null && !appUserDto.getName().isBlank()) appUser.setName(appUserDto.getName());
        if (appUserDto.getAvatar() != null && !appUserDto.getAvatar().isBlank())
            appUser.setAvatar(appUserDto.getAvatar());
        if (appUserDto.getRole() != null) appUser.setRole(appUserDto.getRole());
        if (appUserDto.getEnabled() != null) appUser.setEnabled(appUserDto.getEnabled());
        sessionFactory.getCurrentSession().update(appUser);
        return appUser;
    }

    // *Update persistent user instance
    public AppUser update(AppUser appUser) {
        sessionFactory.getCurrentSession().update(appUser);
        return appUser;
    }

    //Delete user
    public void delete(AppUser appUser) {
        sessionFactory.getCurrentSession().delete(appUser);
    }


}
