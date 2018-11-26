package my.philipshueremote.Database.Entities;

import android.arch.persistence.room.TypeConverters;

import org.json.JSONException;
import org.json.JSONObject;

import static my.philipshueremote.Database.Entities.LampLightStates.LAMP_COLOR_LIGHT_STATE;
import static my.philipshueremote.Database.Entities.LampLightStates.LAMP_COLOR_TEMPERATURE_LIGHT_STATE;
import static my.philipshueremote.Database.Entities.LampLightStates.LAMP_DIMMABLE_LIGHT_STATE;
import static my.philipshueremote.Database.Entities.LampLightStates.LAMP_EXTENDED_COLOR_LIGHT_STATE;
import static my.philipshueremote.Database.Entities.LampLightStates.LAMP_ON_OFF_LIGHT_STATE;

@TypeConverters({LampLightStatesConverter.class, XYColorspaceConverter.class})
public class LightState {
    private LampLightStates lightType;

    private boolean on;
    private boolean reachable;
    private String controlMode;

    private int hue;
    private short saturation;
    private short brightness;

    private float[] xy;
    private int colorTemperature;

    //on/off
    private LightState(LampLightStates lightType, boolean on, boolean reachable) {
        this.lightType = lightType;
        this.on = on;
        this.reachable = reachable;
    }

    //dimmable
    private LightState(LampLightStates lightType, boolean on, boolean reachable, short brightness) {
        this(lightType, on , reachable);
        this.brightness = brightness;
    }

    //color temperature
    private LightState(LampLightStates lightType, boolean on, boolean reachable,
                       String controlMode, int colorTemperature ) {
        this(lightType, on, reachable);
        this.controlMode = controlMode;
        this.colorTemperature = colorTemperature;
    }

    //color light
    private LightState(LampLightStates lightType, boolean on, boolean reachable,
                       String controlMode, int hue, short saturation, short brightness, float xy[]) {
        this(lightType, on, reachable, brightness);
        this.controlMode = controlMode;
        this.hue = hue;
        this.saturation = saturation;
        this.xy = xy;
    }

    //extended colorLight
    public LightState(LampLightStates lightType, boolean on, boolean reachable,
                       String controlMode, int hue, short saturation, short brightness, float xy[],
                       int colorTemperature) {
        this(lightType, on, reachable, controlMode, hue, saturation, brightness, xy);
        this.colorTemperature = colorTemperature;
    }

    public LampLightStates getLightType() {
        return lightType;
    }

    public boolean isOn() {
        return on;
    }

    public boolean isReachable() {
        return reachable;
    }

    public String getControlMode() {
        return controlMode;
    }

    public int getHue() {
        return hue;
    }

    public short getSaturation() {
        return saturation;
    }

    public short getBrightness() {
        return brightness;
    }

    public float[] getXy() {
        return xy;
    }

    public int getColorTemperature() {
        return colorTemperature;
    }

    public void setLightType(LampLightStates lightType) {
        this.lightType = lightType;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void setReachable(boolean reachable) {
        this.reachable = reachable;
    }

    public void setControlMode(String controlMode) {
        this.controlMode = controlMode;
    }

    public void setHue(int hue) {
        this.hue = hue;
    }

    public void setSaturation(short saturation) {
        this.saturation = saturation;
    }

    public void setBrightness(short brightness) {
        this.brightness = brightness;
    }

    public void setXy(float[] xy) {
        this.xy = xy;
    }

    public void setColorTemperature(int colorTemperature) {
        this.colorTemperature = colorTemperature;
    }

    public static LightState parseFromJson(String lampType, JSONObject stateObject) throws JSONException {
        LightState returnLightState = null;

        LampLightStates lightState = parseLampTypeToState(lampType);
        boolean on, reachable;
        short saturation, brightness;
        int hue, colorTemperature;
        float xy[];
        String colorMode;

        on = stateObject.getBoolean("on");
        reachable = stateObject.getBoolean("reachable");

        switch (lightState) {
            case LAMP_ON_OFF_LIGHT_STATE:
                returnLightState = new LightState(lightState, on, reachable);
                break;
            case LAMP_DIMMABLE_LIGHT_STATE:
                brightness = (short) stateObject.getInt("bri");
                returnLightState = new LightState(lightState, on, reachable, brightness);
                break;
            case LAMP_COLOR_TEMPERATURE_LIGHT_STATE:
                colorMode = stateObject.getString("colormode");
                colorTemperature = stateObject.getInt("ct");
                returnLightState = new LightState(lightState, on, reachable, colorMode, colorTemperature);
                break;
            case LAMP_COLOR_LIGHT_STATE:
                hue = stateObject.getInt("hue");
                saturation = (short) stateObject.getInt("sat");
                brightness = (short) stateObject.getInt("bri");
                colorMode = stateObject.getString("colormode");
                xy = new float[] {(float) stateObject.getJSONArray("xy").getDouble(0),
                        (float) stateObject.getJSONArray("xy").getDouble(1)};
                returnLightState = new LightState(lightState, on, reachable,
                        colorMode, hue, saturation, brightness, xy);
                break;
            case LAMP_EXTENDED_COLOR_LIGHT_STATE:
                hue = stateObject.getInt("hue");
                saturation = (short) stateObject.getInt("sat");
                brightness = (short) stateObject.getInt("bri");
                colorMode = stateObject.getString("colormode");
                xy = new float[] {(float) stateObject.getJSONArray("xy").getDouble(0),
                        (float) stateObject.getJSONArray("xy").getDouble(1)};
                colorTemperature = stateObject.getInt("ct");
                returnLightState = new LightState(lightState, on, reachable,
                        colorMode, hue, saturation, brightness, xy, colorTemperature);
        }

        return returnLightState;
    }

    private static LampLightStates parseLampTypeToState(String lampType) {
        LampLightStates lightState;
        switch (lampType) {
            case "Extended color light":
                lightState = LAMP_EXTENDED_COLOR_LIGHT_STATE;
                break;
            case "Color light":
                lightState = LAMP_COLOR_LIGHT_STATE;
                break;
            case "Color temperature light":
                lightState = LAMP_COLOR_TEMPERATURE_LIGHT_STATE;
                break;
            case "Dimmable light":
                lightState = LAMP_DIMMABLE_LIGHT_STATE;
                break;

                default:
                    lightState = LAMP_ON_OFF_LIGHT_STATE;
        }
        return lightState;
    }
}
