/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package client;

import GUIComponents.*;
import utility.GUIUtility;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.*;
import java.awt.*;

import static constants.Commands.*;
import static constants.StateCodes.*;

public class ClientGUI {
    private JPanel panel1;
    private JTextField searchField;
    private JButton searchButton;
    private JTextPane description;
    private JButton modifyButton;
    private JButton removeButton;
    private JList searchList;
    private JTextField addField;
    private JTextArea addDescArea;
    private JButton addButton;
    private JLabel wordLabel;
    private JPanel panel2;
    private JPanel panel3;
    private JScrollPane scrollPane;

    private JFrame clientInterface;

    private final DictClient client;

    public ClientGUI(DictClient client){
        this.client = client;
        initGUI();
    }

    public void run() {
        EventQueue.invokeLater(() -> {
            try {
                clientInterface.pack();
                clientInterface.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public boolean handleError(Result result) {
        int response = result.getResponse();
        if (response == SUCC)
            return false;

        switch (response) {
            case WORD_NOT_FOUND -> JOptionPane.showMessageDialog(clientInterface,
                    "Cannot find the word", "Word Not Found", JOptionPane.ERROR_MESSAGE);
            case WORD_ALREADY_EXIST -> JOptionPane.showMessageDialog(clientInterface,
                    "Word already exist, would you like to update the word?", "Word Already Exist",
                    JOptionPane.ERROR_MESSAGE);
            case SERVER_CONNECTION_FAIL -> JOptionPane.showMessageDialog(clientInterface,
                    "Cannot connect to the server", "Connection Error", JOptionPane.ERROR_MESSAGE);
            case PARSE_ERROR -> JOptionPane.showMessageDialog(clientInterface,
                    "Cannot parse the response from server.", "Parse Error", JOptionPane.ERROR_MESSAGE);
            case IO_ERROR -> JOptionPane.showMessageDialog(clientInterface,
                    "Internal Server Error, please try again", "Internal Error", JOptionPane.ERROR_MESSAGE);
        }
        return true;
    }

    public void initGUI() {
        this.clientInterface = new JFrame();
        clientInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientInterface.setContentPane(panel1);
        scrollPane.setBorder(new EmptyBorder(new Insets(0,0,0,0)));

        searchButton.addActionListener(e -> {
            if (!GUIUtility.checkNotEmpty(searchField)) {
                JOptionPane.showMessageDialog(null,"Please enter a word",
                        "Empty", JOptionPane.WARNING_MESSAGE);
            }
            else {
                Result result = this.client.execute(searchField.getText(), "", QUERY);

                if (!handleError(result)) {
                    wordLabel.setText(GUIUtility.toCamel(searchField.getText()));
                    description.setText(result.getDescription());
                }
            }
        });

        modifyButton.addActionListener(e -> {
            if (!GUIUtility.checkNotEmpty(wordLabel)) {
                JOptionPane.showMessageDialog(null,"Please select a word to modify",
                        "Empty", JOptionPane.WARNING_MESSAGE);
            }
            else {
                ModifyInput mi = new ModifyInput(clientInterface, description.getText(), wordLabel.getText());
                mi.pack();
                mi.setVisible(true);
                if (mi.ifChecked()) {
                    String desc = mi.getInput();
                    Result result = this.client.execute(wordLabel.getText(), desc, UPDATE);

                    if (!handleError(result)) {
                        wordLabel.setText(GUIUtility.toCamel(wordLabel.getText()));
                        description.setText(result.getDescription());
                        JOptionPane.showMessageDialog(null, "Successfully modified.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        removeButton.addActionListener(e -> {
            if (!GUIUtility.checkNotEmpty(wordLabel)) {
                JOptionPane.showMessageDialog(null,"Please select a word to remove",
                        "Empty", JOptionPane.WARNING_MESSAGE);
            }
            else {
                Result result = this.client.execute(wordLabel.getText(), "", DELETE);
                if (!handleError(result)) {
                    wordLabel.setText("");
                    description.setText("");
                    JOptionPane.showMessageDialog(null, "Successfully removed.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    Result updateList = client.execute(searchField.getText(), "", CONTAIN);
                    if (result.getResponse() == SUCC) {
                        String[] containList = updateList.getContainList().toArray(String[]::new);
                        searchList.setListData(containList);
                        searchList.updateUI();
                    }
                    wordLabel.setText("");
                    description.setText("");
                }
            }
        });

        addButton.addActionListener(e -> {
            if (!GUIUtility.checkNotEmpty(addField, addDescArea)) {
                JOptionPane.showMessageDialog(null,"Please enter both word and description",
                        "Empty", JOptionPane.WARNING_MESSAGE);
            }
            else {
                Result result = this.client.execute(addField.getText(), addDescArea.getText(), ADD);
                if (!handleError(result)) {
                    wordLabel.setText(GUIUtility.toCamel(addField.getText()));
                    description.setText(addDescArea.getText());
                    JOptionPane.showMessageDialog(null, "Successfully added.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    Result updateList = client.execute(searchField.getText(), "", CONTAIN);
                    if (result.getResponse() == SUCC) {
                        String[] containList = updateList.getContainList().toArray(String[]::new);
                        searchList.setListData(containList);
                        searchList.updateUI();
                    }
                    addField.setText("");
                    addDescArea.setText("");
                }
            }
        });

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Result result = client.execute(searchField.getText(), "", CONTAIN);
                if (result.getResponse() == SUCC) {
                    String[] containList = result.getContainList().toArray(String[]::new);
                    searchList.setListData(containList);
                    searchList.updateUI();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Result result = client.execute(searchField.getText(), "", CONTAIN);
                if (result.getResponse() == SUCC) {
                    String[] containList = result.getContainList().toArray(String[]::new);
                    searchList.setListData(containList);
                    searchList.updateUI();
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        searchList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    JList source = (JList)e.getSource();
                    if (!source.isSelectionEmpty()) {
                        String selectedWord = source.getSelectedValue().toString();
                        Result result = client.execute(selectedWord, "", QUERY);

                        if (!handleError(result)) {
                            wordLabel.setText(GUIUtility.toCamel(selectedWord));
                            description.setText(result.getDescription());
                        }
                    }
                }
            }
        });
    }

    private void createUIComponents() {
        searchField = new LineTextField(1, 30, 30);
        addField = new LineTextField(1,30,30);
        addDescArea = new RoundTextArea(1,1,30,30);
        description = new RoundTextPane(30,30);
        panel2 = new RoundPanel(15,15);
        panel3 = new RoundPanel(15,15);
        searchButton = new RoundButton("Search", 16, 16);
        addButton = new RoundButton("Add", 12, 12);
        modifyButton = new RoundButton("Modify", 12, 12);
        removeButton = new RoundButton("Remove", 12, 12);
        searchList = new RoundList(30, 30);
    }
}
