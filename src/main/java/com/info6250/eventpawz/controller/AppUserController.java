package com.info6250.eventpawz.controller;

import com.info6250.eventpawz.model.auth.PasswordChangeRequest;
import com.info6250.eventpawz.model.user.AppUserDao;
import com.info6250.eventpawz.model.user.AppUserDto;
import com.info6250.eventpawz.model.user.FileUploadResponse;
import com.info6250.eventpawz.service.auth.AuthenticationService;
import com.info6250.eventpawz.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

//Create a new controller class called AppUserController
//Add the @RestController annotation to the class
//Create methods for CRUD operations

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class AppUserController {
    private final AppUserDao appUserDao;

    private final ModelMapper modelMapper;

    private final AuthenticationService authenticationService;

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
    public ResponseEntity<AppUserDto> updateAppUser(@PathVariable("id") Long id, @RequestBody AppUserDto appUserDto) {
        System.out.println("Srsly");
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

    // ! Commented out because new users should be added via the register API
    /*
    @PostMapping
    public void CreateAppUser(@RequestBody AppUser appUser) {
        appUserDao.save(appUser);
    }
    */


}
