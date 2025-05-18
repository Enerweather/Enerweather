package org.ulpgc.dacd.enerweather.businessunit.view;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.ulpgc.dacd.enerweather.businessunit.util.DateUtils;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TableView {
    private static final Path DATAMART_ROOT = Paths.get("datamart");

    public void displayAvailableData() {
        System.out.println("\n=== Available Data Types ===");
        try (DirectoryStream<Path> topicDirs = Files.newDirectoryStream(DATAMART_ROOT)) {
            List<String> topics = new ArrayList<>();
            for (Path topicDir : topicDirs) {
                String topic = topicDir.getFileName().toString();
                topics.add(topic);
            }

            if (topics.isEmpty()) {
                System.out.println("No data available. Please check if datamart has been populated.");
                return;
            }

            for (int i = 0; i < topics.size(); i++) {
                System.out.println((i + 1) + ". " + topics.get(i));
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("\nSelect a data type (1-" + topics.size() + "): ");
            int selection = scanner.nextInt();

            if (selection > 0 && selection <= topics.size()) {
                String selectedTopic = topics.get(selection - 1);
                displayAvailableDates(selectedTopic);
            } else {
                System.out.println("Invalid selection.");
            }
        } catch (IOException e) {
            System.err.println("Error reading datamart: " + e.getMessage());
        }
    }

    private void displayAvailableDates(String topic) throws IOException {
        Path topicDir = DATAMART_ROOT.resolve(topic);

        System.out.println("\n=== Available Dates for " + topic + " ===");
        try (DirectoryStream<Path> dateFiles = Files.newDirectoryStream(topicDir, "*.csv")) {
            List<Path> files = new ArrayList<>();
            for (Path file : dateFiles) {
                files.add(file);
            }

            if (files.isEmpty()) {
                System.out.println("No data available for " + topic);
                return;
            }

            files.sort(Comparator.comparing(p -> p.getFileName().toString()));

            for (int i = 0; i < files.size(); i++) {
                String fileName = files.get(i).getFileName().toString();

                String date = DateUtils.formatBasicDate(fileName.substring(0, 8));
                System.out.println((i + 1) + ". " + date);
            }

            Scanner scanner = new Scanner(System.in);
            System.out.print("\nSelect a date (1-" + files.size() + "): ");
            int selection = scanner.nextInt();
            scanner.nextLine();

            if (selection > 0 && selection <= files.size()) {
                Path selectedFile = files.get(selection - 1);
                displayTableData(selectedFile, topic);
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }

    private void displayTableData(Path csvFile, String topic) {
        try (FileReader reader = new FileReader(csvFile.toFile());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            List<CSVRecord> records = csvParser.getRecords();
            if (records.isEmpty()) {
                System.out.println("File is empty.");
                return;
            }

            List<String> headers = csvParser.getHeaderNames();

            Map<String, Predicate<CSVRecord>> filters = getFilters(headers, topic);

            List<CSVRecord> filteredRecords = records;
            if (!filters.isEmpty()) {
                filteredRecords = records.stream()
                        .filter(record -> filters.values().stream()
                                .allMatch(predicate -> predicate.test(record)))
                        .collect(Collectors.toList());

                System.out.println("\nShowing " + filteredRecords.size() + " of " + records.size() + " records after filtering");
            }

            if (filteredRecords.isEmpty()) {
                System.out.println("No records match your filters.");
                return;
            }

            Map<String, Integer> columnWidths = calculateColumnWidths(headers, filteredRecords);

            String formatStr = buildFormatString(columnWidths);

            printTableDivider(columnWidths);
            System.out.printf(formatStr, headers.toArray());

            for (CSVRecord record : filteredRecords) {
                System.out.println();
                Object[] values = headers.stream()
                        .map(record::get)
                        .toArray();
                System.out.printf(formatStr, values);
            }
            System.out.println();

            printTableDivider(columnWidths);
            System.out.println("Total records: " + filteredRecords.size());

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }

    private Map<String, Predicate<CSVRecord>> getFilters(List<String> headers, String topic) {
        Map<String, Predicate<CSVRecord>> filters = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("\nAvailable filters (leave blank to skip):");

        if ("weather".equals(topic)) {
            System.out.print("City name: ");
            String cityFilter = scanner.nextLine().trim();
            if (!cityFilter.isEmpty()) {
                filters.put("cityName", record ->
                        record.get("cityName").toLowerCase().contains(cityFilter.toLowerCase()));
            }

            System.out.print("Description (clear sky, few clouds, scattered clouds, clouds, overcast clouds, light rain, rain): ");
            String descFilter = scanner.nextLine().trim();
            if (!descFilter.isEmpty()) {
                filters.put("description", record ->
                        record.get("description").toLowerCase().contains(descFilter.toLowerCase()));
            }

            System.out.print("Min wind speed: ");
            String minWindStr = scanner.nextLine().trim();
            if (!minWindStr.isEmpty()) {
                try {
                    double minWind = Double.parseDouble(minWindStr);
                    filters.put("minWind", record ->
                            Double.parseDouble(record.get("windSpeed")) >= minWind);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number format. Filter will be ignored.");
                }
            }
        } else if ("energy".equals(topic)) {
            System.out.print("Indicator (Wind, Solar Photovoltaic): ");
            String indicatorFilter = scanner.nextLine().trim();
            if (!indicatorFilter.isEmpty()) {
                filters.put("indicator", record ->
                        record.get("indicator").equalsIgnoreCase(indicatorFilter));
            }
        } else {
            System.out.println("Available fields: " + String.join(", ", headers));
            System.out.print("Field to filter: ");
            String field = scanner.nextLine().trim();

            if (!field.isEmpty() && headers.contains(field)) {
                System.out.print("Value to filter for: ");
                String value = scanner.nextLine().trim();

                if (!value.isEmpty()) {
                    filters.put(field, record ->
                            record.get(field).toLowerCase().contains(value.toLowerCase()));
                }
            }
        }

        return filters;
    }

    private Map<String, Integer> calculateColumnWidths(List<String> headers, List<CSVRecord> records) {
        Map<String, Integer> widths = new HashMap<>();

        for (String header : headers) {
            widths.put(header, header.length());
        }

        for (CSVRecord record : records) {
            for (String header : headers) {
                String value = record.get(header);
                int currentWidth = widths.get(header);
                widths.put(header, Math.max(currentWidth, value.length()));
            }
        }

        for (String header : headers) {
            widths.put(header, widths.get(header)*2 + 2);
        }

        return widths;
    }

    private String buildFormatString(Map<String, Integer> columnWidths) {
        return columnWidths.values().stream()
                .map(width -> "%-" + width + "s")
                .collect(Collectors.joining(""));
    }

    private void printTableDivider(Map<String, Integer> columnWidths) {
        int totalWidth = columnWidths.values().stream().mapToInt(Integer::intValue).sum();
        System.out.println("-".repeat(totalWidth));
    }
}