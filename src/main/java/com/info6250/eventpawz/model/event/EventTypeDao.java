package com.info6250.eventpawz.model.event;

import lombok.RequiredArgsConstructor;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
@RequiredArgsConstructor
public class EventTypeDao {

    private final SessionFactory sessionFactory;

    public void save(EventType eventType) {
        sessionFactory.getCurrentSession().save(eventType);
    }

    public void update(EventType eventType) {
        sessionFactory.getCurrentSession().update(eventType);
    }

    public void delete(EventType eventType) {
        sessionFactory.getCurrentSession().delete(eventType);
    }

    public Optional<EventType> findById(Long id) {
        return sessionFactory.getCurrentSession().byId(EventType.class).loadOptional(id);
    }


    public Optional<EventType> findByType(String eventTypeInput) {
        return sessionFactory.getCurrentSession().createQuery("from EventType where type = :type", EventType.class)
                .setParameter("type", eventTypeInput)
                .uniqueResultOptional();
    }
}
