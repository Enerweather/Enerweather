package org.ulpgc.dacd;

import org.ulpgc.dacd.application.Service;
import org.ulpgc.dacd.infrastructure.CommandLineInterface;
import org.ulpgc.dacd.infrastructure.adapters.DatamartReader;
import org.ulpgc.dacd.infrastructure.adapters.EventLoader;

public class Controller {
    public void startCli() {
        Service service = new Service(new DatamartReader(), new EventLoader());
        CommandLineInterface cli = new CommandLineInterface(service);
        cli.run();
    }
}
