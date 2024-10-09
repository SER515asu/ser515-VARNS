package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.DataModel;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.GridBagConstraintsBuilder;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.ResuableHeader;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.SpinnerInput;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.TextInput;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.Wizard;

import java.awt.*;
import javax.swing.*;

class GeneralPage extends Wizard.WizardPage {
        private final DataModel<String> simulationModel;
        private final DataModel<Object> sprintModel;
        private final DataModel<Object> sprintLengthModel;
        private SpinnerInput sprintInput;
        private SpinnerInput sprintLengthInput;

        public GeneralPage(DataModel<String> simulationModel, DataModel<Object> sprintModel,
                        DataModel<Object> sprintLengthModel) {
                this.simulationModel = simulationModel;
                this.sprintModel = sprintModel;
                this.sprintLengthModel = sprintLengthModel;
        }

        @Override
        protected String getId() {
                return "General";
        }

        @Override
        protected JPanel render() {
                JPanel container = new JPanel(new BorderLayout());
                ResuableHeader resuableHeader = new ResuableHeader("General", "General simulation settings");

                JPanel inputs = new JPanel(new GridBagLayout());
                TextInput simulationInput = new TextInput(
                                "Name: ", new JTextField(simulationModel.getData(), 5), simulationModel);
                sprintInput = new SpinnerInput(
                        "Sprints: ",
                        new JSpinner(new SpinnerNumberModel(1, 1, 20, 1)),
                        sprintModel);
                sprintLengthInput = new SpinnerInput(
                        "Sprint Length (days): ",
                        new JSpinner(new SpinnerNumberModel(14, 1, 30, 1)),
                        sprintLengthModel);


                inputs.add(
                                resuableHeader,
                                new GridBagConstraintsBuilder()
                                                .setGridX(0)
                                                .setGridY(0)
                                                .setWeightX(1)
                                                .setFill(GridBagConstraints.HORIZONTAL)
                                                .setInsets(new Insets(0, 0, 5, 0)));
                inputs.add(
                                simulationInput,
                                new GridBagConstraintsBuilder()
                                                .setGridX(0)
                                                .setGridY(1)
                                                .setWeightX(1)
                                                .setFill(GridBagConstraints.HORIZONTAL));
                inputs.add(
                                sprintInput,
                                new GridBagConstraintsBuilder()
                                                .setGridX(0)
                                                .setGridY(2)
                                                .setWeightX(1)
                                                .setFill(GridBagConstraints.HORIZONTAL));
                inputs.add(
                                sprintLengthInput,
                                new GridBagConstraintsBuilder()
                                                .setGridX(0)
                                                .setGridY(3)
                                                .setWeightX(1)
                                                .setFill(GridBagConstraints.HORIZONTAL));

                AutoFillToggleButton autoFillToggleButton = new AutoFillToggleButton(sprintModel, sprintLengthModel, this);
                JPanel togglePanel = autoFillToggleButton.getPanel();

                inputs.add(togglePanel, new GridBagConstraintsBuilder()
                                .setGridX(2)
                                .setGridY(4)
                                .setWeightX(1)
                                .setFill(GridBagConstraints.HORIZONTAL));

                container.add(inputs, BorderLayout.NORTH);
                return container;
        }
        public void updateUI() {
                if (sprintInput != null) {
                    sprintInput.updateFromModel();
                }
                if (sprintLengthInput != null) {
                    sprintLengthInput.updateFromModel();
                }
            }
}
