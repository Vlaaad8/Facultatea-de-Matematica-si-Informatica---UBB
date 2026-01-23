package com.example;

import com.example.proto.Chat.ChatMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class PeerHandler implements Runnable {

    private static final Logger logger = LogManager.getLogger(PeerHandler.class);
    private final Socket socket;
    private final ConnectionManager connectionManager;
    private final String myName;
    private final boolean isInitiator;

    private volatile boolean running = true;

    private String peerName = "NaN";
    private InputStream in;
    private OutputStream out;

    public PeerHandler(Socket socket, ConnectionManager connectionManager, String myName, boolean isInitiator) {
        this.socket = socket;
        this.connectionManager = connectionManager;
        this.myName = myName;
        this.isInitiator = isInitiator;
    }

    @Override
    public void run() {
        try {
            in = socket.getInputStream();
            out = socket.getOutputStream();

            if (!performHandshake()) {
                logger.error("Handshake failed. Closing.");
                return;
            }

            while (running) {
                ChatMessage msg = ChatMessage.parseDelimitedFrom(in);
                if (msg == null) {
                    if (running) {
                        logger.info("Peer {} disconnected", peerName);
                    }
                    break;
                }

                switch (msg.getType()) {
                    case CHAT:
                        System.out.println("[" + peerName + "]: " + msg.getText());
                        break;
                    case BYE:
                        logger.info("Peer {} requested disconnect", peerName);
                        running = false;
                        break;
                    default:
                        logger.debug("Message of type {} ignored", msg.getType());
                }
            }

        } catch (IOException e) {
            if (running) {
                logger.warn("Connection Lost with {}: {}", peerName, e.getMessage());
            }
        } finally {
            closeConnection();
        }
    }

    private boolean performHandshake() throws IOException {
        if (isInitiator) {
            ChatMessage hello = ChatMessage.newBuilder()
                    .setType(ChatMessage.MessageType.HELLO)
                    .setSender(myName)
                    .build();
            hello.writeDelimitedTo(out);
            out.flush();

            ChatMessage response = ChatMessage.parseDelimitedFrom(in);

            logger.debug("Handshake initiator: received response={}", response);

            if (response != null && response.getType() == ChatMessage.MessageType.ACK) {
                this.peerName = response.getSender();
                try {
                    connectionManager.addPeer(this.peerName, this);
                } catch (RuntimeException e) {
                    logger.warn("{}", e.getMessage());
                    return false;
                }
                return true;
            } else {
                return false;
            }

        } else {
            ChatMessage first = ChatMessage.parseDelimitedFrom(in);

            logger.debug("Handshake receiver: received first={}", first);

            if (first != null && first.getType() == ChatMessage.MessageType.HELLO) {
                this.peerName = first.getSender();

                ChatMessage ack = ChatMessage.newBuilder()
                        .setType(ChatMessage.MessageType.ACK)
                        .setSender(myName)
                        .build();
                ack.writeDelimitedTo(out);
                out.flush();
                try {
                    connectionManager.addPeer(this.peerName, this);
                } catch (RuntimeException e) {
                    logger.warn("{}", e.getMessage());
                    return false;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    public void sendMessage(String message) {
        if (!running) {
            logger.warn("You arent connected again with {}", peerName);
            return;
        }
        if (out == null) {
            logger.warn("Tried to send message to {}, but output stream is null", peerName);
            return;
        }

        try {
            ChatMessage chat = ChatMessage.newBuilder()
                    .setType(ChatMessage.MessageType.CHAT)
                    .setSender(myName)
                    .setText(message)
                    .build();
            chat.writeDelimitedTo(out);
            out.flush();
        } catch (IOException e) {
            logger.error("Error sending message to {}", peerName, e);
            closeConnection();
        }
    }

    public void sendBye() {
        if (!running) {
            return;
        }
        if (out != null) {
            try {
                ChatMessage bye = ChatMessage.newBuilder()
                        .setType(ChatMessage.MessageType.BYE)
                        .setSender(myName)
                        .build();
                bye.writeDelimitedTo(out);
                out.flush();
            } catch (IOException e) {
                logger.error("Error sending BYE to {}", peerName, e);
            }
        }
        closeConnection();
    }

    public void closeConnection() {
        running = false;
        try {
            connectionManager.removePeer(this.peerName);

            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            logger.error("Error closing connection with {}", peerName, e);
        }
    }
}
