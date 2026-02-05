package com.example;

import com.example.model.Order;
import com.example.model.OrderStatus;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final LiquidityManager liquidityManager;
    private final OrderManager orderManager;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    private Runnable disconnectCallback;

    public ClientHandler(Socket socket, LiquidityManager liquidityManager, OrderManager orderManager) {
        this.socket = socket;
        this.liquidityManager = liquidityManager;
        this.orderManager = orderManager;
    }

    public void setDisconnectCallback(Runnable callback) {
        this.disconnectCallback = callback;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush();
            in = new ObjectInputStream(socket.getInputStream());


            while (true) {
                Object obj = in.readObject();

                if (obj instanceof Order) {
                    Order order = (Order) obj;
                    handleOrder(order);
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        } catch (ClassNotFoundException e) {
            System.err.println("Unknown object type");
        } finally {
            cleanup();
        }
    }

    private void handleOrder(Order order) {
        System.out.println("\nReceived order: " + order);


        CompletableFuture<Order> future = orderManager.submitOrder(order);
        future.thenAccept(processedOrder -> {
            System.out.println("Order " + processedOrder.getId() +
                    " completed with status: " + processedOrder.getStatus());
            sendResponse(processedOrder);

        }).exceptionally(ex -> {
            System.err.println("Error processing order " + order.getId() + ": " + ex.getMessage());
            order.setStatus(OrderStatus.REJECTED);
            liquidityManager.reallocate(order.getInstrument(), order.getQuantity());
            sendResponse(order);
            return null;
        });
    }

    private void sendResponse(Order order) {
        try {
            out.writeObject(order);
            out.flush();
            System.out.println("Sent response: Order " + order.getId() + " - " + order.getStatus());
        } catch (IOException e) {
            System.err.println("Failed to send response: " + e.getMessage());
        }
    }

    public void sendShutdownNotification(String message) {
        try {
            out.writeObject("SHUTDOWN:" + message);
            out.flush();
            System.out.println("Shutdown notification sent to client");
        } catch (IOException e) {
            System.err.println("Failed to send shutdown notification: " + e.getMessage());
        }
    }

    private void cleanup() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (disconnectCallback != null) {
            disconnectCallback.run();
        }
    }
}