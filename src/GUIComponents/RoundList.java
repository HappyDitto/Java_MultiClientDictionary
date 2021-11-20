/*
 * Student Name: Qixuan Xin
 * Student ID: 1202931
 */

package GUIComponents;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundList extends JList {
    private Shape shape;
    private final int arcWidth;
    private final int arcHeight;

    public RoundList(int arcWidth, int arcHeight) {
        super();
        this.arcHeight = arcHeight;
        this.arcWidth = arcWidth;
        setOpaque(false);
        setBorder(new EmptyBorder(10,10,10,10));

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
}
