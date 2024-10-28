package com.groupesan.project.java.scrumsimulator.mainpackage.ui.widgets;

import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.DataModel;
import com.groupesan.project.java.scrumsimulator.mainpackage.ui.utils.SimpleDocumentListener;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

/** Reusable JLongField component. */
public class LongInput extends ScrumLabeledComponent<JTextField, DataModel<Long>> {
    public LongInput(String labelLong, JTextField component, DataModel<Long> model) {
        super(labelLong, component, model);
    }

    public void init() {
        super.init();
        ((AbstractDocument) component.getDocument()).setDocumentFilter(new LongDocumentFilter());
        component
                .getDocument()
                .addDocumentListener(
                        new SimpleDocumentListener() {
                            public void changed(DocumentEvent event) {
                                try {
                                    model.setData(Long.parseLong(component.getText()));
                                } catch (NumberFormatException e) {
                                    model.setData(null);
                                }
                            }
                        });
    }

    private static class LongDocumentFilter extends DocumentFilter {
        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
            if (isNumeric(fb.getDocument().getText(0, fb.getDocument().getLength()), string, offset)) {
                super.insertString(fb, offset, string, attr);
            }
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            if (isNumeric(fb.getDocument().getText(0, fb.getDocument().getLength()), text, offset)) {
                super.replace(fb, offset, length, text, attrs);
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            super.remove(fb, offset, length);
        }

        private boolean isNumeric(String currentText, String newText, int offset) {
            String resultingText = new StringBuilder(currentText).insert(offset, newText).toString();
            if (resultingText.isEmpty()) {
                return true;
            }
            if (resultingText.equals("-")) {
                return true;
            }
            if (resultingText.startsWith("-")) {
                resultingText = resultingText.substring(1);
            }
            for (char c : resultingText.toCharArray()) {
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
        }
    }
}
