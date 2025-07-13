package ru.bogdanov.tgbotforbooking.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits", schema = "public")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "visits_seq")
    @SequenceGenerator(name = "visits_seq", sequenceName = "visits_seq", schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "visit_datetime")
    private LocalDateTime visitDateTime;

    @Column(name = "end_visit_datetime")
    private LocalDateTime endVisitDateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "google_event_id")
    private String googleEventId;

    @OneToOne
    @JoinColumn(name = "service_id")
    private CosmetologyService cosmetologyService;

    @OneToOne(mappedBy = "visit", cascade = CascadeType.ALL)
    private Notification notification;

    public Visit() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getVisitDateTime() {
        return visitDateTime;
    }

    public void setVisitDateTime(LocalDateTime visitDateTime) {
        this.visitDateTime = visitDateTime;
    }

    public LocalDateTime getEndVisitDateTime() {
        return endVisitDateTime;
    }

    public void setEndVisitDateTime(LocalDateTime endVisitDateTime) {
        this.endVisitDateTime = endVisitDateTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getGoogleEventId() {
        return googleEventId;
    }

    public void setGoogleEventId(String googleEventId) {
        this.googleEventId = googleEventId;
    }

    public CosmetologyService getCosmetologyService() {
        return cosmetologyService;
    }

    public void setCosmetologyService(CosmetologyService cosmetologyService) {
        this.cosmetologyService = cosmetologyService;
    }

    public Notification getNotification() {
        return notification;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", visitDateTime=" + visitDateTime +
                ", endVisitDateTime=" + endVisitDateTime +
                ", user=" + user +
                ", googleEventId='" + googleEventId + '\'' +
                ", cosmetologyService=" + cosmetologyService +
                ", notification=" + notification +
                '}';
    }
}
