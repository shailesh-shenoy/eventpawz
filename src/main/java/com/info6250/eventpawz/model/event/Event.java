package com.info6250.eventpawz.model.event;

import com.info6250.eventpawz.model.user.AppUser;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

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

    @ManyToOne(fetch = FetchType.EAGER)
    private AppUser createdBy;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "event_attendees",
            joinColumns = @JoinColumn(name = "event_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<AppUser> attendees;
}
