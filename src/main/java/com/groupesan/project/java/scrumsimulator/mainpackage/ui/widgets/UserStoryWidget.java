package com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.UserStory;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels.EditUserStoryForm;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;
import java.awt.Cursor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UserStoryWidget extends JPanel implements BaseComponent {

    JLabel id;
    JLabel points;
    JLabel name;
    JLabel desc;
    JLabel businessValuePoint; // added buisness value point

    // TODO: This is a non transient field and this class is supposed to be
    // serializable. this needs to be dealt with before this object can be
    // serialized
    private UserStory userStory;

    ActionListener actionListener = e -> {
    };

    MouseAdapter openEditDialog = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            EditUserStoryForm form = new EditUserStoryForm(userStory);
            form.setVisible(true);

            form.addWindowListener(
                    new java.awt.event.WindowAdapter() {
                        public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                            actionListener.actionPerformed(null);
                            init();
                        }
                    });
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            setCursor(new Cursor(java.awt.Cursor.HAND_CURSOR));
            setBackground(java.awt.Color.LIGHT_GRAY);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            setCursor(new Cursor(java.awt.Cursor.DEFAULT_CURSOR));
            setBackground(null);
        }
    };

    public UserStoryWidget(UserStory userStory) {
        this.userStory = userStory;
    }

    public UserStoryWidget setCloseEditDialogActionListener(ActionListener actionListener) {
        this.actionListener = actionListener;
        return this;
    }

    public void init() {
        removeAll();

        if (this.getMouseListeners().length == 0)
            this.addMouseListener(openEditDialog);

        id = new JLabel(userStory.getId().toString());
        points = new JLabel(Double.toString(userStory.getPointValue()));
        name = new JLabel(userStory.getName());
        desc = new JLabel(userStory.getDescription());
        businessValuePoint = new JLabel(Integer.toString(userStory.getBusinessValuePoint()));

        GridBagLayout myGridBagLayout = new GridBagLayout();

        setLayout(myGridBagLayout);

        add(
                id,
                new CustomConstraints(
                        0, 0, GridBagConstraints.WEST, 0.1, 0.0, GridBagConstraints.HORIZONTAL));
        add(
                points,
                new CustomConstraints(
                        1, 0, GridBagConstraints.WEST, 0.1, 0.0, GridBagConstraints.HORIZONTAL));
        add(
                name,
                new CustomConstraints(
                        2, 0, GridBagConstraints.WEST, 0.2, 0.0, GridBagConstraints.HORIZONTAL));
        add(
                desc,
                new CustomConstraints(
                        3, 0, GridBagConstraints.WEST, 0.7, 0.0, GridBagConstraints.HORIZONTAL));
        add(businessValuePoint,
                new CustomConstraints(
                        4, 0, GridBagConstraints.WEST, 0.1, 0.0, GridBagConstraints.HORIZONTAL));

        revalidate();
        repaint();
    }
}
