package org.example.socialnetworkfx.socialnetworkfx.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import org.example.socialnetworkfx.socialnetworkfx.domain.User;
import org.example.socialnetworkfx.socialnetworkfx.service.FriendshipRequestService;
import org.example.socialnetworkfx.socialnetworkfx.service.FriendshipService;
import org.example.socialnetworkfx.socialnetworkfx.service.UserService;

import java.util.ArrayList;

public class UserPage {
    @FXML
    public Label userText;
    @FXML
    public Label friendsText;
    @FXML
    public Label statusText;
    @FXML
    public PieChart friendsChart;
    @FXML
    public Label totalFriendsText;

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private Long ID;

    public void setService(UserService userService, FriendshipService friendshipService, FriendshipRequestService friendshipRequestService, Long ID) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRequestService = friendshipRequestService;
        this.ID = ID;
        userText.setText(userService.findOne(ID).getFirstName() + " " + userService.findOne(ID).getLastName());
        totalFriendsText.setText("Total friends: " + friendshipService.getFriends(ID, friendshipService.findAll()).size());
        PieChart.Data friends = new PieChart.Data("Friend requests received ", friendshipRequestService.findByReceiver(ID).size());
        PieChart.Data friends1 = new PieChart.Data("Friend requests send", friendshipRequestService.getNumberOfRequestsSend(ID));
        friendsChart.setLegendVisible(true);
        friendsChart.getData().addAll(friends, friends1);
        friendsText.setText(friendsText());

    }

    public String friendsText() {
        ArrayList<User> friends = friendshipService.getFriends(ID, friendshipService.findAll());
        StringBuilder friendsString = new StringBuilder();
        friendsString.append("Friends: ");
        for (int i=0; i<friends.size() && i<1 ; i++) {
            friendsString.append(friends.get(i).getFirstName()).append(" ").append(friends.get(i).getLastName()).append("");
        }
        if (friends.size() > 2) {
            friendsString.append(",").append(friends.get(2).getFirstName()).append(" ").append(friends.get(2).getLastName());
            friendsString.append("+").append((friends.size() - 2)).append(" more");
        }
        return friendsString.toString();
    }
}