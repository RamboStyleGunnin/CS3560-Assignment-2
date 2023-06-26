import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;
import java.util.List;

// This renderer will set a nodes color based on its unique ID, Red is for Groups and Blue is for Users
public class CustomTreeCellRenderer extends DefaultTreeCellRenderer {
    private List<String> groupIDs;
    private List<String> userIDs;

    public CustomTreeCellRenderer(List<String> groupIDs, List<String> userIDs) {
        this.groupIDs = groupIDs;
        this.userIDs = userIDs;
    }

    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

        if (value instanceof DefaultMutableTreeNode) {
            Object userObject= ((DefaultMutableTreeNode) value).getUserObject();
            if (userObject instanceof String) {
                String uniqueID = (String) userObject;
                if (groupIDs.contains(uniqueID)) {
                    setForeground(Color.RED);
                } else if (userIDs.contains(uniqueID)) {
                    setForeground(Color.BLUE);
                }
            }
        }
        return this;
    }
}
