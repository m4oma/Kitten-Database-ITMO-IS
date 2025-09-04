package ru.m4oma.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@Getter
@Table(name = "mistresses")
public class Mistress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "mistress_kittens", joinColumns = @JoinColumn(name = "mistress_id"))
    @Column(name = "kitten_id")
    private List<Integer> kittenIds = new ArrayList<>();

    @Column(name = "user_id")
    private Integer userId;

    public Mistress() {
    }

    public Mistress(String mistressName, LocalDate mistressBirthDate) {
        this.name = mistressName;
        this.birthDate = mistressBirthDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mistress mistress = (Mistress) o;
        return Objects.equals(id, mistress.id);
    }
}