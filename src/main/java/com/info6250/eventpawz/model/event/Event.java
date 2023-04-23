package com.info6250.eventpawz.model.event;

import com.info6250.eventpawz.model.user.AppUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventId;

    @Column(unique = true, nullable = false)
    private String eventName;

    @Column
    private String description;

    @Column
    private String coverImage;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private EventType eventType;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private AppUser createdBy;
}
