package org.ulpgc.dacd.infrastructure.adapters;

import org.ulpgc.dacd.infrastructure.port.HistoricalEventPort;

public class EventLoader implements HistoricalEventPort{
    @Override
    public void loadHistoricalData(){
        System.out.println("Loading historical data");
    }
}
