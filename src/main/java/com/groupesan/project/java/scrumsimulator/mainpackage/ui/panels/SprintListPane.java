package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.Sprint;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SprintFactory;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.SprintStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.SprintWidget;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;

public class SprintListPane extends JFrame implements BaseComponent {

    private JFrame parent;
    private List<SprintWidget> widgets = new ArrayList<>();
    private JPanel glassPane;

    public SprintListPane(JFrame parent) {
        this.parent = parent;
        this.init();
        setupGlassPane();
    }

    public void init() {
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Sprints List");

        GridBagLayout myGridbagLayout = new GridBagLayout();
        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(myGridbagLayout);

        Sprint aSprint = SprintFactory.getSprintFactory().createNewSprint("foo", "bar", 2);
        Sprint aSprint2 = SprintFactory.getSprintFactory().createNewSprint("foo2", "bar2", 4);
        widgets.add(new SprintWidget(aSprint));
        widgets.add(new SprintWidget(aSprint2));

        for (Sprint sprint : SprintStore.getInstance().getSprints()) {
            widgets.add(new SprintWidget(sprint));
        }

        JPanel subPanel = new JPanel(new GridBagLayout());
        int i = 0;
        for (SprintWidget widget : widgets) {
            subPanel.add(widget, new CustomConstraints(
                    0, i++, GridBagConstraints.WEST, 1.0, 0.1, GridBagConstraints.HORIZONTAL));
        }

        myJpanel.add(new JScrollPane(subPanel), new CustomConstraints(
                0, 0, GridBagConstraints.WEST, 1.0, 0.8, GridBagConstraints.HORIZONTAL));

        JButton newSprintButton = new JButton("New Sprint");
        newSprintButton.addActionListener(e -> {
            NewSprintForm form = new NewSprintForm();
            form.setVisible(true);
            form.setAlwaysOnTop(true);
            setGlassPaneVisible(true);
            setAlwaysOnTop(false);

            form.addWindowListener(new WindowAdapter() {
                public void windowClosed(WindowEvent windowEvent) {
                    Sprint newSprint = form.getSprintObject();
                    if (newSprint != null) {
                        SprintWidget newWidget = new SprintWidget(newSprint);
                        widgets.add(newWidget);
                        subPanel.add(newWidget, new CustomConstraints(
                                0, widgets.size() - 1, GridBagConstraints.WEST, 1.0, 0.1,
                                GridBagConstraints.HORIZONTAL));
                        subPanel.revalidate();
                        subPanel.repaint();
                    }
                    setGlassPaneVisible(false);
                    setAlwaysOnTop(true);
                }
            });
        });

        myJpanel.add(newSprintButton, new CustomConstraints(
                0, 1, GridBagConstraints.WEST, 1.0, 0.2, GridBagConstraints.HORIZONTAL));

        add(myJpanel);
    }

    private void setupGlassPane() {
        glassPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 100));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        glassPane.addMouseListener(new MouseAdapter() {
        });
        glassPane.addMouseMotionListener(new MouseAdapter() {
        });
        glassPane.setOpaque(false);
        setGlassPane(glassPane);
    }

    private void setGlassPaneVisible(boolean visible) {
        getGlassPane().setVisible(visible);
        getGlassPane().repaint();
    }
}
