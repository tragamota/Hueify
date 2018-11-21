package my.philipshueremote.Database;

import android.arch.persistence.room.Entity;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

@Entity(primaryKeys = {"bridgeID", "lampApiID"})
public class Lamp {
    @NonNull
    private String bridgeID;
    private short lampApiID;

    private String lampType, lampName, lampManufacturer, lampProduct;
    private LightState state;

    public Lamp(@NonNull String bridgeID, short lampApiID) {
        this.bridgeID = bridgeID;
        this.lampApiID = lampApiID;
    }

    public String getBridgeID() {
        return bridgeID;
    }

    public short getLampApiID() {
        return lampApiID;
    }

    public String getLampType() {
        return lampType;
    }

    public String getLampName() {
        return lampName;
    }

    public String getLampManufacturer() {
        return lampManufacturer;
    }

    public String getLampProduct() {
        return lampProduct;
    }

    public LightState getState() {
        return state;
    }

    public Lamp setLampType(String lampType) {
        this.lampType = lampType;
        return this;
    }

    public Lamp setLampName(String lampName) {
        this.lampName = lampName;
        return this;
    }

    public Lamp setLampManufacturer(String lampManufacturer) {
        this.lampManufacturer = lampManufacturer;
        return this;
    }

    public Lamp setLampProduct(String lampProduct) {
        this.lampProduct = lampProduct;
        return this;
    }

    public Lamp setState(LightState state) {
        this.state = state;
        return this;
    }

    public static Lamp parseFromJson(String bridgeID, short lampApiID, JSONObject lampObject) throws JSONException {
        Lamp returnLamp = new Lamp(bridgeID, lampApiID)
                .setLampName(lampObject.getString("name"))
                .setLampType(lampObject.getString("type"))
                .setLampProduct(lampObject.getString("productname"))
                .setLampManufacturer(lampObject.getString("manufacturername"));

        //todo: Need to update state object from lamp
        returnLamp.getState();

        return returnLamp;
    }
}
