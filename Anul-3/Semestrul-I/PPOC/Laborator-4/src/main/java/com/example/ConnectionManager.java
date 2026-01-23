package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    private final Map<String, PeerHandler> activePeers = new ConcurrentHashMap<>();

    private static final Logger logger = LogManager.getLogger(ConnectionManager.class);

    public void addPeer(String name, PeerHandler handler) {
        if(this.activePeers.containsKey(name)) {
            throw new RuntimeException("I already have a connection with a peer named "+name);
        }
        logger.info("Connection successful with: {}", name);
        this.activePeers.put(name, handler);
    }

    public void removePeer(String name) {
        if (name == null) {
            return;
        }
        if (this.activePeers.containsKey(name)) {
            this.activePeers.remove(name);
            logger.info("Removed connection with peer: {}", name);
        }
    }

    public boolean isConnected(String name) {
        return this.activePeers.containsKey(name);
    }

    public void sendMessageTo(String name, String message) {
        PeerHandler peer = this.activePeers.get(name);

        if (peer != null) {
            peer.sendMessage(message);
        } else {
            logger.warn("Peer {} not found", name);
        }
    }

    public void disconnectPeer(String name) {
        PeerHandler peer = this.activePeers.get(name);
        if (peer != null) {
            peer.sendBye();
        } else {
            logger.warn("Peer {} not found for disconnect", name);
        }
    }

    public void closeAll() {
        activePeers.values().forEach(PeerHandler::closeConnection);
        activePeers.clear();
    }

}
