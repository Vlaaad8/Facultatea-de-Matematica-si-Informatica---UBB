package com.example;

import com.example.model.Instrument;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LiquidityManager {


    private final Map<Instrument,Double> currentLiquidity = new ConcurrentHashMap<>();


    private void initializeLiquidity(){
        Instrument[] instruments = Instrument.values();
        for(Instrument instrument : instruments){
            currentLiquidity.put(instrument, 100000.0);
        }
    }

    public synchronized  boolean allocate(Instrument instrument, double amount){
        double available = currentLiquidity.get(instrument);

        if(available >= amount){
            currentLiquidity.put(instrument, available - amount);
            return true;
        }
        return false;
    }

    public synchronized void reallocate(Instrument instrument, double amount){
        double current = currentLiquidity.get(instrument);
        double newAmount = current + amount;
        currentLiquidity.put(instrument, newAmount);
    }

    public LiquidityManager(){
        initializeLiquidity();
    }

}
