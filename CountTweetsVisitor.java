public class CountTweetsVisitor implements TwitterComponentVisitor{

    int numOfTweets=0;

    @Override
    public void visitUser(User user) {
        for(Tweet tweet: user.getMyTweets()){
            numOfTweets++;
        }
    }

    @Override
    public void visitUserGroup(UserGroup userGroup) {
        for(TwitterComponent member: userGroup.getGroupMembers()) {
            member.accept(this);
        }
    }

    public int getNumOfTweets(){
        return numOfTweets;
    }
}
