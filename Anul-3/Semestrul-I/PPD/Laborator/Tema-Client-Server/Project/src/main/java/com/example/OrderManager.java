package com.example;

import com.example.model.Annulment;
import com.example.model.Execution;
import com.example.model.Order;
import com.example.model.OrderStatus;
import com.example.model.OrderType;

import java.time.LocalDateTime;
import com.example.AuditTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderManager {

    private final BlockingQueue<Order> orderQueue = new LinkedBlockingQueue<>();
    private final ExecutorService executorService;
    private final PriceManager priceManager;
    private final PersistenceManager persistenceManager;
    private final LiquidityManager liquidityManager;

    private final ConcurrentHashMap<Integer, CompletableFuture<Order>> orderFutures = new ConcurrentHashMap<>();

    private volatile boolean running = true;

    private final AtomicInteger executionIdCounter = new AtomicInteger(1);
    private final AtomicInteger annulmentIdCounter = new AtomicInteger(1);

    private AuditTask auditTask;

    public OrderManager(PriceManager priceManager, int workerThreads, PersistenceManager persistenceManager, LiquidityManager liquidityManager) {
        this.priceManager = priceManager;
        this.executorService = Executors.newFixedThreadPool(workerThreads);
        this.persistenceManager = persistenceManager;
        this.liquidityManager = liquidityManager;
    }

    public void setAuditTask(AuditTask auditTask) {
        this.auditTask = auditTask;
    }

    public CompletableFuture<Order> submitOrder(Order order) {
        CompletableFuture<Order> future = new CompletableFuture<>();

        try {
            orderFutures.put(order.getId(), future);
            orderQueue.put(order);
            persistenceManager.saveOrder(order);
            System.out.println("Order " + order.getId() + " queued (Queue size: " + orderQueue.size() + ")");

        } catch (InterruptedException e) {
            future.completeExceptionally(e);
            orderFutures.remove(order.getId());
        }

        return future;
    }

    public void startProcessing() {
        new Thread(() -> {
            System.out.println("OrderManager worker started");

            while (running) {
                try {
                    Order order = orderQueue.take();
                    executorService.submit(() -> processOrder(order));

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "OrderQueue-Consumer").start();
    }

    private void processOrder(Order order) {
        CompletableFuture<Order> future = orderFutures.get(order.getId());

        if (future == null) {
            System.err.println("No future found for order " + order.getId());
            return;
        }

        try {
            System.out.println("Processing order " + order.getId() + "...");

            double currentPrice = priceManager.getCurrentPrice(order.getInstrument());
            boolean canExecute = checkMarketConditions(order, currentPrice);

            boolean hasLiquidity = liquidityManager.allocate(order.getInstrument(), order.getQuantity());

            if (canExecute && hasLiquidity) {
                executeOrder(order, currentPrice);
                System.out.println("Order " + order.getId() + " executed IMMEDIATELY");
            } else if (!hasLiquidity) {
                cancelOrder(order, "Ordin esuat");
                System.out.println("Order " + order.getId() + " insufficient liquidity");
            } else {
                System.out.println("Order " + order.getId() + " remains PENDING (price conditions) " +
                        "(Current: " + String.format("%.2f", currentPrice) +
                        ", Limit: " + String.format("%.2f", order.getPrice()) + ")");
                    auditTask.addPendingOrder(order);

            }

            future.complete(order);
            orderFutures.remove(order.getId());

        } catch (Exception e) {
            System.err.println("Error processing order " + order.getId() + ": " + e.getMessage());
            future.completeExceptionally(e);
            orderFutures.remove(order.getId());
        }
    }


    public void executeOrder(Order order, double currentPrice) {

        int executionId = executionIdCounter.getAndIncrement();
        Execution execution = new Execution(
                executionId,
                order.getId(),
                order.getQuantity(),
                currentPrice
        );

        order.setStatus(OrderStatus.TRANSACTED);

        System.out.println("Order " + order.getId() + " EXECUTED at " +
                String.format("%.2f", currentPrice));
        System.out.println("   Execution[" + execution.getId() + "] - " +
                "Volume: " + String.format("%.2f", execution.getExecutedVolume()) + ", " +
                "Price: " + String.format("%.2f", execution.getFinalPrice()) + ", " +
                "Commission: " + String.format("%.2f", execution.getCommission()));

        persistenceManager.saveExecution(execution);
    }

    public void cancelOrder(Order order, String reason) {
        int annulmentId = annulmentIdCounter.getAndIncrement();
        Annulment annulment = new Annulment(
                annulmentId,
                order.getId(),
                reason
        );

        order.setStatus(OrderStatus.CANCELLED);

        System.out.println("Order " + order.getId() + " CANCELLED");
        System.out.println("   Annulment[" + annulment.getId() + "] - Reason: " + reason);

        persistenceManager.saveAnnulment(annulment);
    }

    private boolean checkMarketConditions(Order order, double currentPrice) {
        boolean canExecute;

        if (order.getType() == OrderType.BUY) {
            canExecute = currentPrice <= order.getPrice();
            System.out.println("BUY Order " + order.getId() +
                    ": Market=" + String.format("%.2f", currentPrice) +
                    ", Limit=" + String.format("%.2f", order.getPrice()) +
                    " -> " + (canExecute ? "CAN EXECUTE" : "WAIT"));
        } else {
            canExecute = currentPrice >= order.getPrice();
            System.out.println("SELL Order " + order.getId() +
                    ": Market=" + String.format("%.2f", currentPrice) +
                    ", Limit=" + String.format("%.2f", order.getPrice()) + 
                    " -> " + (canExecute ? "CAN EXECUTE" : "WAIT"));
        }
        
        return canExecute;
    }

    public void shutdown() {
        running = false;
        executorService.shutdown();
    }
}