package com.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

public class Server {

    private static final int SHUTDOWN_MINUTES = 3;
    private final int port = 8080;

    private final ExecutorService clientHandler = Executors.newCachedThreadPool();

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    private final CopyOnWriteArrayList<ClientHandler> activeClients = new CopyOnWriteArrayList<>();

    private final LiquidityManager liquidityManager = new LiquidityManager();
    private final PriceManager priceManager = new PriceManager();
    private final PersistenceManager persistenceManager = new PersistenceManager();
    private final OrderManager orderManager;
    private final AuditTask auditTask;

    private ServerSocket socket;
    private volatile boolean running = true;

    public Server() {
        this.orderManager = new OrderManager(priceManager, 5, persistenceManager, liquidityManager);
        this.auditTask = new AuditTask(orderManager, priceManager, liquidityManager, persistenceManager);
        this.orderManager.setAuditTask(auditTask);
    }

    public static void main(String[] args) {
        Server server = new Server();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nServer shutting down...");
            server.shutdown();
        }));

        try {
            server.start();
        } catch (IOException e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        socket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
        System.out.println("Server will shutdown in " + SHUTDOWN_MINUTES + " minutes");

        orderManager.startProcessing();

        scheduler.scheduleAtFixedRate(auditTask,5 , 1, TimeUnit.SECONDS);
        System.out.println("Audit Task scheduled (every 1 seconds)\n");

        updatePrices();

        scheduler.schedule(() -> {
            System.out.println("Initiating automatic shutdown...\n");
            stopServer();
        }, SHUTDOWN_MINUTES, TimeUnit.MINUTES);


        while (running) {
            try {
                Socket clientSocket = socket.accept();
                System.out.println("Accepted connection from " + clientSocket.getInetAddress());

                ClientHandler handler = new ClientHandler(clientSocket, liquidityManager, orderManager);

                activeClients.add(handler);

                handler.setDisconnectCallback(() -> activeClients.remove(handler));

                clientHandler.submit(handler);

            } catch (IOException e) {
                if (running) {
                    System.err.println("Error accepting connection: " + e.getMessage());
                }
            }
        }
    }

    private void updatePrices() {
        scheduler.scheduleAtFixedRate(() -> {
            priceManager.updateAllPrices();
            priceManager.printMarket();
        }, 1, 2, TimeUnit.SECONDS);
    }

    private void stopServer() {
        running = false;


        System.out.println("Notifying " + activeClients.size() + " active clients...");
        for (ClientHandler client : activeClients) {
            client.sendShutdownNotification("Server is shutting down after " + SHUTDOWN_MINUTES + " minutes");
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }


        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing server socket: " + e.getMessage());
        }

        shutdown();

        System.out.println("Server stopped successfully");
        System.exit(0);
    }

    public void shutdown() {

        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }

        clientHandler.shutdown();
        try {
            if (!clientHandler.awaitTermination(5, TimeUnit.SECONDS)) {
                clientHandler.shutdownNow();
            }
        } catch (InterruptedException e) {
            clientHandler.shutdownNow();
        }

        orderManager.shutdown();
        persistenceManager.close();

        System.out.println("===== SHUTDOWN COMPLETE =====");
    }
}