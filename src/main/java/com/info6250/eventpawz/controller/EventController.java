package com.info6250.eventpawz.controller;

import com.info6250.eventpawz.model.event.EventDao;
import com.info6250.eventpawz.model.event.EventDto;
import com.info6250.eventpawz.model.event.EventRegisterDto;
import com.info6250.eventpawz.model.user.AppUserDao;
import com.info6250.eventpawz.service.auth.AuthenticationService;
import com.info6250.eventpawz.service.event.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
    private final EventService eventService;
    private final EventDao eventDao;
    private final AuthenticationService authenticationService;
    private final AppUserDao appUserDao;

    // *Get all events
    @GetMapping
    public ResponseEntity<List<EventDto>> getAllEvents() {
        return ResponseEntity.ok(eventService.getAllEvents());
    }

    // *Get event detail
    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable("id") Long id) {
        return eventService.getEventById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/register")
    public ResponseEntity<EventDto> registerEvent(@PathVariable("id") Long id, @RequestBody EventRegisterDto eventRegisterDto, Authentication authentication) {
        Long userId = eventRegisterDto.getUserId();
        if (!authenticationService.isSiteAdmin(authentication) && !authenticationService.isCurrentUser(authentication, userId)) {
            return ResponseEntity.status(403).build();
        }
        var event = eventDao.findById(id);
        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var appUser = appUserDao.findById(userId);
        return appUser.map(user -> ResponseEntity.ok(eventService.registerUserForEvent(user, event.get()))).orElseGet(() -> ResponseEntity.badRequest().build());
    }

}
