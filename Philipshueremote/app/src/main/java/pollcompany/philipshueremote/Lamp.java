package pollcompany.philipshueremote;

import java.io.Serializable;

/**
 * Created by Ian on 21-7-2017.
 */

public class Lamp implements Serializable{
    private boolean onOff;
    private String id;
    private String name;
    private String type;
    private int brightness;
    private int hue;
    private int saturation;

    public Lamp(boolean onOff, String id, String name, String type, int brightness, int hue, int saturation) {
        this.onOff = onOff;
        this.id = id;
        this.name = name;
        this.type = type;
        this.brightness = brightness;
        this.hue = hue;
        this.saturation = saturation;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrightness() {
        return brightness;
    }

    public void setBrightness(int brightness) {
        this.brightness = brightness;
    }

    public int getHue() {
        return hue;
    }

    public void setHue(int hue) {
        this.hue = hue;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }
}
