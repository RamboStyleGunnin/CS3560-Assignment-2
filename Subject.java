public interface Subject {
        void attachFollower(Observer follower);
        void notifyFollowers(String newTweet);
}
