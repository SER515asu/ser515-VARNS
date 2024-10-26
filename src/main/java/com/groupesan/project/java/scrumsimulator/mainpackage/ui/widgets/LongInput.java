package com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets;

import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.DataModel;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.SimpleDocumentListener;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

/** Reusable JLongField component. */
public class LongInput extends ScrumLabeledComponent<JTextField, DataModel<Long>> {
    public LongInput(String labelLong, JTextField component, DataModel<Long> model) {
        super(labelLong, component, model);
    }

    public void init() {
        super.init();
        component
                .getDocument()
                .addDocumentListener(
                        new SimpleDocumentListener() {
                            public void changed(DocumentEvent event) {
                                model.setData(Long.parseLong(component.getText()));
                            }
                        });
    }
}
