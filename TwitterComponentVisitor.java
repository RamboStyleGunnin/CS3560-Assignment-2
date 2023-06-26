public interface TwitterComponentVisitor {
    void visitUser(User user);
    void visitUserGroup(UserGroup userGroup);
}
