package ru.m4oma.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"mistress", "friends"})
@Table(name = "kittens")
public class Kitten implements KittenInterface {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "tail_length")
    private Double tailLength;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Breed breed;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Colour colour;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mistress_id")
    private Mistress mistress;

    @ManyToMany
    @JoinTable(
            name = "kitten_friends",
            joinColumns = @JoinColumn(name = "kitten_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Kitten> friends = new ArrayList<>();

    public Kitten(String name, LocalDate birthDate, double tailLength, Breed breed, Colour colour, Mistress mistress) {
        this.name = name;
        this.birthDate = birthDate;
        this.tailLength = tailLength;
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

    public void becomeLoved(Mistress mistress) throws IllegalArgumentException {
        if (this.mistress == null) {
            this.mistress = mistress;
        } else {
            throw new IllegalArgumentException();
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