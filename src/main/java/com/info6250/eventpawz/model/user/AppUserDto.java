package com.info6250.eventpawz.model.user;

import lombok.Data;

@Data
public class AppUserDto {

    private Long id;
    private String username;
    private String email;
    private String name;
    private Role role;
    private String avatar;
    private boolean enabled;
}
