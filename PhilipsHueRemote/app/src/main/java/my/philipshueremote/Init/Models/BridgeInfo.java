package my.philipshueremote.Init.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class BridgeInfo implements Parcelable {
    private String ipAddress;
    private int portNumber;

    private String bridgeName;
    private String bridgeVersion;
    private String bridgeMacAddress;
    private String bridgeID;

    public BridgeInfo(String ipAddress, int portNumber,
                      String bridgeName, String bridgeVersion,
                      String bridgeMacAddress, String bridgeID) {
        this.ipAddress = ipAddress;
        this.portNumber = portNumber;
        this.bridgeName = bridgeName;
        this.bridgeVersion = bridgeVersion;
        this.bridgeMacAddress = bridgeMacAddress;
        this.bridgeID = bridgeID;
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

    private BridgeInfo(Parcel in) {
        ipAddress = in.readString();
        portNumber = in.readInt();
        bridgeName = in.readString();
        bridgeVersion = in.readString();
        bridgeMacAddress = in.readString();
        bridgeID = in.readString();
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
    }
}
