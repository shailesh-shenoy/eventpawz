package com.info6250.eventpawz.model.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long eventTypeId;

    private String type;
}
