package REDataApi;

public class REDataController {
    private final  REFeeder  feeder;
    public REDataController(REFeeder feeder) {
        this.feeder = feeder;
    }

    public REData getEnergyData() {
        return feeder.fetchEnergyData();
    }
}
