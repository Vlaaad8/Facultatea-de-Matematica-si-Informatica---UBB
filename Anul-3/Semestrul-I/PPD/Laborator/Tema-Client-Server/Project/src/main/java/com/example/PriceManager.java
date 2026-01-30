package com.example;

import com.example.model.Instrument;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class PriceManager {

    private final ConcurrentHashMap<Instrument, Double> prices = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Instrument, Double> mu = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Instrument, Double> sigma = new ConcurrentHashMap<>();


    private final Random random = new Random();

    private final double dt = 1.7;
    public PriceManager() {
        initializeMarket();
    }

    private void initializeMarket() {
        Instrument[] instruments = Instrument.values();


        for (Instrument instrument : instruments) {
            prices.put(instrument, 100.0 + random.nextDouble() * 20.0);

            mu.put(instrument, -0.01 + random.nextDouble() * 0.02);

            sigma.put(instrument, 0.5 + random.nextDouble() * 0.5);

        }
    }

    private double generateEpsilon() {
        return random.nextGaussian();
    }


    private void updatePrince(Instrument instrument) {

        double currentPrice = prices.get(instrument);
        double epsilon = generateEpsilon();

        double muVal = mu.get(instrument);
        double sigmaVal = sigma.get(instrument);

        double newPrice = currentPrice + (muVal * dt) + (sigmaVal * Math.sqrt(dt) * epsilon);



        prices.put(instrument, newPrice);
    }

    public synchronized void updateAllPrices() {
        Instrument[] instruments = Instrument.values();
        for (Instrument instrument : instruments) {
            updatePrince(instrument);
        }
    }

    public double getCurrentPrice(Instrument instrument) {
        return prices.get(instrument);
    }

    public void printMarket(){
        Instrument[] instruments = Instrument.values();
        System.out.println("Current Market Prices:");
        for(Instrument instrument : instruments){
            System.out.printf("%s: %.2f%n", instrument.name(), getCurrentPrice(instrument));
        }
    }

}
