package pollcompany.philipshueremote;

import android.graphics.Color;

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
    private int hsvColor;

    public Lamp(boolean onOff, String id, String name, String type, int brightness, int hue, int saturation) {
        this.onOff = onOff;
        this.id = id;
        this.name = name;
        this.type = type;
        this.brightness = brightness;
        this.hue = hue;
        this.saturation = saturation;
        hsvColor = calculateHSV(0.9f);
    }

    public int calculateHSV(float alpha) {
        int color;
        float hsv[] = new float[3];
        if(hue != 0 && saturation != 0 && brightness != 0) {
            hsv[0] = (hue / 65535f) * 360f;
            hsv[1] = (saturation / 255f);
            hsv[2] = (brightness / 255f);
        }
        else {
            return 0;
        }

        color = Color.HSVToColor((int) (alpha*255), hsv);
        return color;
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

    public boolean isOnOff() {
        return onOff;
    }

    public void setOnOff(boolean onOff) {
        this.onOff = onOff;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHsvColor() {
        return hsvColor;
    }

    public void setHsvColor(int hsvColor) {
        this.hsvColor = hsvColor;
    }
}
