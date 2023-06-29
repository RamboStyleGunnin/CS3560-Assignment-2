import java.util.ArrayList;
import java.util.List;

public class CheckForInvalidIDsVisitor implements TwitterComponentVisitor{
    private boolean allValid=true;
    private List<String> uniqueIDs=new ArrayList<>();

    @Override
    public void visitUser(User user) {

        if(user.getUniqueID().contains(" ")||uniqueIDs.contains(user.getUniqueID())){
            allValid=false;
        }
        else {
            uniqueIDs.add(user.getUniqueID());
        }
    }

    @Override
    public void visitUserGroup(UserGroup userGroup) {
        if(allValid==true) {
            if (userGroup.getUniqueID().contains(" ") || uniqueIDs.contains(userGroup.getUniqueID())) {
                allValid = false;
            }
            else {
                uniqueIDs.add(userGroup.getUniqueID());
            }

            for (TwitterComponent member : userGroup.getGroupMembers()) {
                if(allValid==true) {
                    member.accept(this);
                }
            }
        }
    }

    public boolean areAllValid(){
        return allValid;
    }
}
