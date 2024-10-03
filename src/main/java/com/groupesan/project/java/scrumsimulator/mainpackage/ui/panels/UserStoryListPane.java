package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStoryStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.UserStoryWidget;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;

public class UserStoryListPane extends JFrame implements BaseComponent {
    public UserStoryListPane() {
        this.init();
    }

    private final List<UserStoryWidget> widgets = new ArrayList<>();
    private final JPanel subPanel = new JPanel();

    private void reloadUserStories() {
        widgets.clear();
        subPanel.removeAll();

        for (UserStory userStory : UserStoryStore.getInstance().getUserStories()) {
            UserStoryWidget userStoryWidget = new UserStoryWidget(userStory, true).setCloseEditDialogActionListener(
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
            NewUserStoryForm form = new NewUserStoryForm();
            form.setVisible(true);

            form.addWindowListener(
                    new java.awt.event.WindowAdapter() {
                        public void windowClosed(
                                java.awt.event.WindowEvent windowEvent) {
                            UserStory userStory = form.getUserStoryObject();
                            UserStoryStore.getInstance().addUserStory(userStory);
                            reloadUserStories();
                        }
                    });
        };
    }

    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("User Story list");
        setSize(400, 300);

        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(new GridBagLayout());

        // demo/testing widgets
        // UserStory aUserStory =
        // UserStoryFactory.getInstance().createNewUserStory("foo",
        // "bar", 2);
        // UserStory aUserStory2 =
        // UserStoryFactory.getInstance().createNewUserStory("foo2", "bar2", 4);
        // widgets.add(new UserStoryWidget(aUserStory));
        // widgets.add(new UserStoryWidget(aUserStory2));

        subPanel.setLayout(new GridBagLayout());

        reloadUserStories();

        myJpanel.add(
                new JScrollPane(subPanel),
                new CustomConstraints(
                        0, 0, GridBagConstraints.WEST, 1.0, 0.8, GridBagConstraints.HORIZONTAL));

        JButton newUserStory = new JButton("New User Story");
        newUserStory.addActionListener(handleNewUserStoryAction());
        myJpanel.add(
                newUserStory,
                new CustomConstraints(
                        0, 1, GridBagConstraints.WEST, 1.0, 0.2, GridBagConstraints.HORIZONTAL));

        add(myJpanel);
    }
}
