package ru.bogdanov.tgbotforbooking.entities;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "services", schema = "public")
public class CosmetologyService implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "services_seq")
    @SequenceGenerator(name = "services_seq", sequenceName = "services_seq", schema = "public", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private Long price;

    @Column(name = "duration_minutes")
    private Integer duration;

    public CosmetologyService() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "CosmetologyService{" +
                "id=" + id +
                ", name='" + name +
                ", price=" + price +
                ", duration=" + duration +
                '}';
    }
}
