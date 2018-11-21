package my.philipshueremote.Database;

import android.arch.persistence.room.Entity;

@Entity
public class Lamp {
    private short lampApiID;
    private String bridgeID;
    private String lampType, lampName, lampManufacturer, lampProduct;
    private LightState state;

    public Lamp(short lampApiID, String bridgeID, ) {

    }
}
