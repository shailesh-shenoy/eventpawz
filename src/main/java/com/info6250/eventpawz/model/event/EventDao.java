package com.info6250.eventpawz.model.event;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
@RequiredArgsConstructor
public class EventDao {
    private final SessionFactory sessionFactory;

    public void save(Event event) {
        sessionFactory.getCurrentSession().save(event);
    }

    public List<Event> findAll() {
        CriteriaQuery<Event> criteriaQuery = sessionFactory.getCurrentSession().getCriteriaBuilder().createQuery(Event.class);
        Root<Event> root = criteriaQuery.from(Event.class);
        criteriaQuery.select(root);

        return sessionFactory.getCurrentSession().createQuery(criteriaQuery).getResultList();
    }
}
