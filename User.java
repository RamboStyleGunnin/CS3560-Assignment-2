import java.util.ArrayList;
import java.util.List;

public class User implements Observer, Subject, TwitterComponent {
    private final String uniqueID;
    private final long creationTime;
    private long lastUpdatedTime;
    private final List<Observer> followers = new ArrayList<>();//people who follow this user
    private final List<User> usersImFollowing = new ArrayList<>();//people this user follows
    private final List<Tweet> newsFeed = new ArrayList<>();//holds tweets from this user and everyone this user follows
    private final List<Tweet> myTweets =new ArrayList<>();//holds tweets from this user only
    private UserViewPanel myPage = null;

    public User(String uniqueIDPar) { //Constructor
        uniqueID = uniqueIDPar;//Admin should make sure ID is unique before User is constructed
        creationTime=System.currentTimeMillis();
    }

    @Override
    public void attachFollower(Observer follower) {
        followers.add(follower);
    }

    @Override
    public void notifyFollowers(String newTweet) {
        Tweet tweet = new Tweet(newTweet,this);
        newsFeed.add(tweet);
        myTweets.add(tweet);
        for(Observer follower : followers) {
            follower.update(tweet);
        }
    }

    @Override
    public void update(Tweet tweet) {
        newsFeed.add(tweet);
        myPage.refreshNewsFeed();
    }

    @Override
    public String getUniqueID(){
        return uniqueID;
    }

    @Override
    public void accept(TwitterComponentVisitor visitor) {
        visitor.visitUser(this);
    }

    @Override
    public long getCreationTime() {
        return creationTime;
    }

    public long getLastUpdatedTime() {
        return lastUpdatedTime;
    }

    public void setLastUpdatedTime(){
        lastUpdatedTime=System.currentTimeMillis();
    }

    public void follow(User newSubject){
        usersImFollowing.add(newSubject);
        for (Tweet tweet : newSubject.getMyTweets()){
            update(tweet);
        }
    }

    public List<Tweet> getMyTweets(){
        return myTweets;
    }

    public  List<Tweet> getNewsFeed(){
        return newsFeed;
    }

    public UserViewPanel getMyPage() {
        return myPage;
    }

    public void setMyPage(UserViewPanel myPage) {
        this.myPage = myPage;
    }

    public List<User> getUsersImFollowing() {
        return usersImFollowing;
    }

    public List<Observer> getFollowers() { //not used in assignment 2
        return followers;
    }
}
