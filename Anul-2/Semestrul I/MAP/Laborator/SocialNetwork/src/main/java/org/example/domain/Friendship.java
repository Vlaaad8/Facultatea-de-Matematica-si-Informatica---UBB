package org.example.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class Friendship extends Entity<Long> {
    private User firstFriend;
    private User secondFriend;
    private LocalDateTime friendsFrom;

    public Friendship(User firstFriend, User secondFriend) {
        this.firstFriend = firstFriend;
        this.secondFriend = secondFriend;
        this.friendsFrom = LocalDateTime.now();
    }

    public User getFirstFriend() {
        return firstFriend;
    }

    public void setFirstFriend(User firstFriend) {
        this.firstFriend = firstFriend;
    }

    public User getSecondFriend() {
        return secondFriend;
    }

    public void setSecondFriend(User secondFriend) {
        this.secondFriend = secondFriend;
    }

    public LocalDateTime getFriendsFrom() {
        return friendsFrom;
    }

    public void setFriendsFrom(LocalDateTime friendsFrom) {
        this.friendsFrom = friendsFrom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(firstFriend, that.firstFriend) && Objects.equals(secondFriend, that.secondFriend) && Objects.equals(friendsFrom, that.friendsFrom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstFriend, secondFriend, friendsFrom);
    }

}
