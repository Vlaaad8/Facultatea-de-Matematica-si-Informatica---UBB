package com.example;

import com.example.model.Instrument;
import com.example.model.Order;
import com.example.model.OrderType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class TradingRobot {

    private final String host = "localhost";
    private final int port;
    private final Random random = new Random();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final int clientId;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Socket socket;
    private volatile boolean running = true;

    private AtomicInteger orderCounter = new AtomicInteger(1);

    public TradingRobot(int port, int clientId) {
        this.port = port;
        this.clientId = clientId;
    }

    public static void main(String[] args) {
        int port = 8080;
        int clientId = new Random().nextInt(1000);

        TradingRobot robot = new TradingRobot(port, clientId);

        try {
            robot.connect();
        } catch (IOException e) {
            System.err.println("Could not connect to server: " + e.getMessage());
        }
    }

    public void connect() throws IOException {
        socket = new Socket(host, port);
        System.out.println("Connected to server " + host + " on port " + port);

        out = new ObjectOutputStream(socket.getOutputStream());
        out.flush();
        in = new ObjectInputStream(socket.getInputStream());

        new Thread(this::listenForResponses).start();
        startTrading();
    }

    private Order generateRandomOrder() {
        int orderId = clientId * 1000000 + orderCounter.getAndIncrement();
        OrderType type = random.nextBoolean() ? OrderType.BUY : OrderType.SELL;

        Instrument[] instruments = Instrument.values();
        Instrument instrument = instruments[random.nextInt(instruments.length)];

        double quantity = 10 + random.nextDouble() * 90;


        double basePrice = 90 + random.nextDouble() * 40;
        double price;
        if (type == OrderType.BUY) {

            price = basePrice + random.nextDouble() * 30;
        } else {

            price = basePrice - random.nextDouble() * 30;
        }

        return new Order(orderId, clientId, instrument, type, quantity, price);
    }

    private void startTrading() {
        Runnable tradingTask = () -> {
            if (!running) return;

            try {
                Order order = generateRandomOrder();
                out.writeObject(order);
                out.flush();
                System.out.println("Sent: Order " + order.getId() + " " + order.getType() +
                        " " + order.getInstrument() + " Qty:" + String.format("%.2f", order.getQuantity()));
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error sending order");
                    disconnect();
                }
            }
        };

        scheduler.scheduleAtFixedRate(tradingTask, 0, 1, java.util.concurrent.TimeUnit.SECONDS);
    }

    private void listenForResponses() {
        try {
            while (running) {
                Object obj = in.readObject();

                if (obj instanceof String) {
                    String message = (String) obj;
                    if (message.startsWith("SHUTDOWN:")) {
                        System.out.println(message.substring(9));
                        System.out.println("Disconnecting...\n");
                        disconnect();
                        break;
                    }
                }

                if (obj instanceof Order) {
                    Order order = (Order) obj;
                    System.out.println("[" + clientId + "] Order " + order.getId() + " -> " + order.getStatus());
                }
            }
        } catch (IOException e) {
            if (running) {
                System.out.println("\nConnection lost (server may have shut down)");
                disconnect();
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Unknown response type");
        }
    }

    private void disconnect() {
        running = false;
        scheduler.shutdown();

        try {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            System.out.println("[" + clientId + "] Disconnected successfully");
        } catch (IOException e) {
            System.err.println("Error during disconnect");
        }

        System.exit(0);
    }
}