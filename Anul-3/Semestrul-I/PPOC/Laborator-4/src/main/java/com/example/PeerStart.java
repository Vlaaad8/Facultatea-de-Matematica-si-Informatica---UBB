package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class PeerStart {
    private static final String localhost = "127.0.0.1";
    private static final Logger logger = LogManager.getLogger(PeerStart.class);

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Name:");
        String name = sc.nextLine();

        System.out.println("Port:");
        int myPort = sc.nextInt();
        sc.nextLine();

        ConnectionManager connectionManager = new ConnectionManager();

        PeerServer server = new PeerServer(myPort, connectionManager, name);
        new Thread(server).start();

        System.out.println("Commands");
        System.out.println("1. !hello <Port>");
        System.out.println("2. !msg <Name> <Message>");
        System.out.println("3. !bye <Name>");
        System.out.println("4. !byebye");

        while (true) {
            String command = sc.nextLine();

            if (command.startsWith("!byebye")) {
                connectionManager.closeAll();
                server.stop();
                System.exit(0);

            } else if (command.startsWith("!hello ")) {
                try {
                    String[] parts = command.split(" ");
                    int targetPort = Integer.parseInt(parts[1]);

                    connectToPeer(targetPort, name, connectionManager);
                } catch (Exception e) {
                    logger.error("Command error !hello");
                }

            } else if (command.startsWith("!msg ")) {
                try {
                    String[] parts = command.split(" ", 3);

                    if (parts.length < 3) {
                        logger.warn("Empty message");
                        continue;
                    }

                    String recipient = parts[1];
                    String message = parts[2];

                    connectionManager.sendMessageTo(recipient, message);
                } catch (Exception e) {
                    logger.warn("Wrong format");
                }

            } else if (command.startsWith("!bye ")) {
                String[] parts = command.split(" ");
                if (parts.length < 2) {
                    logger.warn("Use !bye <Name>");
                    continue;
                }
                String recipient = parts[1];
                connectionManager.disconnectPeer(recipient);
            }
            else{
                logger.info("Wrong format");
            }
        }
    }

    private static void connectToPeer(int port, String myName, ConnectionManager connectionManager) {
        try {
            logger.debug("Trying to connect to {}", port);
            Socket socket = new Socket(localhost, port);

            PeerHandler handler = new PeerHandler(socket, connectionManager, myName, true);
            new Thread(handler).start();

        } catch (IOException e) {
            logger.error("Couldn't connect to port  {}. Verify if peer is on.", port);
        }
    }
}
