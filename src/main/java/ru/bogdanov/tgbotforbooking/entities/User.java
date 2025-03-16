package ru.bogdanov.tgbotforbooking.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users", schema = "public")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "tg_account")
    private String tgAccount;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "tg_user_id")
    private Long tgUserId;

    @Column(name = "notifications_on")
    private Boolean notificationsOn = true;

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

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public Long getTgUserId() {
        return tgUserId;
    }

    public void setTgUserId(Long tgUserId) {
        this.tgUserId = tgUserId;
    }

    public Boolean getNotificationsOn() {
        return notificationsOn;
    }

    public void setNotificationsOn(Boolean notificationsOn) {
        this.notificationsOn = notificationsOn;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", tgAccount='" + tgAccount + '\'' +
                ", chatId='" + chatId + '\'' +
                ", tgUserId='" + tgUserId + '\'' +
                ", notificationsOn=" + notificationsOn +
                '}';
    }
}
