package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;
import com.groupesan.project.java.scrumsimulator.mainpackage.utils.CustomConstraints;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PotentialBlockersPane extends JFrame implements BaseComponent {
    public PotentialBlockersPane() {
        this.init();
    }

    @Override
    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Potential Blocker list");
        setSize(400, 300);

        GridBagLayout myGridbagLayout = new GridBagLayout();
        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(myGridbagLayout);

        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridBagLayout());

        List<JPanel> panels = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            JPanel panel = new JPanel();
            GridBagLayout myGridBagLayout = new GridBagLayout();
            panel.setLayout(myGridBagLayout);
            panels.add(panel);
        }

        panels.getFirst().add(new JLabel("10% Chance of System Failure"), new CustomConstraints(
                0, 0, GridBagConstraints.WEST, 0.1, 0.0, GridBagConstraints.HORIZONTAL));
        panels.get(1).add(new JLabel("5% Chance of Architecture Failure"), new CustomConstraints(
                0, 0, GridBagConstraints.WEST, 0.1, 0.0, GridBagConstraints.HORIZONTAL));
        panels.get(2).add(new JLabel("1% of Total Failure"), new CustomConstraints(
                0, 0, GridBagConstraints.WEST, 0.1, 0.0, GridBagConstraints.HORIZONTAL));

        for (int i = 0; i < panels.size(); i++) {
            subPanel.add(panels.get(i), new CustomConstraints(
                    0,
                    i,
                    GridBagConstraints.WEST,
                    1.0,
                    0.1,
                    GridBagConstraints.HORIZONTAL));
        }

        myJpanel.add(
                new JScrollPane(subPanel),
                new CustomConstraints(
                        0, 0, GridBagConstraints.WEST, 1.0, 0.8, GridBagConstraints.HORIZONTAL));

        add(myJpanel);
    }
}
