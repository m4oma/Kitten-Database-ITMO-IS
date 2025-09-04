package models;

import exceptions.FaithfulKittenException;

import java.util.ArrayList;

public interface KittenInterface {
    void makeFriends(Kitten kitten);
    void becomeLoved(Mistress mistress) throws FaithfulKittenException;
    ArrayList<Kitten> showFriends();
    void breakFriendship(Kitten friend);
}
