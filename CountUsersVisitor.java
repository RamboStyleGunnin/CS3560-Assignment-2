public class CountUsersVisitor implements TwitterComponentVisitor {

    private int numOfUsers=0;

    @Override
    public void visitUser(User user) {
        numOfUsers++;
    }

    @Override
    public void visitUserGroup(UserGroup userGroup) {
        for(TwitterComponent member: userGroup.getGroupMembers()) {
            member.accept(this);
        }
    }

    public int getNumOfUsers(){
        return numOfUsers;
    }
}
