package com.info6250.eventpawz.model.event;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.info6250.eventpawz.model.user.AppUserDto;
import lombok.*;

import java.util.Set;

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

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnoreProperties({"createdEvents", "attendedEvents"})
    private AppUserDto createdBy;

    private EventTypeDto eventType;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonIgnoreProperties({"createdEvents", "attendedEvents"})
    private Set<AppUserDto> attendees;
}
