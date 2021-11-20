/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package server;

import GUIComponents.RequestCellRenderer;
import GUIComponents.RoundButton;
import GUIComponents.RoundList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ServerGUI {
    private JList addressList;
    private JButton startButton;
    private JButton stopButton;
    private JPanel panel1;
    private JPanel panel2;
    private JScrollPane Scroll;

    private DictServer server;
    private JFrame serverInterface;

    public ServerGUI(DictServer server) {
        this.server = server;
        initGUI();
    }

    public void initGUI() {
        this.serverInterface = new JFrame();
        serverInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverInterface.setContentPane(panel1);
        switchButton(false);
        Scroll.setBorder(new EmptyBorder(new Insets(0,0,0,0)));
        addressList.setCellRenderer(new RequestCellRenderer());


        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.setStartFlag(1);
                switchButton(false);
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                server.setStartFlag(0);
                switchButton(true);
            }
        });
    }

    public void updateList(Request[] list) {
        addressList.setListData(list);
        addressList.repaint();
    }

    public void switchButton(boolean stop) {
        stopButton.setEnabled(!stop);
        startButton.setEnabled(stop);
        if (stop) {
            startButton.setForeground(new Color(0xD7FCDF));
            startButton.setBackground(new Color(0x62E537));
            stopButton.setBackground(new Color(0x3D2222));
            stopButton.setForeground(new Color(0xF3D9D1));
        } else {
            startButton.setForeground(new Color(0xD7FCDF));
            startButton.setBackground(new Color(0x134505));
            stopButton.setBackground(new Color(0xE83E3E));
            stopButton.setForeground(new Color(0xF3D9D1));
        }
    }

    public void run() {
        EventQueue.invokeLater(() -> {
            try {
                serverInterface.pack();
                serverInterface.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
        addressList = new RoundList(30, 30);
        startButton = new RoundButton("Start",10, 10, new Color(0x94B7F5), new Color(0x62E537));
        stopButton = new RoundButton("Stop",10, 10, new Color(0x94B7F5), new Color(0xE83E3E));
    }
}
