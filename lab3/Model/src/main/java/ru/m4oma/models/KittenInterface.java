package ru.m4oma.models;

import java.util.ArrayList;

public interface KittenInterface {
    void makeFriends(Kitten kitten);
    void becomeLoved(Mistress mistress);
    ArrayList<Kitten> showFriends();
    void breakFriendship(Kitten friend);
}