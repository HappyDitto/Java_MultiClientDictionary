/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package GUIComponents;

import server.Request;

import javax.swing.*;
import java.awt.*;

import static constants.StateCodes.SUCC;

public class RequestCellRenderer extends JPanel implements ListCellRenderer<Request> {

    private JLabel address;
    private JLabel command;
    private JLabel word;
    private JPanel main;
    private JPanel resultPanel;
    private JLabel result;

    public RequestCellRenderer() {
        setLayout(new BorderLayout(20, 20));
        add(main, BorderLayout.CENTER);
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Request> list, Request value, int index, boolean isSelected, boolean cellHasFocus) {
        result.setForeground(Color.WHITE);
        resultPanel.setOpaque(true);
        if (value.getResult() == SUCC) {
            result.setText("Succ");
            resultPanel.setBackground(new Color(0x0AF649));
        } else {
            result.setText("Fail");
            resultPanel.setBackground(new Color(0xC91703));
        }

        address.setText(value.getAddress());
        command.setText(value.getCommand());
        word.setText("Word: " + value.getWord());


        return this;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
