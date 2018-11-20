package my.philipshueremote.Init.Models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

@Entity
public class BridgeInfo implements Parcelable {
    private String ipAddress;
    private int portNumber;

    private String bridgeAccessKey;

    private String bridgeName;
    private String bridgeVersion;
    private String bridgeMacAddress;

    @PrimaryKey @NonNull
    private String bridgeID;


    public BridgeInfo(String ipAddress, int portNumber,
                      String bridgeName, String bridgeVersion,
                      String bridgeMacAddress, @NonNull String bridgeID) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.bridgeName = bridgeName;
        this.bridgeVersion = bridgeVersion;
        this.bridgeMacAddress = bridgeMacAddress;
        this.bridgeID = bridgeID;
    }

    public static BridgeInfo BridgeInfo(String ipAddress, int port, JSONObject bridgeObject) throws JSONException {
        String bridgeName, bridgeVersion, bridgeMacAddress, bridgeID;
        bridgeName = bridgeObject.getString("name");
        bridgeMacAddress = bridgeObject.getString("mac");
        bridgeVersion = bridgeObject.has("apiversion") ?
                bridgeObject.getString("apiversion") :
                "Unknown api version";
        bridgeID = bridgeObject.has("bridgeid") ?
                bridgeObject.getString("bridgeid") :
                "Unknown bridge ID";

        return new BridgeInfo(ipAddress, port, bridgeName, bridgeVersion, bridgeMacAddress, bridgeID);
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public String getBridgeName() {
        return bridgeName;
    }

    public String getBridgeVersion() {
        return bridgeVersion;
    }

    public String getBridgeMacAddress() {
        return bridgeMacAddress;
    }

    public String getBridgeID() {
        return bridgeID;
    }

    public String getBridgeAccessKey() {
        return bridgeAccessKey;
    }

    public void setBridgeAccessKey(String accessKey) {
        bridgeAccessKey = accessKey;
    }

    private BridgeInfo(Parcel in) {
        ipAddress = in.readString();
        portNumber = in.readInt();
        bridgeName = in.readString();
        bridgeVersion = in.readString();
        bridgeMacAddress = in.readString();
        bridgeID = in.readString();
        bridgeAccessKey = in.readString();
    }

    public static final Creator<BridgeInfo> CREATOR = new Creator<BridgeInfo>() {
        @Override
        public BridgeInfo createFromParcel(Parcel in) {
            return new BridgeInfo(in);
        }

        @Override
        public BridgeInfo[] newArray(int size) {
            return new BridgeInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ipAddress);
        parcel.writeInt(portNumber);
        parcel.writeString(bridgeName);
        parcel.writeString(bridgeVersion);
        parcel.writeString(bridgeMacAddress);
        parcel.writeString(bridgeID);
        parcel.writeString(bridgeAccessKey);
    }
}
