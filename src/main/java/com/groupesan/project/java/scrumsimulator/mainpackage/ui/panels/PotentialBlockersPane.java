package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerType;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerTypeStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.BaseComponent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PotentialBlockersPane extends JFrame implements BaseComponent {

    private static DefaultTableModel tableModel;
    private static JTable blockersTable;

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

        String[] columnNames = { "Blocker Name", "Encounter Chance (%)", "Resolve Chance (%)", "" };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        blockersTable = new JTable(tableModel);

        blockersTable.getColumn("").setCellRenderer(new ButtonRenderer());
        blockersTable.getColumn("").setCellEditor(new ButtonEditor(new JCheckBox()));

        refreshTableData();

        blockersTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1
                        && blockersTable.getSelectedRow() != -1) {
                    int row = blockersTable.getSelectedRow();
                    if (row != -1) {
                        // Do something when a row is clicked
                        System.out.println("Row " + row + " clicked.");
                        // For example, get the value of the first column (ID)
                        Object id = blockersTable.getValueAt(row, 0);
                        int encounterChance = (int) blockersTable.getValueAt(row, 1);
                        int resolveChance = (int) blockersTable.getValueAt(row, 2);
                        System.out.println("ID: " + id);

                        openEditForm(id, encounterChance, resolveChance);
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

    private static void openEditForm(Object id, int encounterChance, int resolveChance) {
        EditBlockerProbabilities form = new EditBlockerProbabilities((String) id, encounterChance, resolveChance);
        form.setVisible(true);
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refreshTableData();
            }
        });
    }

    private static void refreshTableData() {
        int i = tableModel.getRowCount() - 1;
        while (i > -1) {
            tableModel.removeRow(i);
            i--;
        }

        BlockerTypeStore.get().getBlockerTypes().forEach(blockerType -> {
            Object[] rowData = {
                    blockerType.getName(),
                    blockerType.getEncounterChance(),
                    blockerType.getResolveChance(),
                    "Actions"
            };
            tableModel.addRow(rowData);
        });
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {

        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "Actions" : value.toString());
            return this;
        }
    }
    
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "Actions" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                BlockerType blockerType = BlockerTypeStore.get().getBlockerType((String) tableModel.getValueAt(blockersTable.getSelectedRow(), 0));
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem editItem = new JMenuItem("Edit");
                JMenuItem deleteItem = new JMenuItem("Delete");
                JMenuItem duplicateItem = new JMenuItem("Duplicate");

                editItem.addActionListener(e -> openEditForm(blockerType.getName(),
                        blockerType.getEncounterChance(), blockerType.getResolveChance()));
                deleteItem.addActionListener(e -> {
                    BlockerTypeStore.get().removeBlocker(blockerType);
                    refreshTableData();
                });
                duplicateItem.addActionListener(e -> {
                    BlockerType duplicate = new BlockerType(blockerType.getName() + " - Copy",
                            blockerType.getEncounterChance(), blockerType.getResolveChance());
                    BlockerTypeStore.get().addBlockerType(duplicate);
                    refreshTableData();
                });

                popupMenu.add(editItem);
                popupMenu.add(deleteItem);
                popupMenu.add(duplicateItem);

                popupMenu.show(button, button.getWidth(), button.getHeight());
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }

        @Override
        protected void fireEditingStopped() {
            super.fireEditingStopped();
        }
    }
}
