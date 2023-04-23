package com.info6250.eventpawz.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.info6250.eventpawz.model.user.AppUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long eventId;

    private String eventName;

    private String description;

    private String coverImage;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private AppUserDto createdBy;
    
    private EventTypeDto eventType;
}
