package graphics.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ControlPanel extends JPanel{
    private JButton onButton;
    private JButton pauseButton;
    private boolean isSuspended = true, isOff = true;

    public ControlPanel() {
        view();
        buttons();
        setVisible(true);
    }

    private void view() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    private void buttons() {
        // Кнопка начала/остановки симуляции
        this.onButton = new CustomButtons(new ImageIcon("src\\resources\\offButton.png"));
        onButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isOff) {
                    onButton.setIcon(new ImageIcon("src\\resources\\offButton.png"));
                    pauseButton.setIcon(new ImageIcon("src\\resources\\pauseButton.png"));
                    pauseButton.setEnabled(false);
                } else {
                    onButton.setIcon(new ImageIcon("src\\resources\\onButton.png"));
                    pauseButton.setEnabled(true);
                }
                isOff = !isOff;
            }
        });
        add(onButton);

        // Кнопка паузы/продолжения симуляции
        this.pauseButton = new CustomButtons(new ImageIcon("src\\resources\\pauseButton.png"));
        pauseButton.setEnabled(false);
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isSuspended) {
                        pauseButton.setIcon(new ImageIcon("src\\resources\\continueButton.png"));
                    } else {
                        pauseButton.setIcon(new ImageIcon("src\\resources\\pauseButton.png"));
                }
                    isSuspended = !isSuspended;
            }
        });
        add(pauseButton);
    }
}
