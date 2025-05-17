package org.ulpgc.dacd.infrastructure;

import java.util.Scanner;
import org.ulpgc.dacd.application.Service;
import org.ulpgc.dacd.application.domain.model.CombinedData;

public class CommandLineInterface {
    private final Service service;
    public CommandLineInterface(final Service service) {
        this.service = service;
    }

    public void run(){
        Scanner scanner = new Scanner(System.in);
        while(true){
            System.out.println("ciudad: ");
            String city = scanner.nextLine();
            System.out.println("nombre: ");
            String indicator = scanner.nextLine();

            CombinedData data = service.getCombinedData(city, indicator);
            System.out.printf(""" 
                Ciudad: %s
                Viento: %.2f km/h
                Descripción: %s 
                Fecha del dato: %s
                
                Indicador: %s
                Valor: %.2f
                Fecha del dato: %s
                
                %n
                """,
                data.getWeather().getCityName(),
                data.getWeather().getDescription(),
                data.getWeather().getWindSpeed(),
                data.getWeather().getDate(),
                
                data.getEnergy().getValue(),
                data.getEnergy().getIndicator(),
                data.getEnergy().getTimestamp()
            );
        }
    }
}