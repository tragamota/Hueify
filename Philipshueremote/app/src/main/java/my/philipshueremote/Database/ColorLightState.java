package my.philipshueremote.Database;

public class ColorLightState extends LightState {
    private short hue;
    private byte sat;
    private byte brightness;

    public ColorLightState(boolean on, boolean reachable) {
        super(on, reachable);
    }
}
