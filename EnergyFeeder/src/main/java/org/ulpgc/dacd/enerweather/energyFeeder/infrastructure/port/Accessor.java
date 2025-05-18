package org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.port;

import org.ulpgc.dacd.enerweather.energyFeeder.application.domain.model.Energy;
import org.ulpgc.dacd.enerweather.energyFeeder.infrastructure.adapters.accessors.FetchException;

import java.util.List;

public interface Accessor {
    List<Energy> fetchEnergyData() throws FetchException;
}
