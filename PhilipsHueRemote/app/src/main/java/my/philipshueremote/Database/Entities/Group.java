package my.philipshueremote.Database.Entities;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;

import org.json.JSONObject;

@Entity(primaryKeys = {"bridgeID", "groupID"})
public class Group {
    private String bridgeID;
    private short groupID;

    private String groupName, groupType;
    private short[] lightsInGroup;

    @Embedded
    private GroupLightState groupState;

    public Group(String bridgeID, short groupID) {

    }

    public String getBridgeID() {
        return bridgeID;
    }

    public short getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupType() {
        return groupType;
    }

    public short[] getLightsInGroup() {
        return lightsInGroup;
    }

    public GroupLightState getGroupState() {
        return groupState;
    }

    public void setBridgeID(String bridgeID) {
        this.bridgeID = bridgeID;
    }

    public void setGroupID(short groupID) {
        this.groupID = groupID;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupType(String groupType) {
        this.groupType = groupType;
    }

    public void setLightsInGroup(short[] lightsInGroup) {
        this.lightsInGroup = lightsInGroup;
    }

    public void setGroupState(GroupLightState groupState) {
        this.groupState = groupState;
    }

    public static Group parseFromJson(String bridgeID, short groupID, JSONObject groupObject) {
        Group returnValue = new Group(bridgeID, groupID);

        return returnValue;
    }
}
