import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminControlPanel extends JFrame {
    private final List<User> users = new ArrayList<>();
    private final List<String> groupIDs = new ArrayList<>();
    private final List<String> userIDs = new ArrayList<>();
    private final JLabel analysisLabel = new JLabel();
    private final JTextField userIdTextBox = new JTextField(15);
    private final JTextField groupIdTextBox = new JTextField(15);
    private final JButton addUserButton = new JButton("Add User");
    private final JButton addGroupButton = new JButton("Add Group");
    private final JButton userViewButton = new JButton("User View");
    private final JButton getNumOfUsersButton = new JButton("Get Number of Users");
    private final JButton getNumOfGroupsButton = new JButton("Get Number of Groups");
    private final JButton getNumOfTweetsButton = new JButton("Get Number of Tweets");
    private final JButton getPercentageOfPositiveTweetsButton = new JButton("Get Percentage of Positive Tweets");
    private final TwitterTreeNode root = new TwitterTreeNode("Root", new UserGroup("Root"));
    private final DefaultTreeModel userTreeModel = new DefaultTreeModel(root);
    private final JTree userTree = new JTree(userTreeModel);
    private final CustomTreeCellRenderer renderer = new CustomTreeCellRenderer(groupIDs, userIDs);
    private TwitterComponentVisitor visitor;
    private static AdminControlPanel instance;

    public static AdminControlPanel getInstance() { //setting up Singleton Pattern
        if (instance == null) {
            instance = new AdminControlPanel();
        }
        return instance;
    }

    private AdminControlPanel() { //private constructor for Singleton Pattern
        render();
        groupIDs.add("Root");
        analysisLabel.setText("Awaiting Analysis Command");
        anticipateUserInput();
    }
    
    private void anticipateUserInput() {

        addUserButton.addActionListener(e -> {
            String nodeText = userIdTextBox.getText();
            nodeText = nodeText.trim();
            if (nodeText.isEmpty()) {
                analysisLabel.setText("Unique ID must be at least 1 non-whitespace character");
                return;
            }
            if (userIDs.contains(nodeText) || groupIDs.contains(nodeText)) {
                analysisLabel.setText("This ID is already in use. Choose a different ID and try again.");
            }
            else {
                TwitterTreeNode selectedNode = (TwitterTreeNode) userTree.getLastSelectedPathComponent();
                if (selectedNode == null || selectedNode == root) {
                    userIDs.add(nodeText);
                    TwitterTreeNode newNode = new TwitterTreeNode(nodeText, new User(nodeText));
                    root.add(newNode);
                    ((UserGroup)root.getType()).add(newNode.getType());
                    users.add((User) newNode.getType());
                    analysisLabel.setText("New user \"" + nodeText + "\" has joined Twitter.");
                    TreePath path = new TreePath(root);
                    userTree.expandPath(path);
                }
                else if (selectedNode.getType() instanceof UserGroup) {
                    userIDs.add(nodeText);
                    TwitterTreeNode newNode = new TwitterTreeNode(nodeText, new User(nodeText));
                    selectedNode.add(newNode);
                    ((UserGroup)selectedNode.getType()).add(newNode.getType());
                    users.add((User) newNode.getType());
                    userTreeModel.nodeStructureChanged(selectedNode);
                    TreePath path = new TreePath(selectedNode.getPath());
                    userTree.expandPath(path);
                    analysisLabel.setText("New user \"" + nodeText +
                            "\" has joined Twitter and was added as a member of \"" + selectedNode + "\".");
                }
                else {
                    analysisLabel.setText("Cannot add user as a member of another user.");
                }
            }
            userIdTextBox.setText("");
            userTree.updateUI();
        });

        addGroupButton.addActionListener(e -> {
            String nodeText = groupIdTextBox.getText();
            nodeText=nodeText.trim();
            if(nodeText.isEmpty()){
                analysisLabel.setText("Unique ID must be at least 1 non whitespace character");
                return;
            }
            if (userIDs.contains(nodeText)|| groupIDs.contains(nodeText)) {//same list
                analysisLabel.setText("This ID is already in use. Choose a different ID and try again.");
            }
            else {
                TwitterTreeNode selectedNode = (TwitterTreeNode) userTree.getLastSelectedPathComponent();
                if(selectedNode==null||selectedNode==root){
                    groupIDs.add(nodeText);
                    TwitterTreeNode newNode = new TwitterTreeNode(nodeText, new UserGroup(nodeText));
                    root.add(newNode);
                    ((UserGroup)root.getType()).add(newNode.getType());
                    analysisLabel.setText("New group \""+nodeText+"\" added to Twitter.");
                    TreePath path = new TreePath(root);
                    userTree.expandPath(path);
                }
                else if (selectedNode.getType() instanceof UserGroup){
                    groupIDs.add(nodeText);
                    TwitterTreeNode newNode = new TwitterTreeNode(nodeText, new UserGroup(nodeText));
                    selectedNode.add(newNode);
                    ((UserGroup)selectedNode.getType()).add(newNode.getType());
                    userTreeModel.nodeStructureChanged(selectedNode);
                    TreePath path = new TreePath(selectedNode.getPath());
                    userTree.expandPath(path);
                    analysisLabel.setText("New group \""+nodeText+
                            "\" added to Twitter as a sub-group of \""+selectedNode+"\".");
                }
                else {
                    analysisLabel.setText("Cannot add group as a sub-group of a user.");
                }
            }
            groupIdTextBox.setText("");
            userTree.updateUI();
        });

        userViewButton.addActionListener(e -> {
            TwitterTreeNode selectedNode = (TwitterTreeNode) userTree.getLastSelectedPathComponent();
            //if no user is selected
            if(selectedNode==null||selectedNode==root){
                analysisLabel.setText("No user selected.");
            }

            //if valid user has been selected
            else if(selectedNode.getType() instanceof User) {

                    // if not yet done, create the users page for first time
                if(((User)selectedNode.getType()).getMyPage()==null){
                    SwingUtilities.invokeLater(() -> {
                        UserViewPanel userViewPanel = new UserViewPanel((User) selectedNode.getType());
                        userViewPanel.setVisible(true);
                        analysisLabel.setText("User view opened in another window.");
                    });
                }

                    //otherwise just open the existing instance for this user
                else{
                    ((User)selectedNode.getType()).getMyPage().setVisible(true);
                    analysisLabel.setText("User view opened in another window.");
                }
            }

            //a group has been selected instead of a user
            else{
                analysisLabel.setText("Cannot display a User View for a group.");
            }
        });

        //calculate number of users on Twitter
        getNumOfUsersButton.addActionListener(e -> {
            visitor = new CountUsersVisitor();
            root.getType().accept(visitor);
            String output = Integer.toString(((CountUsersVisitor) visitor).getNumOfUsers());
            analysisLabel.setText("Total Number of Users = "+output);
        });

        //calculate number of groups on Twitter. Root group does not count
        getNumOfGroupsButton.addActionListener(e -> {
            visitor = new CountGroupsVisitor();
            root.getType().accept(visitor);
            String output = Integer.toString(((CountGroupsVisitor) visitor).getNumOfGroups());
            analysisLabel.setText("Total Number of Groups = "+output);
        });

        //calculate total number of tweets on Twitter
        getNumOfTweetsButton.addActionListener(e -> {
            visitor=new CountTweetsVisitor();
            root.getType().accept(visitor);
            String output = Integer.toString(((CountTweetsVisitor) visitor).getNumOfTweets());
            analysisLabel.setText("Total Number of Tweets = "+output);
        });

        //calculate percentage of total tweets that contain positive words. Words are defined in the new "visitor"
        getPercentageOfPositiveTweetsButton.addActionListener(e -> {
            visitor=new getPercentageOfPositiveTweetsVisitor();
            root.getType().accept(visitor);
            String output=Double.toString(((getPercentageOfPositiveTweetsVisitor) visitor).getPercentage());
            if(output.length()>5){
                output=output.substring(0,5);
            }
            analysisLabel.setText("Total Percentage of Positive Tweets = "+output+"%");
        });
    }

    //handle requests from users to follow other users
    public Boolean followRequest(Observer requester, String userID){
      for(User user: users){
          if(user.getUniqueID().equals(userID)){
              if(((User)requester).getUsersImFollowing().contains(user)){ //already following this user
                  return false;
              }
              if(((User)requester).getUniqueID().equals(userID)){ // cant follow yourself
                  return false;
              }
              //ID match, link the subject and observer
              user.attachFollower(requester);
              ((User) requester).follow(user);
              return true;
          }
      }
      return false;
    }

    //contains all the code to render the Admin Control Panel window
    private void render(){

        setTitle("Admin Control Panel");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 500));
        pack();
        setLocationRelativeTo(null);

        // Create the main content panel that will hold a left and a right panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Create the left panel
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        leftPanel.setPreferredSize(new Dimension(160,160));
        mainPanel.add(leftPanel, BorderLayout.WEST);
        userTree.setCellRenderer(renderer);
        userTree.setPreferredSize(new Dimension(156,156));
        leftPanel.add(userTree, BorderLayout.CENTER);

        // Create the right panel
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        // Create the top panel inside the right panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        rightPanel.add(topPanel, BorderLayout.NORTH);
        JPanel topOfTopPanel= new JPanel();//Create the top part of the top panel of the right panel
        topOfTopPanel.add(userIdTextBox);
        topOfTopPanel.add(addUserButton);
        topOfTopPanel.add(groupIdTextBox);
        topOfTopPanel.add(addGroupButton);
        topPanel.add(topOfTopPanel, BorderLayout.NORTH);
        JPanel bottomOfTopPanel= new JPanel();//Create the bottom part of the top panel of the right panel
        bottomOfTopPanel.add(userViewButton);
        topPanel.add(bottomOfTopPanel, BorderLayout.CENTER);
        //create center right panel
        JPanel centerPanel = new JPanel();
        rightPanel.add(centerPanel, BorderLayout.CENTER);
        centerPanel.add(analysisLabel);
        // Create the bottom panel inside the right panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2,2,5,5));
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.GREEN, 2));
        bottomPanel.setPreferredSize(new Dimension(100,180));
        bottomPanel.add(getNumOfUsersButton);
        bottomPanel.add(getNumOfGroupsButton);
        bottomPanel.add(getNumOfTweetsButton);
        bottomPanel.add(getPercentageOfPositiveTweetsButton);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
}