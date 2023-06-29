public class GetLastUpdatedUserVisitor implements TwitterComponentVisitor{
    private String lastUpdatedUser;
    private long newestUpdateTime=0;

    @Override
    public void visitUser(User user) {
        if(user.getLastUpdatedTime()>newestUpdateTime){
            newestUpdateTime= user.getLastUpdatedTime();
            lastUpdatedUser=user.getUniqueID();
        }
    }

    @Override
    public void visitUserGroup(UserGroup userGroup) {
        for(TwitterComponent member: userGroup.getGroupMembers()) {
            member.accept(this);
        }
    }

    public String getLastUpdatedUser(){
        return lastUpdatedUser;
    }
}
