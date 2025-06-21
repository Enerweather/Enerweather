package org.ulpgc.dacd.enerweather.businessunit.recommendations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Recommendator {
    private static final double WIND_SPEED_THRESHOLD   = 6.0;
    private static final double WIND_GEN_THRESHOLD     = 2000;
    private static final double SOLAR_GEN_THRESHOLD    = 3500;
    private static final String WIND_INDICATOR         = "Wind";
    private static final String SOLAR_INDICATOR        = "Solar photovoltaic";
    private static final Path DATAMART_ROOT = Paths.get("datamart");

    private final Path weatherRoot = DATAMART_ROOT.resolve("weather").resolve("weatherFeeder");
    private final Path energyRoot = DATAMART_ROOT.resolve("energy").resolve("energyFeeder");

    public Recommendator() {
    }

    public void generateRecommendations() {
        Map<String, List<Double>> windSpeeds   = collectWindSpeeds();
        Map<String, List<String>> skyClouds     = collectClouds();
        Map<String, List<Double>> windEnergy   = collectEnergy(WIND_INDICATOR);
        Map<String, List<Double>> solarEnergy  = collectEnergy(SOLAR_INDICATOR);
        System.out.println("\n=== RECOMENDACIONES DE ENERGÍA ===");

        for (String city : windSpeeds.keySet()) {
            double avgSpeed    = average(windSpeeds.get(city));
            double avgWindGen  = average(windEnergy.getOrDefault(city, List.of()));
            if (avgWindGen == 0) continue;
            if (avgSpeed > WIND_SPEED_THRESHOLD && avgWindGen < WIND_GEN_THRESHOLD) {
                System.out.printf("🌪️ %s: velocidad media %.2f m/s y solo %.2f MWh ➜ Instalar MÁS aerogeneradores%n",
                        city, avgSpeed, avgWindGen);
            }
        }

        for (String city : skyClouds.keySet()) {
            String commonSky   = mostFrequent(skyClouds.get(city)).toLowerCase();
            double avgSolarGen = average(solarEnergy.getOrDefault(city, List.of()));
            if (avgSolarGen == 0) continue;
            if (!commonSky.contains("cloud") && avgSolarGen < SOLAR_GEN_THRESHOLD) {
                System.out.printf("☀️ %s: cielo habitual “%s” y solo %.2f MWh ➜ Instalar MÁS paneles solares%n",
                        city, commonSky, avgSolarGen);
            }
        }
    }

    private Map<String, List<Double>> collectWindSpeeds() {
        Map<String, List<Double>> map = new HashMap<>();
        try (Stream<Path> files = Files.walk(weatherRoot)) {
            files.filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> readWeatherFile(p, map, null));
        } catch (IOException e) {
            System.out.println("Error walking weather files: " + e.getMessage());
        }
        return map;
    }

    private Map<String, List<String>> collectClouds() {
        Map<String, List<String>> map = new HashMap<>();
        try (Stream<Path> files = Files.walk(weatherRoot)) {
            files.filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> readWeatherFile(p, null, map));
        } catch (IOException e) {
            System.out.println("Error walking weather files: " + e.getMessage());
        }
        return map;
    }

    private void readWeatherFile(Path file, Map<String, List<Double>> windMap, Map<String, List<String>> cloudMap) {
        try (Stream<String> lines = Files.lines(file).skip(1)) {
            lines.forEach(line -> {
                String[] parts = line.split(",", 4);
                if (parts.length < 4) return;

                String city = parts[1];
                String desc = parts[2];
                double wind = Double.parseDouble(parts[3]);

                if (windMap != null) {
                    windMap.computeIfAbsent(city, c -> new ArrayList<>()).add(wind);
                }

                if (cloudMap != null) {
                    cloudMap.computeIfAbsent(city, c -> new ArrayList<>()).add(desc);
                }
            });
        } catch (IOException e) {
            System.out.println("Error reading " + file + ": " + e.getMessage());
        }
    }

    private Map<String, List<Double>> collectEnergy(String indicatorFilter) {
        Map<String, List<Double>> map = new HashMap<>();
        try (Stream<Path> files = Files.walk(energyRoot)) {
            files.filter(p -> p.toString().endsWith(".csv"))
                    .forEach(p -> readEnergyFile(p, indicatorFilter, map));
        } catch (IOException e) {
            System.out.println("Error walking energy files: " + e.getMessage());
        }
        return map;
    }


    private void readEnergyFile(Path file, String indicatorFilter, Map<String, List<Double>> map) {
        try (Stream<String> lines = Files.lines(file).skip(1)) {
            lines.forEach(line -> {
                String[] parts = line.split(",", 4);
                if (parts.length < 4) return;
                String city      = parts[1];
                String indicator = parts[2];
                if (!indicator.equals(indicatorFilter)) return;
                double value     = Double.parseDouble(parts[3]);

                map.computeIfAbsent(city, c -> new ArrayList<>()).add(value);
            });
        } catch (IOException e) {
            System.out.println("Error reading " + file + ": " + e.getMessage());
        }
    }

    private double average(List<Double> list) {
        return list == null || list.isEmpty()
                ? 0.0
                : list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }

    private String mostFrequent(List<String> list) {
        return list == null || list.isEmpty()
                ? ""
                : list.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("");
    }
}