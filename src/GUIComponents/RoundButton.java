/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package GUIComponents;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundButton extends JButton {
    private Shape shape;
    private final int arcWidth;
    private final int arcHeight;

    public RoundButton(String info, int arcWidth, int arcHeight) {
        super(info);
        this.arcHeight = arcHeight;
        this.arcWidth = arcWidth;
        this.setModel(new CustomBM());
        this.getModel().addChangeListener(e -> {
            if (getModel().isRollover()) {
                setBackground(new Color(0x94B7F5));
            } else {
                setBackground(new Color(0x495464));
            }
        });
        setOpaque(false);
        setFocusPainted(false);
    }

    public RoundButton(String info, int arcWidth, int arcHeight, Color hover, Color background) {
        super(info);
        this.arcHeight = arcHeight;
        this.arcWidth = arcWidth;
        this.setBackground(background);
        this.setModel(new CustomBM());
        this.getModel().addChangeListener(e -> {
            if (getModel().isRollover()) {
                setBackground(hover);
            } else {
                setBackground(background);
            }
        });
        setOpaque(false);
        setFocusPainted(false);
    }


    protected void paintComponent(Graphics g) {
        g.setColor(getBackground());
        g.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {

    }

    public boolean contains(int x, int y) {
        if (shape == null || !shape.getBounds().equals(getBounds())) {
            shape = new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
        }
        return shape.contains(x, y);
    }
    public class CustomBM extends DefaultButtonModel    {
        @Override
        public boolean isPressed() {
            return false;
        }
    }

}
