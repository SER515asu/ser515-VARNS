package com.groupesan.project.java.scrumsimulator.mainpackage.ui.dialogs.simulation;

import com.groupesan.project.java.scrumsimulator.mainpackage.core.*;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.DataModel;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.Wizard;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets.WizardHandler;
import java.util.ArrayList;
import java.util.List;

public class SimulationWizard extends Wizard<Simulation> {
    private DataModel<String> simulationName;
    private DataModel<Object> sprintCount;
    private DataModel<Object> sprintLength;
    private DataModel<List<ScrumRole>> roles;
    private DataModel<List<Player>> users;

    public SimulationWizard(WizardHandler<Simulation> handler) {
        super(handler);
        setTitle("New Simulation");
    }

    @Override
    protected void initDataModels() {
        this.simulationName = new DataModel<>("New Simulation");
        this.sprintCount = new DataModel<>(1);
        this.sprintLength = new DataModel<>(14);
        this.roles = new DataModel<>(new ArrayList<>());
        this.users = new DataModel<>(new ArrayList<>());
    }

    protected List<WizardPage> build() {
        this.roles.getData().add(new ScrumRole("Developer"));
        this.roles.getData().add(new ScrumRole("Scrum Master"));
        this.roles.getData().add(new ScrumRole("Product Owner"));
        return List.of(
                new GeneralPage(simulationName, sprintCount, sprintLength),
                new ParticipantsPage(users, roles));
    }

    @Override
    protected Simulation process() {
        Simulation simulation =
                new Simulation(simulationName.getData(), (Integer) sprintCount.getData(), (Integer) sprintLength.getData());
        for (Player player : users.getData()) {
            player.doRegister();
            simulation.addPlayer(player);
        }
        return simulation;
    }
}
