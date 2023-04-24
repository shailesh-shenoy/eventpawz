package com.info6250.eventpawz.controller;

import com.info6250.eventpawz.model.auth.PasswordChangeRequest;
import com.info6250.eventpawz.model.event.EventDao;
import com.info6250.eventpawz.model.event.EventDto;
import com.info6250.eventpawz.model.event.EventTypeDao;
import com.info6250.eventpawz.model.user.AppUserDao;
import com.info6250.eventpawz.model.user.AppUserDto;
import com.info6250.eventpawz.model.user.FileUploadResponse;
import com.info6250.eventpawz.service.auth.AuthenticationService;
import com.info6250.eventpawz.service.event.EventService;
import com.info6250.eventpawz.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

//Create a new controller class called AppUserController
//Add the @RestController annotation to the class
//Create methods for CRUD operations

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {
    private final AppUserDao appUserDao;

    private final EventDao eventDao;

    private final EventTypeDao eventTypeDao;

    private final ModelMapper modelMapper;

    private final AuthenticationService authenticationService;

    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<AppUserDto>> getAllAppUsers() {
        return ResponseEntity.ok(appUserDao.findAll().stream().map(appUser -> modelMapper.map(appUser, AppUserDto.class)).toList());
    }

    // *Get AppUser by id
    @GetMapping("/{id}")
    public ResponseEntity<AppUserDto> getAppUserById(@PathVariable("id") Long id) {
        return appUserDao.findById(id).map(appUser -> ResponseEntity.ok(modelMapper.map(appUser, AppUserDto.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AppUserDto> updateAppUser(@PathVariable("id") Long id, @RequestBody AppUserDto appUserDto, Authentication authentication) {
        if (!authenticationService.isSiteAdmin(authentication) && !authenticationService.isCurrentUser(authentication, id)) {
            return ResponseEntity.status(403).build();
        }
        return appUserDao.findById(id).map(appUser -> {
            appUser = appUserDao.update(appUser, appUserDto);
            return ResponseEntity.ok(modelMapper.map(appUser, AppUserDto.class));
        }).orElse(ResponseEntity.notFound().build());
    }


    @PutMapping("/{id}/avatar")
    public ResponseEntity<FileUploadResponse> uploadAvatar(@PathVariable("id") Long id, @RequestParam("file") MultipartFile multipartFile) throws IOException {
        var appUser = appUserDao.findById(id);
        if (appUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String originalFileName = multipartFile.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        String fileName = "images/" + appUser.get().getUsername() + "/avatar_" + id + "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String filePath = FileUploadUtil.saveFile(fileName, multipartFile);

        appUser.get().setAvatar(fileName);
        appUserDao.update(appUser.get());

        return ResponseEntity.ok(new FileUploadResponse(fileName, filePath, multipartFile.getSize()));
    }

    @PutMapping("/{id}/password") // *Change password
    public ResponseEntity<AppUserDto> changePassword(@PathVariable("id") Long id, @RequestBody PasswordChangeRequest passwordChangeRequest) {
        var appUser = appUserDao.findById(id);
        if (appUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        appUser = java.util.Optional.ofNullable(authenticationService.changePassword(appUser.get(), passwordChangeRequest));
        return appUser.map(user -> ResponseEntity.ok(modelMapper.map(user, AppUserDto.class))).orElse(ResponseEntity.badRequest().build());
    }

    @GetMapping("/{id}/events")
    public ResponseEntity<List<EventDto>> getEventsByAppUser(@PathVariable("id") Long id) {
        var appUser = appUserDao.findById(id);
        if (appUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ;
        return ResponseEntity.ok(eventService.getUserEvents(appUser.get()));

    }

    @PostMapping("/{id}/events")
    public ResponseEntity<EventDto> createEvent(@PathVariable("id") Long id, @RequestBody EventDto eventDto, Authentication authentication) {
        if (!authenticationService.isSiteAdmin(authentication) && !authenticationService.isCurrentUser(authentication, id)) {
            return ResponseEntity.status(403).build();
        }

        var appUser = appUserDao.findById(id);
        if (appUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        String eventTypeInput = "";
        if (eventDto.getEventType() == null || eventDto.getEventType().getType() == null || eventDto.getEventType().getType().isBlank()) {
            eventTypeInput = "Other";
        } else {
            eventTypeInput = eventDto.getEventType().getType();
        }
        var eventType = eventTypeDao.findByType(eventTypeInput).orElseGet(() -> eventTypeDao.findByType("Other").orElseGet(() -> null));
        return ResponseEntity.ok(eventService.createEventForUser(appUser.get(), eventDto, eventType));
    }

    @PatchMapping("/{id}/events/{eventId}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable("id") Long userId, @PathVariable("eventId") Long eventId, @RequestBody EventDto eventDto, Authentication authentication) {
        if (!authenticationService.isSiteAdmin(authentication) && !authenticationService.isCurrentUser(authentication, userId)) {
            return ResponseEntity.status(403).build();
        }
        var appUser = appUserDao.findById(userId);
        if (appUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var event = eventDao.findById(eventId);
        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        if (!Objects.equals(event.get().getCreatedBy().getId(), userId)) {
            return ResponseEntity.status(403).build();
        }
        String eventTypeInput = "";
        if (eventDto.getEventType() != null && eventDto.getEventType().getType() != null && !eventDto.getEventType().getType().isBlank()) {
            eventTypeInput = eventDto.getEventType().getType();
        }
        var eventType = eventTypeDao.findByType(eventTypeInput).orElseGet(() -> null);
        return ResponseEntity.ok(eventService.updateEventForUser(appUser.get(), event.get(), eventType, eventDto));
    }

    @PutMapping("/{id}/events/{eventId}/cover")
    public ResponseEntity<FileUploadResponse> uploadEventCover(@PathVariable("id") Long userId, @PathVariable("eventId") Long eventId, @RequestParam("file") MultipartFile multipartFile, Authentication authentication) throws IOException {
        if (!authenticationService.isSiteAdmin(authentication) && !authenticationService.isCurrentUser(authentication, userId)) {
            return ResponseEntity.status(403).build();
        }
        var appUser = appUserDao.findById(userId);
        if (appUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var event = eventDao.findById(eventId);
        if (event.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        if (!Objects.equals(event.get().getCreatedBy().getId(), userId)) {
            return ResponseEntity.status(403).build();
        }
        if (multipartFile == null || multipartFile.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        String originalFileName = multipartFile.getOriginalFilename();
        if (originalFileName == null || originalFileName.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        String fileName = "images/" + appUser.get().getUsername() + "/event_" + eventId + "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
        String filePath = FileUploadUtil.saveFile(fileName, multipartFile);

        event.get().setCoverImage(fileName);
        eventDao.update(event.get());

        return ResponseEntity.ok(new FileUploadResponse(fileName, filePath, multipartFile.getSize()));
    }

    // ! Commented out because new users should be added via the register API
    /*
    @PostMapping
    public void CreateAppUser(@RequestBody AppUser appUser) {
        appUserDao.save(appUser);
    }
    */


}
