package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerTypeStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;

public class PotentialBlockersPane extends JFrame implements BaseComponent {
    public PotentialBlockersPane() {
        this.init();
    }

    @Override
    public void init() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Potential Blocker List");
        setSize(600, 400);

        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(new BorderLayout());

        String[] columnNames = {"Blocker Name", "Encounter Chance (%)", "Resolve Chance (%)"};
        
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable blockersTable = new JTable(tableModel);

        BlockerTypeStore.get().getBlockerTypes().forEach(blockerType -> {
            Object[] rowData = {
                blockerType.getName(),
                blockerType.getEncounterChance(),
                blockerType.getResolveChance()
            };
            tableModel.addRow(rowData);
        });

        blockersTable.setFillsViewportHeight(true);
        blockersTable.setAutoCreateRowSorter(true);
        blockersTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(blockersTable);

        myJpanel.add(scrollPane, BorderLayout.CENTER);

        add(myJpanel);
    }
}
