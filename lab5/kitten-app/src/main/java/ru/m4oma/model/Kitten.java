package ru.m4oma.model;

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
@ToString(exclude = {"friends"})
@Table(name = "kittens")
public class Kitten {

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

    @Column(name = "mistress_id")
    private Integer mistressId;

    @ManyToMany
    @JoinTable(
            name = "kitten_friends",
            joinColumns = @JoinColumn(name = "kitten_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Kitten> friends = new ArrayList<>();

    public Kitten(String name, LocalDate birthDate, double tailLength, Breed breed, Colour colour) {
        this.name = name;
        this.birthDate = birthDate;
        this.tailLength = tailLength;
        this.breed = breed;
        this.colour = colour;
    }

    public void makeFriends(Kitten kitten) {
        if (!friends.contains(kitten) && kitten.getId() != this.id) {
            friends.add(kitten);
        }
    }

    public void breakFriendship(Kitten friend) {
        friends.removeIf(k -> k.getId() == friend.getId());
    }

    public List<Kitten> showFriends() {
        return new ArrayList<>(friends);
    }
}