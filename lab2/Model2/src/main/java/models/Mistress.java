package models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Setter
@Getter
@Table(name = "mistresses")
public class Mistress implements MistressInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @OneToMany(mappedBy = "mistress", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Kitten> kittens = new ArrayList<>();

    public Mistress(String mistressName, LocalDate mistressBirthDate) {
        name = mistressName;
        birthDate = mistressBirthDate;
    }

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
}
