package com.info6250.eventpawz.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AppUserDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String username;

    private String email;
    private String name;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Role role;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String avatar;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Boolean enabled;
}
