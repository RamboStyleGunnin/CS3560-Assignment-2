public class Tweet {
    private final String tweetPost;
    private final User tweeter;
    private final int chronology;
    private static int numOfTweets=0;

    public Tweet(String tweetPostPar, User tweeterPar) {
        tweetPost = tweetPostPar;
        tweeter = tweeterPar;
        chronology=numOfTweets;
    }

    public String getTweet() { //returns the String associated with the Tweet
        return tweetPost;
    }

    public int getChronology(){ //the higher the number, the newer the tweet
        return chronology;
    }

    public User getTweeter() { //returns the User object associated with the Tweet
        return tweeter;
    }
}
