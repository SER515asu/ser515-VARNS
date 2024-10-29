package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.UserAction;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.UserRolePermissions;
import com.groupesan.project.java.scrumsimulator.mainpackage.core.UserRoleSingleton;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStoryStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.UserStoryWidget;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;

public class UserStoryListPane extends JFrame implements BaseComponent {

    JFrame parent;

    public UserStoryListPane(JFrame parent) {
        this.init();
        this.parent = parent;
    }

    private final List<UserStoryWidget> widgets = new ArrayList<>();
    private final JPanel subPanel = new JPanel();
    private boolean isEditWindowOpen;

    private void reloadUserStories() {
        widgets.clear();
        subPanel.removeAll();

        for (UserStory userStory : UserStoryStore.getInstance().getUserStories()) {
            UserStoryWidget userStoryWidget = new UserStoryWidget(userStory, true, this)
                    .setCloseEditDialogActionListener(
                            e -> reloadUserStories());
            userStoryWidget.init();
            widgets.add(userStoryWidget);
        }

        int i = 0;
        for (UserStoryWidget widget : widgets) {
            subPanel.add(
                    widget,
                    new CustomConstraints(
                            0,
                            i++,
                            GridBagConstraints.WEST,
                            1.0,
                            0.1,
                            GridBagConstraints.HORIZONTAL));
        }

        subPanel.revalidate();
        subPanel.repaint();
    }

    private ActionListener handleNewUserStoryAction() {
        return e -> {
            NewUserStoryForm form = new NewUserStoryForm(this);
            form.setVisible(true);
            form.setAlwaysOnTop(true);

            form.addWindowListener(
                    new java.awt.event.WindowAdapter() {
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            reloadUserStories();
                        }
                    });
        };
    }

    private ActionListener handleInitializeUserStoriesAction() {
        return e -> {
            UserStoryStore.getInstance().initializeUserStories();
            reloadUserStories();
        };
    }

    public void disableWindow() {
        isEditWindowOpen = true;
        setEnabled(false);
    }

    public void enableWindow() {
        isEditWindowOpen = false;
        setEnabled(true);
    }

    public void init() {
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setTitle("Product Backlog");

        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(new GridBagLayout());

        subPanel.setLayout(new GridBagLayout());

        reloadUserStories();

        myJpanel.add(
                new JScrollPane(subPanel),
                new CustomConstraints(
                        0, 0, GridBagConstraints.WEST, 1.0, 0.8, GridBagConstraints.HORIZONTAL));


        if (UserRolePermissions.actionAllowed(UserRoleSingleton.getInstance().getUserRole(), UserAction.MANAGE_USER_STORES)) {
            JButton newUserStory = new JButton("New User Story");
            newUserStory.addActionListener(handleNewUserStoryAction());
            myJpanel.add(
                    newUserStory,
                    new CustomConstraints(
                            0, 1, GridBagConstraints.WEST, 1.0, 0.2, GridBagConstraints.HORIZONTAL));

            JButton initializeUserStories = new JButton("Add Default User Stories");
            initializeUserStories.addActionListener(handleInitializeUserStoriesAction());
            myJpanel.add(
                    initializeUserStories,
                    new CustomConstraints(
                            0, 2, GridBagConstraints.WEST, 1.0, 0.2, GridBagConstraints.HORIZONTAL
                    )
            );
        }

        add(myJpanel);
    }

    public boolean isEditWindowOpen() {
        return isEditWindowOpen;
    }
}
