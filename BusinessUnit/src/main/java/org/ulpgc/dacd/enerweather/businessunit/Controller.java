package org.ulpgc.dacd.enerweather.businessunit;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.ulpgc.dacd.enerweather.businessunit.store.CsvEventWriter;
import org.ulpgc.dacd.enerweather.businessunit.store.JsonMessageListener;
import org.ulpgc.dacd.enerweather.businessunit.view.TableView;

import javax.jms.*;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static java.util.Map.entry;


public class Controller {
    private static final String BROKER_URL = "tcp://localhost:61616";
    private static final String CLIENT_ID = "BusinessUnitClient";
    private static final Path EVENTSTORE_ROOT = Paths.get("eventstore");
    private static final Path DATAMART_ROOT = Paths.get("datamart");

    private Connection connection;
    private Session session;
    private final TableView tableView = new TableView();

    private static final Map<String, Integer> CITY_POPULATIONS = Map.ofEntries(
            entry("Madrid", 3422416),
            entry("Barcelona", 1686208),
            entry("Valencia", 824340),
            entry("Seville", 686741),
            entry("Zaragoza", 691037),
            entry("Málaga", 592346),
            entry("Murcia", 471982),
            entry("Palma", 438234),
            entry("Las Palmas de Gran Canaria", 383516),
            entry("Bilbao", 350000),
            entry("Alicante", 358608),
            entry("Córdoba", 326039),
            entry("Valladolid", 299265),
            entry("Vigo", 296692),
            entry("Gijón", 271717),
            entry("Vitoria-Gasteiz", 253996),
            entry("La Coruña", 247604),
            entry("Granada", 233648),
            entry("Elche", 234765),
            entry("Oviedo", 219910),
            entry("Badalona", 223166),
            entry("Cartagena", 216108),
            entry("Terrassa", 223627),
            entry("Jerez de la Frontera", 213105),
            entry("Sabadell", 216520),
            entry("Móstoles", 210309),
            entry("Santa Cruz de Tenerife", 209194),
            entry("Alcalá de Henares", 197562)
    );


    public void start() {
        try {
            rebuildDatamart();
            connectToBroker();
            System.out.println("BusinessUnit connected and listening on topics.");
            startCommandLoop();
        } catch (JMSException e) {
            System.err.println("Initial connection failed: " + e.getMessage());
            retryOnce();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void retryOnce() {
        try {
            Thread.sleep(3000);
            connectToBroker();
            System.out.println("Reconnected successfully.");
            startCommandLoop();
        } catch (Exception ex) {
            System.err.println("Retry failed: " + ex.getMessage());
        }
    }

    private void startCommandLoop() {
        Thread commandThread = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            boolean running = true;

            while (running) {
                displayMenu();
                String command = scanner.nextLine().trim();

                switch (command) {
                    case "1" -> tableView.displayAvailableData();
                    case "2" -> rebuildDatamartAndNotify();
                    case "q", "Q", "exit", "quit" -> {
                        running = false;
                        stop();
                        System.out.println("Exiting...");
                        System.exit(0);
                    }
                    default -> System.out.println("Unknown command. Please try again.");
                }
            }
        });

        commandThread.setDaemon(true);
        commandThread.start();
    }

    private void displayMenu() {
        System.out.println("\n=== Enerweather BusinessUnit ===");
        System.out.println("1. View Data");
        System.out.println("2. Rebuild Datamart");
        System.out.println("q. Quit");
        System.out.print("\nSelect an option: ");
    }

    private void rebuildDatamartAndNotify() {
        try {
            System.out.println("Rebuilding datamart...");
            rebuildDatamart();
            System.out.println("Datamart rebuilt successfully.");
        } catch (IOException e) {
            System.err.println("Failed to rebuild datamart: " + e.getMessage());
        }
    }

    private void rebuildDatamart() throws IOException {
        CsvEventWriter replayWriter = new CsvEventWriter();
        Gson gson = new Gson();

        if(Files.exists(DATAMART_ROOT)) {
            try (Stream<Path> walk = Files.walk(DATAMART_ROOT)) {
                walk.sorted(Comparator.reverseOrder())
                        .forEach(p -> {
                            try {
                                Files.delete(p);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        } else {
            Files.createDirectories(DATAMART_ROOT);
        }

        if (Files.exists(EVENTSTORE_ROOT)) {
            try (DirectoryStream<Path> topics = Files.newDirectoryStream(EVENTSTORE_ROOT)) {
                for (Path topicDir : topics) {
                    String topic = topicDir.getFileName().toString();
                    try (DirectoryStream<Path> days = Files.newDirectoryStream(topicDir, "*.events")) {
                        for (Path eventsFile : days) {
                            Files.lines(eventsFile).forEach(line -> {
                                JsonObject evt = gson.fromJson(line, JsonObject.class);
                                replayWriter.handleEvent(topic, evt);
                            });
                        }
                    }
                }
            }
        }
    }

    private void connectToBroker() throws JMSException {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(BROKER_URL);
        connection = factory.createConnection();
        connection.setClientID(CLIENT_ID);
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        subscribe("weather");
        subscribe("energy");

        connection.start();
    }

    private void subscribe(String topicName) throws JMSException {
        Topic topic = session.createTopic(topicName);
        MessageConsumer consumer = session.createDurableSubscriber(topic, topicName);
        consumer.setMessageListener(new JsonMessageListener());
    }

    public void stop() {
        try {
            if (session != null) session.close();
            if (connection != null) connection.close();
        } catch (JMSException e) {
            System.err.println("Error closing JMS resources: " + e.getMessage());
        }
    }
}
