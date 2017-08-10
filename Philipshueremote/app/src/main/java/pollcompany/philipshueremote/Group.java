package pollcompany.philipshueremote;

import java.io.Serializable;

/**
 * Created by Ian on 7-8-2017.
 */

public class Group implements Serializable {
    private String groupID;
    private String groupName;
    private boolean isOnOff;

    public Group(String groupID, String groupName) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.isOnOff = false;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isOnOff() {
        return isOnOff;
    }

    public void setOnOff(boolean onOff) {
        isOnOff = onOff;
    }
}
