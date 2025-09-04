package ru.m4oma.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Setter
@Getter
@ToString(exclude = "kittens")
@Table(name = "mistresses")
public class Mistress implements MistressInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @OneToMany(mappedBy = "mistress", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Kitten> kittens = new ArrayList<>();

    public Mistress(String mistressName, LocalDate mistressBirthDate) {
        this.name = mistressName;
        this.birthDate = mistressBirthDate;
    }

    @OneToOne(mappedBy = "mistress")
    private AppUser user;

    public Mistress() {
    }

    @Override
    public void entangleKitten(Kitten kitten) {
        if (!kittens.contains(kitten)) {
            kittens.add(kitten);
            kitten.setMistress(this);
        }
    }

    @Override
    public void leaveKitten(Kitten kitten) {
        kittens.removeIf(cat -> cat.getId() == kitten.getId());
        kitten.setMistress(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mistress mistress = (Mistress) o;
        return Objects.equals(id, mistress.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}