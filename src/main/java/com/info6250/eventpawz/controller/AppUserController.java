package com.info6250.eventpawz.controller;

import com.info6250.eventpawz.model.user.AppUserDao;
import com.info6250.eventpawz.model.user.AppUserDto;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//Create a new controller class called AppUserController
//Add the @RestController annotation to the class
//Create methods for CRUD operations

@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {
    private final AppUserDao appUserDao;

    private final ModelMapper modelMapper;

    public AppUserController(AppUserDao appUserDao, ModelMapper modelMapper) {
        this.appUserDao = appUserDao;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<AppUserDto> getAllAppUsers() {
        return appUserDao.findAll().stream().map(appUser -> modelMapper.map(appUser, AppUserDto.class)).toList();
    }

    // ! Commented out because new users should be added via the register API
    /*
    @PostMapping
    public void CreateAppUser(@RequestBody AppUser appUser) {
        appUserDao.save(appUser);
    }
    */


}
