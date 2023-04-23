package com.info6250.eventpawz.service.event;

import com.info6250.eventpawz.model.event.*;
import com.info6250.eventpawz.model.user.AppUser;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional
public class EventService {

    private final EventDao eventDao;
    private final EventTypeDao eventTypeDao;
    private final ModelMapper modelMapper;
    private final SessionFactory sessionFactory;

    public Event createEventForUser(AppUser appUser, EventDto eventDto, EventType eventType) {

        Event event = Event.builder()
                .eventName(eventDto.getEventName())
                .description(eventDto.getDescription())
                .coverImage(eventDto.getCoverImage())
                .eventType(eventType)
                .createdBy(appUser)
                .build();
        eventDao.save(event);
        return event;
    }

    public List<EventDto> getAllEvents() {
        return eventDao.findAll().stream().map(event -> modelMapper.map(event, EventDto.class)).toList();
    }

    public List<EventDto> getUserEvents(AppUser appUser) {
        sessionFactory.getCurrentSession().refresh(appUser);
        Hibernate.initialize(appUser.getEvents());
        return appUser.getEvents().stream().map(event -> modelMapper.map(event, EventDto.class)).toList();
    }
}
