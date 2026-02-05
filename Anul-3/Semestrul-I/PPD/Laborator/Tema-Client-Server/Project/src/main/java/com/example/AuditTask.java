package com.example;

import com.example.LiquidityManager;
import com.example.OrderManager;
import com.example.PriceManager;
import com.example.model.Order;
import com.example.model.OrderStatus;
import com.example.model.OrderType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AuditTask implements Runnable {

    private final List<Order> pendingOrders;
    private final OrderManager orderManager;
    private final PriceManager priceManager;
    private final LiquidityManager liquidityManager;
    private final PersistenceManager persistenceManager;

    private static final long EXPIRATION_SECONDS = 30;

    public AuditTask(OrderManager orderManager, PriceManager priceManager, LiquidityManager liquidityManager, PersistenceManager persistenceManager) {
        this.pendingOrders = new CopyOnWriteArrayList<>();
        this.orderManager = orderManager;
        this.priceManager = priceManager;
        this.liquidityManager = liquidityManager;
        this.persistenceManager = persistenceManager;
    }

    public void addPendingOrder(Order order) {
        if (order.getStatus() == OrderStatus.PENDING) {
            pendingOrders.add(order);
            System.out.println("Order " + order.getId() + " added to audit monitoring");
        }
    }


    @Override
    public void run() {
        System.out.println("Pending orders to check: " + pendingOrders.size());
        persistenceManager.saveAuditEvent("AUDIT_START",
                "Checking " + pendingOrders.size() + " pending orders");
        if (pendingOrders.isEmpty()) {
            System.out.println("No pending orders to audit");
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        int expiredCount = 0;
        int executedCount = 0;

        for (Order order : pendingOrders) {

            if (isExpired(order, now)) {
                handleExpiredOrder(order);
                expiredCount++;
                continue;
            }

            if (canExecuteNow(order)) {
                handleExecutableOrder(order);
                executedCount++;
            } else {
                boolean hasLiquidity = liquidityManager.allocate(order.getInstrument(), order.getQuantity());
                if (hasLiquidity) {
                    liquidityManager.reallocate(order.getInstrument(), order.getQuantity());
                } else {
                    orderManager.cancelOrder(order, "Ordin esuat");
                    pendingOrders.remove(order);
                    expiredCount++;
                    System.out.println("Order " + order.getId() + " insufficient liquidity");
                }
            }
        }

        System.out.println("Audit summary: " +
                executedCount + " executed, " +
                expiredCount + " expired/cancelled");
        persistenceManager.saveAuditEvent("AUDIT_COMPLETE",
                String.format("Executed: %d, Cancelled: %d", executedCount, expiredCount));
    }

    private boolean isExpired(Order order, LocalDateTime now) {
        Duration duration = Duration.between(order.getTimestamp(), now);
        return duration.getSeconds() > EXPIRATION_SECONDS;
    }

    private boolean canExecuteNow(Order order) {
        double currentPrice = priceManager.getCurrentPrice(order.getInstrument());
        boolean priceMatches;
        if (order.getType() == OrderType.BUY) {

            priceMatches = currentPrice <= order.getPrice();
        } else {

            priceMatches = currentPrice >= order.getPrice() ;
        }



        boolean canExecute = priceMatches;

        System.out.println("Order " + order.getId() + " " + order.getType() + " " + order.getInstrument() +
                ": Price " + (priceMatches ? "OK" : "NO") +
                " (market: " + String.format("%.2f", currentPrice) + ", order: " + String.format("%.2f", order.getPrice()) + ")" +
                " " +
                " -> " + (canExecute ? "CAN EXECUTE" : "WAIT"));

        return canExecute;
    }

    private void handleExpiredOrder(Order order) {
        System.out.println("Order " + order.getId() + " has EXPIRED (>" + EXPIRATION_SECONDS + "s)");

        orderManager.cancelOrder(order, "Expired after " + EXPIRATION_SECONDS + " seconds");

        liquidityManager.reallocate(order.getInstrument(), order.getQuantity());

        pendingOrders.remove(order);
    }

    private void handleExecutableOrder(Order order) {
        double currentPrice = priceManager.getCurrentPrice(order.getInstrument());

        System.out.println("Order " + order.getId() + " can NOW be executed at " +
                String.format("%.2f", currentPrice));

        if (!liquidityManager.allocate(order.getInstrument(), order.getQuantity())) {
            System.err.println("Failed to allocate liquidity for order " + order.getId());
            return;
        }

        orderManager.executeOrder(order, currentPrice);

        pendingOrders.remove(order);
    }
}