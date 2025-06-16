package ru.bogdanov.tgbotforbooking.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications", schema = "public")
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notifications_seq")
    @SequenceGenerator(name = "notifications_seq", sequenceName = "notifications_seq", schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "notification_datetime")
    private LocalDateTime notificationDateTime;

    @OneToOne
    @JoinColumn(name = "visit_id")
    private Visit visit;

    public Notification() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getNotificationDateTime() {
        return notificationDateTime;
    }

    public void setNotificationDateTime(LocalDateTime notificationDate) {
        this.notificationDateTime = notificationDate;
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }
}
