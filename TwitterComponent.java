public interface TwitterComponent {
    String getUniqueID();
    void accept(TwitterComponentVisitor visitor);
}
