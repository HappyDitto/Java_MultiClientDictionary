/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package client;

import GUIComponents.RoundButton;
import GUIComponents.RoundTextArea;
import utility.GUIUtility;

import javax.swing.*;
import java.awt.event.*;

public class ModifyInput extends JDialog {
    public static int OK = 1;
    public static int CANCEL = 0;

    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextArea textArea1;

    private int checked;

    public ModifyInput(JFrame parent, String defaultContent, String word) {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);
        setLocationRelativeTo(parent);
        setTitle("Modify: " + word);
        textArea1.setText(defaultContent);
        this.checked = CANCEL;


        buttonOK.addActionListener(e -> onOK());

        buttonCancel.addActionListener(e -> onCancel());

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public String getInput() {
        return textArea1.getText();
    }

    public boolean ifChecked () {
        return checked == OK;
    }

    private void onOK() {
        // add your code here
        if (!GUIUtility.checkNotEmpty(textArea1)) {
            JOptionPane.showMessageDialog(this,"Please enter description", "Empty", JOptionPane.WARNING_MESSAGE);
            return;
        }
        checked = OK;
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }


    private void createUIComponents() {
        // TODO: place custom component creation code here
        textArea1 = new RoundTextArea(1,1,16, 16);
        buttonCancel = new RoundButton("Cancel", 10, 10);
        buttonOK = new RoundButton("OK", 10, 10);
    }
}
