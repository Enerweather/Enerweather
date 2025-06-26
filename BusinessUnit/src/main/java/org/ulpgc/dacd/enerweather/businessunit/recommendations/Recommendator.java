package org.ulpgc.dacd.enerweather.businessunit.recommendations;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Recommendator {
    private static final Path DATAMART_ROOT = Paths.get("datamart");

    private final Path weatherRoot = DATAMART_ROOT.resolve("weather").resolve("weatherFeeder");
    private final Path energyRoot = DATAMART_ROOT.resolve("energy").resolve("energyFeeder");

    public Recommendator() {
    }

    public void generateRecommendations() {
        generateWindRecommendations();
        generateSolarRecommendations();
    }

    public void generateSolarRecommendations() {
        Map<String, List<String>> skyClouds     = collectClouds();
        Map<String, List<Double>> solarEnergy  = collectEnergy("Solar photovoltaic");

        Map<String, Integer> cloudPriority = new HashMap<>();
        cloudPriority.put("clear sky", 5);
        cloudPriority.put("few clouds", 4);
        cloudPriority.put("scattered clouds", 3);
        cloudPriority.put("broken clouds", 2);
        cloudPriority.put("overcast clouds", 1);

        Map<String, Double> skyScorePerCity = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : skyClouds.entrySet()) {
            String city = entry.getKey();
            List<String> clouds = entry.getValue();

            double avgScore = clouds.stream()
                    .mapToInt(desc -> cloudPriority.getOrDefault(desc, 0))
                    .average().orElse(0);

            skyScorePerCity.put(city, avgScore);
        }

        System.out.println("🔆 Top 3 cities with the most solar energy to exploit:");

        skyScorePerCity.entrySet().stream()
                .filter(e -> solarEnergy.containsKey(e.getKey()))
                .map(e -> Map.entry(
                        e.getKey(),
                        e.getValue() - average(solarEnergy.get(e.getKey())) / 10000.0
                ))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(3)
                .forEach(e -> {
                    String city = e.getKey();

                    double avgEnergy = average(solarEnergy.get(city));

                    String mostCommonSky = skyClouds.get(city).stream()
                            .collect(Collectors.groupingBy(s -> s, Collectors.counting()))
                            .entrySet().stream()
                            .max(Map.Entry.comparingByValue())
                            .map(Map.Entry::getKey)
                            .orElse("Unknown");

                    System.out.printf("- %s: %.2f kWh, Most common sky: %s %n", city, avgEnergy, mostCommonSky);
                });
    }

    public void generateWindRecommendations() {
        Map<String, List<Double>> windSpeeds   = collectWindSpeeds();
        Map<String, List<Double>> windEnergy   = collectEnergy("Wind");

        Map<String, Double> avgWindSpeed = windSpeeds.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> average(e.getValue()) / 2
                ));

        System.out.println("🌬️ Top 3 cities with the most aeolic energy to exploit:");

        avgWindSpeed.entrySet().stream()
                .filter(e -> windEnergy.containsKey(e.getKey()))
                .map(e -> Map.entry(
                        e.getKey(),
                        e.getValue() - average(windEnergy.get(e.getKey())) / 10000
                ))
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(3)
                .forEach(e -> System.out.printf("- %s: %.2f kWh, Avg Wind Speed: %.2f %n",
                        e.getKey(),
                        average(windEnergy.get(e.getKey())),
                        avgWindSpeed.get(e.getKey())));

        System.out.println();
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
}