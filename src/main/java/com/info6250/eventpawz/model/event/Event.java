package com.info6250.eventpawz.model.event;

import com.info6250.eventpawz.model.user.AppUser;
import lombok.*;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @Column
    private LocalDate eventDate;

    @Column
    private LocalTime eventStartTime;

    @Column
    private LocalTime eventEndTime;

    @Formula(value = "(CASE WHEN (eventDate < current_date OR (eventDate = current_date AND eventEndTime < current_time)) THEN 'PAST' ELSE (CASE WHEN (eventDate = current_date AND eventStartTime <= current_time AND eventEndTime >= current_time) THEN 'ONGOING' ELSE 'UPCOMING' END) END)")
    private String status;

    @Column
    private String virtualMeetLink;

    @Column
    private String address;

    @Column
    private String city;

    @Column
    private String state;

    @Column
    private String zipCode;

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
