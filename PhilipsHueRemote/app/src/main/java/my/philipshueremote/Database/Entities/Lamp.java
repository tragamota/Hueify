package my.philipshueremote.Database.Entities;

import android.arch.persistence.room.Embedded;
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
    @Embedded
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

    public void setLampType(String lampType) {
        this.lampType = lampType;
    }

    public void setLampName(String lampName) {
        this.lampName = lampName;
    }

    public void setLampManufacturer(String lampManufacturer) {
        this.lampManufacturer = lampManufacturer;
    }

    public void setLampProduct(String lampProduct) {
        this.lampProduct = lampProduct;
    }

    public void setState(LightState state) {
        this.state = state;
    }

    public static Lamp parseFromJson(String bridgeID, short lampApiID, JSONObject lampObject) throws JSONException {
        Lamp returnLamp = new Lamp(bridgeID, lampApiID);
        returnLamp.setLampName(lampObject.getString("name"));
        returnLamp.setLampType(lampObject.getString("type"));
        if(lampObject.has("productname")) {
            returnLamp.setLampProduct(lampObject.getString("productname"));
        }
        if(lampObject.has("manufacturername")) {
            returnLamp.setLampManufacturer(lampObject.getString("manufacturername"));
        }

        returnLamp.setState(LightState.parseFromJson(returnLamp.getLampType(), lampObject.getJSONObject("state")));

        return returnLamp;
    }
}
