public class CountGroupsVisitor implements TwitterComponentVisitor{

    int numOfGroups = 0;

    @Override
    public void visitUser(User user) {
        //visitor pattern interface sometimes forces unnecessary methods on a visitor class
        // this is one of those methods
    }

    @Override
    public void visitUserGroup(UserGroup userGroup) {
        for(TwitterComponent member: userGroup.getGroupMembers()){
            if(member instanceof UserGroup){
                numOfGroups++;
                member.accept(this);
            }
        }
    }

    public int getNumOfGroups(){
        return numOfGroups;
    }
}
