package models;

import exceptions.FaithfulKittenException;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "kittens")
@NoArgsConstructor
@AllArgsConstructor
public class Kitten implements KittenInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Breed breed;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Colour colour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mistress_id")
    private Mistress mistress;

    @OneToMany
    private List<Kitten> friends = new ArrayList<>();

    public Kitten(String name, LocalDate birthDate, Breed breed, Colour colour, models.Mistress mistress) {
        this.name = name;
        this.birthDate = birthDate;
        this.breed = breed;
        this.colour = colour;
        this.mistress = mistress;
        this.friends = new ArrayList<>();
    }


    public void makeFriends(Kitten kitten) {
        if (!friends.contains(kitten) && kitten.getId() != this.id) {
            friends.add(kitten);
        }
    }

    public void becomeLoved(Mistress mistress) throws FaithfulKittenException {
        if (this.mistress == null) {
            this.mistress = mistress;
        } else {
            throw new FaithfulKittenException();
        }
    }

    public ArrayList<Kitten> showFriends() {
        return new ArrayList<>(friends);
    }

    @Override
    public void breakFriendship(Kitten friend) {
        friends.removeIf(k -> k.getId() == friend.getId());
    }
}
