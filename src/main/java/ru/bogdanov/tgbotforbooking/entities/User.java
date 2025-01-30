package ru.bogdanov.tgbotforbooking.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "tg_account")
    private String tgAccount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Visit> visits;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTgAccount() {
        return tgAccount;
    }

    public void setTgAccount(String tgAccount) {
        this.tgAccount = tgAccount;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", tgAccount='" + tgAccount + '\'' +
                '}';
    }
}
