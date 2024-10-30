package com.groupesan.project.java.scrumsimulator.mainpackage.ui.panels;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.BlockerType;
import com.groupesan.project.java.scrumsimulator.mainpackage.impl.BlockerTypeStore;
import com.groupesan.project.java.scrumsimulator.mainpackage.state.SimulationSingleton;
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

public class SimulationHistoryPane extends JFrame implements BaseComponent {
    private DefaultTableModel tableModel;
    private JTable simulationsTable;
    private JPanel glassPane;
    private JFrame parent;

    public SimulationHistoryPane(JFrame parent) {
        this.parent = parent;
        this.init();
        setupGlassPane();
    }

    @Override
    public void init() {
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("All Simulations List");

        JPanel myJpanel = new JPanel();
        myJpanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        myJpanel.setLayout(new BorderLayout());

        myJpanel.add(new JLabel("Double click on any simulation below to set as the current simulation context"), BorderLayout.NORTH);

        JButton addBlockerButton = new JButton("Add Simulation");
        addBlockerButton.addActionListener(e -> openEditForm("", 0, 0, 0));
        myJpanel.add(addBlockerButton, BorderLayout.SOUTH);

        String[] columnNames = { "ID", "Name", "Random Seed", "Sprint Count", "Sprint Duration (Days)", "# of User Stories",
                "Actions" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6;
            }
        };

        simulationsTable = new JTable(tableModel);
        simulationsTable.getColumn("Actions").setCellRenderer(new SimulationHistoryPane.ButtonRenderer());
        simulationsTable.getColumn("Actions").setCellEditor(new SimulationHistoryPane.ButtonEditor(new JCheckBox()));
        refreshTableData();

        simulationsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1
                        && simulationsTable.getSelectedRow() != -1) {
                    int row = simulationsTable.getSelectedRow();
                    if (row != -1) {
                        Object id = simulationsTable.getValueAt(row, 0);
                        int encounterChance = (int) simulationsTable.getValueAt(row, 1);
                        int resolveChance = (int) simulationsTable.getValueAt(row, 2);
                        int spikeChance = (int) simulationsTable.getValueAt(row, 3);
                        openEditForm(id, encounterChance, resolveChance, spikeChance);
                    }
                }
            }
        });

        simulationsTable.setFillsViewportHeight(true);
        simulationsTable.setAutoCreateRowSorter(true);
        simulationsTable.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(simulationsTable);

        myJpanel.add(scrollPane, BorderLayout.CENTER);
        add(myJpanel);
    }

    private void openEditForm(Object id, int encounterChance, int resolveChance, int spikeChance) {
        EditBlockerProbabilities form = new EditBlockerProbabilities((String) id, encounterChance, resolveChance,
                spikeChance);
        this.setAlwaysOnTop(false);
        form.setAlwaysOnTop(true);
        setGlassPaneVisible(true);
        form.setVisible(true);
        form.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                refreshTableData();
                setGlassPaneVisible(false);
                setAlwaysOnTop(true);
            }
        });
    }

    private void refreshTableData() {
        tableModel.setRowCount(0);
        SimulationSingleton.getInstance().getAllSimulations().forEach(simulation -> {
            Object[] rowData = {
                    simulation.getSimulationId(),
                    simulation.getSimulationName(),
                    simulation.getRandomSeed(),
                    simulation.getSprintCount(),
                    simulation.getSprintDuration(),
                    simulation.getUserStories().size(),
                    "Actions"
            };
            tableModel.addRow(rowData);
        });
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

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
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

    private class ButtonEditor extends DefaultCellEditor {
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
                BlockerType blockerType = BlockerTypeStore.get()
                        .getBlockerType((String) tableModel.getValueAt(simulationsTable.getSelectedRow(), 0));
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem editItem = new JMenuItem("Edit");
                JMenuItem deleteItem = new JMenuItem("Delete");
                JMenuItem duplicateItem = new JMenuItem("Duplicate");

                editItem.addActionListener(e -> openEditForm(blockerType.getName(),
                        blockerType.getEncounterChance(), blockerType.getResolveChance(),
                        blockerType.getSpikeChance()));
                deleteItem.addActionListener(e -> {
                    BlockerTypeStore.get().removeBlocker(blockerType);
                    refreshTableData();
                });
                duplicateItem.addActionListener(e -> {
                    BlockerType duplicate = new BlockerType(blockerType.getName() + " - Copy",
                            blockerType.getEncounterChance(), blockerType.getResolveChance(),
                            blockerType.getSpikeChance());
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
