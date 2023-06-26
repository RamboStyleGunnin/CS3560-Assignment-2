import javax.swing.tree.DefaultMutableTreeNode;

//These nodes will have a type associated with them. User or UserGroup
public class TwitterTreeNode extends DefaultMutableTreeNode {
    private TwitterComponent type;

    public TwitterTreeNode(Object userObject, TwitterComponent type) {
        super(userObject);
        this.type = type;
    }

    public TwitterComponent getType() {
        return type;
    }
}
