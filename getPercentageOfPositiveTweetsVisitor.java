public class getPercentageOfPositiveTweetsVisitor implements TwitterComponentVisitor{
    private int numberOfPositiveTweets=0;
    private int totalNumOfTweets=0;
    private double percentageOfPositiveTweets;
    private final String[] positiveWords = {"cute", "love", "pretty", "beautiful",
                                            "ponies", "kittens", "thanks", "prayers"};
    @Override
    public void visitUser(User user) {
        for(Tweet tweet: user.getMyTweets()) {
            for(int i = 0; i< positiveWords.length; i++){
                if(tweet.getTweet().contains(positiveWords[i])){
                    numberOfPositiveTweets++;
                    break;
                }
            }
            totalNumOfTweets++;
        }
    }

    @Override
    public void visitUserGroup(UserGroup userGroup) {
        for(TwitterComponent member: userGroup.getGroupMembers()){
            member.accept(this);
        }
    }

    public double getPercentage(){
        if(totalNumOfTweets==0) {
            return 0.0;
        }
        percentageOfPositiveTweets = ((double) numberOfPositiveTweets /totalNumOfTweets)*100;
        return percentageOfPositiveTweets;
    }
}
