package org.example.service;

import org.example.domain.Friendship;
import org.example.domain.User;
import org.example.repository.FriendshipDbRepository;
import org.example.repository.UserDbRepository;

import java.util.ArrayList;

public class UserService implements Service<User> {
    private UserDbRepository repository;
    private FriendshipDbRepository friendshipRepository;

    public UserService(UserDbRepository repository, FriendshipDbRepository friendshipRepository) {
        this.repository = repository;
        this.friendshipRepository = friendshipRepository;
    }

    @Override
    public User delete(Long ID) {
        User user = repository.findOne(ID).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        friendships.forEach(friendship -> {
            if (friendship.getFirstFriend().getID().equals(ID) || friendship.getSecondFriend().getID().equals(ID)) {
                friendshipRepository.delete(friendship.getID()).orElseThrow(null);
            }
        });
        repository.delete(ID).orElseThrow(null);
        return user;
    }

    public User save(String firstName, String lastName) {
        User newUser = new User(firstName, lastName);
        return repository.save(newUser).orElse(null);
    }

    public User update(Long ID, String firstName, String lastName) {
        User toBeUpdated = new User(firstName, lastName);
        toBeUpdated.setID(ID);
        return repository.update(toBeUpdated).orElse(null);
    }
    @Override
    public Iterable<User> findAll(){
        return repository.findAll();
    }

    public ArrayList<User> getFriends(Long ID){
        ArrayList<User> friends = new ArrayList<>();
        Iterable<Friendship> friendships = friendshipRepository.findAll();
        friendships.forEach(friendship->{
            if(friendship.getFirstFriend().getID().equals(ID)) {
                friends.add(friendship.getSecondFriend());
            }
            else if(friendship.getSecondFriend().getID().equals(ID)) {
                friends.add(friendship.getFirstFriend());
            }
        });
        return friends;
    }
    public User findOne(Long ID) {
        return repository.findOne(ID).orElseThrow(()-> new IllegalArgumentException("User not found"));
    }
}

