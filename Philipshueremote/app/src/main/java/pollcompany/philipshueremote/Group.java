package pollcompany.philipshueremote;

/**
 * Created by Ian on 7-8-2017.
 */

public class Group {
    private int groupID;
    private String groupName;
    private boolean isOnOff;

    public Group(int groupID, String groupName, boolean isOnOff) {
        this.groupID = groupID;
        this.groupName = groupName;
        this.isOnOff = isOnOff;
    }

    public int getGroupID() {
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
