package com.example;

import com.example.model.Annulment;
import com.example.model.Execution;
import com.example.model.Order;

import java.io.*;
import java.time.format.DateTimeFormatter;

public class PersistenceManager {

    private static final String ORDERS_FILE = "orders.txt";
    private static final String EXECUTIONS_FILE = "executions.txt";
    private static final String ANNULMENTS_FILE = "annulments.txt";
    private static final String AUDIT_FILE = "audit_log.txt";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private PrintWriter ordersWriter;
    private PrintWriter executionsWriter;
    private PrintWriter annulmentsWriter;
    private PrintWriter auditWriter;

    public PersistenceManager() {
        initializeFiles();
        System.out.println("PersistenceManager initialized");
    }


    private void initializeFiles() {
        try {
            // Orders file
            ordersWriter = new PrintWriter(new BufferedWriter(new FileWriter(ORDERS_FILE, false)));
            ordersWriter.println("OrderID,ClientID,Instrument,Type,Volume,PriceLimit,Status,PlacedAt");
            ordersWriter.flush();


            executionsWriter = new PrintWriter(new BufferedWriter(new FileWriter(EXECUTIONS_FILE, false)));
            executionsWriter.println("ExecutionID,OrderID,Timestamp,ExecutedVolume,FinalPrice,Commission");
            executionsWriter.flush();

            annulmentsWriter = new PrintWriter(new BufferedWriter(new FileWriter(ANNULMENTS_FILE, false)));
            annulmentsWriter.println("AnnulmentID,OrderID,Timestamp,Reason");
            annulmentsWriter.flush();

            auditWriter = new PrintWriter(new BufferedWriter(new FileWriter(AUDIT_FILE, false)));
            auditWriter.println("Timestamp,Event,Details");
            auditWriter.flush();

            System.out.println("Files initialized: " + ORDERS_FILE + ", " + EXECUTIONS_FILE + ", " +
                    ANNULMENTS_FILE + ", " + AUDIT_FILE);

        } catch (IOException e) {
            System.err.println("Failed to initialize files: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public synchronized void saveOrder(Order order) {
        try {
            String line = String.format("%d,%s,%s,%s,%.2f,%.2f,%s,%s",
                    order.getId(),
                    order.getClientId(),
                    order.getInstrument(),
                    order.getType(),
                    order.getQuantity(),
                    order.getPrice(),
                    order.getStatus(),
                    order.getTimestamp().format(formatter)
            );

            ordersWriter.println(line);
            ordersWriter.flush();

            System.out.println("[ORDERS] Saved order " + order.getId());

        } catch (Exception e) {
            System.err.println("Failed to save order: " + e.getMessage());
        }
    }


    public synchronized void saveExecution(Execution execution) {
        try {
            String line = String.format("%d,%d,%s,%.2f,%.2f,%.2f",
                    execution.getId(),
                    execution.getOrderId(),
                    execution.getTimestamp().format(formatter),
                    execution.getExecutedVolume(),
                    execution.getFinalPrice(),
                    execution.getCommission()
            );

            executionsWriter.println(line);
            executionsWriter.flush();

            System.out.println("[EXECUTIONS] Saved execution " + execution.getId() +
                    " for order " + execution.getOrderId());

        } catch (Exception e) {
            System.err.println("Failed to save execution: " + e.getMessage());
        }
    }


    public synchronized void saveAnnulment(Annulment annulment) {
        try {
            String line = String.format("%d,%d,%s,%s",
                    annulment.getId(),
                    annulment.getOrderId(),
                    annulment.getTimestamp().format(formatter),
                    annulment.getReason()
            );

            annulmentsWriter.println(line);
            annulmentsWriter.flush();

            System.out.println("[ANNULMENTS] Saved annulment " + annulment.getId() +
                    " for order " + annulment.getOrderId());

        } catch (Exception e) {
            System.err.println("Failed to save annulment: " + e.getMessage());
        }
    }


    public synchronized void saveAuditEvent(String event, String details) {
        try {
            String timestamp = java.time.LocalDateTime.now().format(formatter);
            String line = String.format("%s,%s,%s", timestamp, event, details);

            auditWriter.println(line);
            auditWriter.flush();

        } catch (Exception e) {
            System.err.println("Failed to save audit event: " + e.getMessage());
        }
    }


    public void close() {
        try {
            if (ordersWriter != null) ordersWriter.close();
            if (executionsWriter != null) executionsWriter.close();
            if (annulmentsWriter != null) annulmentsWriter.close();
            if (auditWriter != null) auditWriter.close();

            System.out.println("All files closed successfully");
        } catch (Exception e) {
            System.err.println("Error closing files: " + e.getMessage());
        }
    }
}