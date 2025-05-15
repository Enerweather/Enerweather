package org.ulpgc.dacd.enerweather.reFeeder.infrastructure.port;

import org.ulpgc.dacd.enerweather.reFeeder.application.domain.model.Energy;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.accessors.EnergyFetchException;

import java.util.List;

public interface EnergyFeederInterface {
    List<Energy> fetchEnergyData() throws EnergyFetchException;
}
