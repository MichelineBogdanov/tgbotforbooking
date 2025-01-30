package ru.bogdanov.tgbotforbooking.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "visits", schema = "public")
public class Visit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "visit_datetime")
    private LocalDateTime visitDateTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "visit_id")
    private String visitId;

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

    public void setVisitDateTime(LocalDateTime name) {
        this.visitDateTime = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", visitDateTime=" + visitDateTime +
                ", user=" + user +
                ", visitId='" + visitId + '\'' +
                '}';
    }
}
