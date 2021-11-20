/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package utility;

import javax.swing.*;

public class GUIUtility {
    public static boolean checkNotEmpty(JComponent... components) {
        for (JComponent comp : components) {
            String text = "";
            if (comp instanceof JTextField) {
                text = ((JTextField)comp).getText();
            } else if (comp instanceof JTextPane) {
                text = ((JTextPane)comp).getText();
            } else if (comp instanceof JTextArea) {
                text = ((JTextArea)comp).getText();
            } else if (comp instanceof JLabel) {
                text = ((JLabel)comp).getText();
            }
            if (text.equals("")) return false;
        }

        return true;
    }

    public static String toCamel(String word) {
        if (word.length() <= 1) {
            return word;
        }
        String lower = word.toLowerCase();
        return lower.substring(0,1).toUpperCase() + lower.substring(1);
    }
}
