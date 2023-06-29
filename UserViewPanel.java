import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;

public class UserViewPanel extends JFrame {
    private final JTextField userIdTextBox= new JTextField(15);
    private final JTextField newTweetTextBox = new JTextField(50);
    private final JList<String> currentlyFollowingListBox=new JList<>();
    private final JList<String> newsFeedListBox=new JList<>();
    private final DefaultListModel<String> followingListModel = new DefaultListModel<>();
    private final DefaultListModel<String> newsFeedListModel = new DefaultListModel<>();
    private final JTextArea creationTimeTextArea = new JTextArea();
    private final JTextArea lastUpdatedTimeTextArea = new JTextArea();
    private final JButton postTweetButton = new JButton("Post New Tweet");
    private final JButton followUserButton = new JButton("Follow User");
    private final Comparator<Tweet> chronologicalOrder = Comparator.comparingInt(Tweet::getChronology);
    private final User viewingAsUser;
    private final JScrollPane followingScrollPane = new JScrollPane(currentlyFollowingListBox);
    private final JScrollPane newsFeedScrollPane = new JScrollPane(newsFeedListBox);

    public UserViewPanel(User user) {
        viewingAsUser = user;
        viewingAsUser.setMyPage(this);
        render();
        creationTimeTextArea.setText(":User Creation Time:\n    "+Long.toString(user.getCreationTime()));
        creationTimeTextArea.setRows(2);
        awaitInput();
    }

    public void refreshNewsFeed(){
        Collections.sort(viewingAsUser.getNewsFeed(), chronologicalOrder);
        newsFeedListModel.clear();
        newsFeedListModel.addElement("Here is your News Feed:   Newest to Oldest:");
        for(Tweet tweet: viewingAsUser.getNewsFeed()){
            newsFeedListModel.add(1,tweet.getTweet());
        }
        viewingAsUser.setLastUpdatedTime();
        lastUpdatedTimeTextArea.setText(":Last Updated Time:\n   "+Long.toString(viewingAsUser.getLastUpdatedTime()));
        lastUpdatedTimeTextArea.setRows(2);
    }

    private void showSubjects(){
        followingListModel.clear();
        followingListModel.addElement("Displaying users you follow:");
        for (User subject : viewingAsUser.getUsersImFollowing()) {
            followingListModel.addElement(subject.getUniqueID());
        }
    }

    private void awaitInput() {

        postTweetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String newTweet = newTweetTextBox.getText();
                newTweet = newTweet.trim();
                if (!newTweet.isEmpty()) {
                    newTweet = viewingAsUser.getUniqueID() + ": " + newTweet;
                    viewingAsUser.notifyFollowers(newTweet);
                    newTweetTextBox.setText("");
                    refreshNewsFeed();
                }
            }
        });

        followUserButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (AdminControlPanel.getInstance().followRequest(viewingAsUser, userIdTextBox.getText())) {
                    showSubjects();
                }
                userIdTextBox.setText("");
                refreshNewsFeed();
            }
        });
    }

    private void render(){
        setTitle("Viewing as "+ viewingAsUser.getUniqueID());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 550));
        pack();
        setLocationRelativeTo(null);

        // Create the main content panel that will hold a top and a bottom panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        setContentPane(mainPanel);

        // Create the top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        topPanel.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        mainPanel.add(topPanel, BorderLayout.NORTH);
        JPanel topOfTopPanel=new JPanel();
        topOfTopPanel.setLayout(new FlowLayout());
        topOfTopPanel.add(userIdTextBox);
        topOfTopPanel.add(followUserButton, BorderLayout.EAST);
        topPanel.add(topOfTopPanel, BorderLayout.NORTH);
        JPanel bodyOfTopPanel =new JPanel();
        bodyOfTopPanel.setLayout(new FlowLayout());
        bodyOfTopPanel.add(followingScrollPane);
        currentlyFollowingListBox.setVisibleRowCount(10);
        currentlyFollowingListBox.setFixedCellWidth(500);
        currentlyFollowingListBox.setFixedCellHeight(20);
        currentlyFollowingListBox.setModel(followingListModel);
        topPanel.add(bodyOfTopPanel,BorderLayout.CENTER);
        showSubjects();

        // Create the bottom panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        mainPanel.add(bottomPanel);
        JPanel topOfBottomPanel=new JPanel();
        topOfBottomPanel.setLayout(new FlowLayout());
        topOfBottomPanel.add(newTweetTextBox);
        topOfBottomPanel.add(postTweetButton);
        bottomPanel.add(topOfBottomPanel, BorderLayout.NORTH);
        JPanel bodyOfBottomPanel =new JPanel();
        bodyOfBottomPanel.setLayout(new FlowLayout());
        newsFeedListBox.setVisibleRowCount(10);
        newsFeedListBox.setFixedCellWidth(500);
        newsFeedListBox.setFixedCellHeight(20);
        newsFeedListBox.setModel(newsFeedListModel);
        bodyOfBottomPanel.add(newsFeedScrollPane);
        bottomPanel.add(bodyOfBottomPanel,BorderLayout.CENTER);
        JPanel bottomOfBottomPanel =new JPanel();
        bottomOfBottomPanel.add(creationTimeTextArea);
        bottomOfBottomPanel.add(lastUpdatedTimeTextArea);
        bottomPanel.add(bottomOfBottomPanel,BorderLayout.SOUTH);
        refreshNewsFeed();
    }
}
