package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerTypeStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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

        myJpanel.add(new JLabel("Double click on any blocker below to edit its probabilities"), BorderLayout.NORTH);

        String[] columnNames = {"Blocker Name", "Encounter Chance (%)", "Resolve Chance (%)"};

        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable blockersTable = new JTable(tableModel);

        refreshTableData(tableModel);

        blockersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = blockersTable.getSelectedRow();
                    if (row != -1) {
                        // Do something when a row is clicked
                        System.out.println("Row " + row + " clicked.");
                        // For example, get the value of the first column (ID)
                        Object id = blockersTable.getValueAt(row, 0);
                        int encounterChance = (int) blockersTable.getValueAt(row, 1);
                        int resolveChance = (int) blockersTable.getValueAt(row, 2);
                        System.out.println("ID: " + id);

                        EditBlockerProbabilities form = new EditBlockerProbabilities((String) id, encounterChance, resolveChance);
                        form.setVisible(true);
                        form.addWindowListener(new WindowAdapter() {
                            @Override
                            public void windowClosed(WindowEvent e) {
                                refreshTableData(tableModel);
                            }
                        });
                    }
                }
            }
        });

        blockersTable.setFillsViewportHeight(true);
        blockersTable.setAutoCreateRowSorter(true);
        blockersTable.getTableHeader().setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(blockersTable);

        myJpanel.add(scrollPane, BorderLayout.CENTER);

        add(myJpanel);
    }

    private static void refreshTableData(DefaultTableModel tableModel) {
        int i = tableModel.getRowCount() - 1;
        while (i > -1) {
            tableModel.removeRow(i);
            i--;
        }
        BlockerTypeStore.get().getBlockerTypes().forEach(blockerType -> {
            Object[] rowData = {
                blockerType.getName(),
                blockerType.getEncounterChance(),
                blockerType.getResolveChance()
            };
            tableModel.addRow(rowData);
        });
    }
}
