package com.info6250.eventpawz.controller;

import com.info6250.eventpawz.model.user.AppUser;
import com.info6250.eventpawz.model.user.AppUserDao;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Create a new controller class called AppUserController
//Add the @RestController annotation to the class
//Create methods for CRUD operations

@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {
    private final AppUserDao appUserDao;

    public AppUserController(AppUserDao appUserDao) {
        this.appUserDao = appUserDao;
    }

    @GetMapping
    public List<AppUser> getAllAppUsers() {
        return appUserDao.findAll();
    }

    @PostMapping
    public void CreateAppUser(@RequestBody AppUser appUser) {
        appUserDao.save(appUser);
    }


}
