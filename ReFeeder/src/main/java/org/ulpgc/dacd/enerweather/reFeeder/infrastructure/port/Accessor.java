package org.ulpgc.dacd.enerweather.reFeeder.infrastructure.port;

import org.ulpgc.dacd.enerweather.reFeeder.application.domain.model.Energy;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.adapters.accessors.FetchException;

import java.util.List;

public interface Accessor {
    List<Energy> fetchEnergyData() throws FetchException;
}
