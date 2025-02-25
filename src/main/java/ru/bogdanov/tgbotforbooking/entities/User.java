package ru.bogdanov.tgbotforbooking.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTgAccount() {
        return tgAccount;
    }

    public void setTgAccount(String tgAccount) {
        this.tgAccount = tgAccount;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", tgAccount='" + tgAccount + '\'' +
                '}';
    }
}
