package graphics.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;

public class CustomButtons extends JButton implements MouseListener {

    public CustomButtons(ImageIcon icon) {
        super(icon);
        setFocusPainted(false);
        int width  = icon.getIconWidth();
        int height = icon.getIconHeight();
        setPreferredSize(new Dimension(width,height));
        setContentAreaFilled(false);
        addMouseListener(this);
    }

    protected void paintComponent(Graphics g)
    {
        if (getModel().isArmed())
        {
            g.setColor(Color.lightGray);
        }
        else
        {
            g.setColor(getBackground());
        }
        g.fillOval(0, 0, getSize().width-1, getSize().height-1);
        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g)
    {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width-1, getSize().height-1);
    }

    Shape shape;
    public boolean contains(int x, int y)
    {
        if (shape == null ||!shape.getBounds().equals(getBounds()))
        {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        // Возвращает true, если точка принадлежит,
        // и false, если не принадлежит кнопке.
        return shape.contains(x, y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.setForeground(new Color(0x6E6E6E));
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.setForeground(new Color(0x333333));
    }
}
