package org.ulpgc.dacd.enerweather.reFeeder.application.service;

import org.ulpgc.dacd.enerweather.reFeeder.application.port.ReeFeederInterface;
import org.ulpgc.dacd.enerweather.reFeeder.domain.model.RE;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.accessors.ReeFetchException;

import java.util.List;

public class ReeService {
    private final ReeFeederInterface feeder;

    public ReeService(ReeFeederInterface feeder) {
        this.feeder = feeder;
    }

    public List<RE> getEnergyData() throws ReeFetchException {
        return feeder.fetchEnergyData();
    }
}
