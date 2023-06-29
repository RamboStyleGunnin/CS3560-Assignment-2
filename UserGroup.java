import java.util.ArrayList;
import java.util.List;

public class UserGroup implements TwitterComponent {
    private final String uniqueID;
    private final List<TwitterComponent> members=new ArrayList<>();
    private final long creationTime;

    public UserGroup(String uniqueIDPar) {
        uniqueID = uniqueIDPar;//Admin should make sure ID is unique before User is constructed
        creationTime=System.currentTimeMillis();
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public List<TwitterComponent> getGroupMembers() {
        return members;
    }

    public void add(TwitterComponent twitterComponentPar) {
        members.add(twitterComponentPar);
    }

    public void accept(TwitterComponentVisitor visitor) {
        visitor.visitUserGroup(this);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }
}
