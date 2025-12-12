/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class PasswordFieldWithEye extends JPanel {
    private JPasswordField passwordField;
    private JToggleButton eyeButton;
    
    public PasswordFieldWithEye(int columns) {
        setLayout(new BorderLayout());
        setBorder(new EmptyBorder(0, 0, 0, 0));
        
        passwordField = new JPasswordField(columns);
        eyeButton = new JToggleButton(new ImageIcon("eye.png"));
        eyeButton.setPreferredSize(new Dimension(30, passwordField.getPreferredSize().height));
        eyeButton.setBorder(new EmptyBorder(0, 0, 0, 0));
        eyeButton.setContentAreaFilled(false);
        
        add(passwordField, BorderLayout.CENTER);
        add(eyeButton, BorderLayout.EAST);
        
        eyeButton.addActionListener(e -> {
            if (eyeButton.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }
        });
    }
    
    public char[] getPassword() {
        return passwordField.getPassword();
    }
    
    public void setEnabled(boolean enabled) {
        passwordField.setEnabled(enabled);
        eyeButton.setEnabled(enabled);
    }
}