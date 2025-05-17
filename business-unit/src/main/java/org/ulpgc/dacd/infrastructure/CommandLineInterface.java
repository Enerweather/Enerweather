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
            System.out.println(""" 
                Ciudad: %s
                Temperatura: %.2f °C, %s
                Indicador: %s = %.2f %s (%s)
                        \\n""\",
                data.getWeather().getCityName(),
                data.getWeather().getTemperature(),
                data.getWeather().getDescription(),
                data.getEnergy().getIndicator(),
                
                data.getEnergy().getValue(),
                data.getEnergy().getUnit(),
                data.getEnergy().getTimestamp() """
            );
        }
    }
}