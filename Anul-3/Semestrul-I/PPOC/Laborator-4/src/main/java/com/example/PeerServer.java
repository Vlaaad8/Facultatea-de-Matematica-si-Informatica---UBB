package com.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerServer implements Runnable {

    private final int port;

    private final ExecutorService threadPool = Executors.newCachedThreadPool();

    private final ConnectionManager connectionManager;

    public ServerSocket serverSocket;

    private final String myName;

    private volatile boolean isRunning = true;

    private static final Logger logger = LogManager.getLogger(PeerServer.class);

    public PeerServer(int port, ConnectionManager connectionManager, String myName) {
        this.port = port;
        this.connectionManager = connectionManager;
        this.myName = myName;
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            logger.info("Started server peer on port {}", port);
            while (isRunning) {
                try {
                    Socket socket = serverSocket.accept();

                    PeerHandler handler = new PeerHandler(socket, connectionManager, myName, false);

                    threadPool.execute(handler);
                } catch (IOException e) {
                    if (isRunning) {
                        logger.error("Error accepting connection", e);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Could not start server on port {}", port, e);
        }
    }

    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdownNow();
        } catch (IOException e) {
            logger.error("Error stopping server", e);
        }
        logger.info("Server Stopped");
    }
}
