package org.ulpgc.dacd.enerweather.reFeeder.application.port;

import org.ulpgc.dacd.enerweather.reFeeder.domain.model.RE;
import org.ulpgc.dacd.enerweather.reFeeder.infrastructure.accessors.ReeFetchException;

import java.util.List;

public interface ReeFeederInterface {
    List<RE> fetchEnergyData() throws ReeFetchException;
}
