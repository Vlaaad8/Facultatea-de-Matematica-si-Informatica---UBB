package org.example;

import org.example.domain.validation.FriendshipValidation;
import org.example.domain.validation.UserValidation;
import org.example.repository.FriendshipDbRepository;
import org.example.repository.UserDbRepository;
import org.example.service.CommunityService;
import org.example.service.FriendshipService;
import org.example.service.UserService;
import org.example.ui.Console;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            String username="postgres";
            String pasword="1mai1984";
            String url="jdbc:postgresql://localhost:5432/postgres";


            UserDbRepository userRepository=new UserDbRepository(url,pasword,username,new UserValidation());
            FriendshipDbRepository friendshipRepository = new FriendshipDbRepository(new FriendshipValidation(),url,username,pasword);
            UserService userService = new UserService(userRepository,friendshipRepository);
            FriendshipService friendshipService = new FriendshipService(friendshipRepository, userRepository);
            CommunityService communityService=new CommunityService(userRepository,friendshipRepository);
            Console console = new Console(userService, friendshipService,communityService);
            boolean condition=true;
            while (condition) {
                Scanner scanner = new Scanner(System.in);
                console.menu();
                System.out.print("Introduce your option: ");
                int option = scanner.nextInt();
                switch (option) {
                    case 1:
                        console.addUser();
                        break;
                    case 2:
                        console.addFriendship();
                        break;
                    case 3:
                        console.removeUser();
                        break;
                    case 4:
                        console.removeFriendship();
                        break;
                    case 5:
                        console.updateUser();
                        break;
                    case 6:
                        console.showUsers();
                        break;
                    case 7:
                        console.showFriendships();
                        break;
                    case 8:
                        console.showCommunities();
                        break;
                    case 9:
                        console.showBiggestCommunity();
                        break;
                    case 10:
                        condition=false;
                        break;
                    default:
                        System.out.print("Introduce a valid option!\n");
                        break;

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
