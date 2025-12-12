package utils;

import javax.swing.text.*;

/**
 * Limita la cantidad de caracteres permitidos y sólo permite números (para DNI/RUC).
 * Puede usarse tanto en JTextPane como JTextField.
 */
public class JTextFieldLimit extends DocumentFilter {
    private int limit;

    public JTextFieldLimit(int limit) {
        this.limit = limit;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        if (string == null) return;
        if (isAllowed(fb, string)) {
            super.insertString(fb, offset, string, attr);
        }
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        if (text == null) return;
        if (isAllowed(fb, text)) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    private boolean isAllowed(FilterBypass fb, String text) {
        try {
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            StringBuilder sb = new StringBuilder(currentText);
            sb.insert(fb.getDocument().getLength(), text);
            // Solo números y máximo 'limit' caracteres
            return sb.length() <= limit && text.matches("\\d+");
        } catch (BadLocationException e) {
            return false;
        }
    }
}