package org.example.service;

import org.example.domain.Friendship;
import org.example.domain.User;
import org.example.repository.FriendshipDbRepository;
import org.example.repository.UserDbRepository;

public class FriendshipService implements Service<Friendship> {
    FriendshipDbRepository repository;
    UserDbRepository userRepository;

    public FriendshipService(FriendshipDbRepository repository, UserDbRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    @Override
    public Friendship delete(Long ID) {
        return repository.delete(ID).orElseThrow(() -> new IllegalArgumentException("Service says: invalid ID"));
    }

    public Friendship save(Long ID1, Long ID2) {
        User user1 = userRepository.findOne(ID1).orElseThrow(() -> new IllegalArgumentException("User not found"));
        User user2 = userRepository.findOne(ID2).orElseThrow(() -> new IllegalArgumentException("User not found"));
        Friendship friendship = new Friendship(user1, user2);
        return repository.save(friendship).orElseThrow(() -> new IllegalArgumentException("Friendship not found"));
    }

    @Override
    public Iterable<Friendship> findAll() {
        return repository.findAll();
    }
}
