package graphics.Controller;

import logic.BasicLogic;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static graphics.MainPanel.parameters;

// Класс реализует панель управления запуском/приостановкой/остановкой работы модели
public class ControlPanel extends JPanel{
    private JButton onButton; // Кнопка вкл/выкл
    private JButton pauseButton; // Кнопка паузы/продолжения
    private boolean isSuspended = true, isOff = true; // Флаги приостановки и выключенности симуляции

    public ControlPanel() {
        view();
        buttons();
        setVisible(true);
    }

    // Внешний вид панели, расположение на ней компонентов
    private void view() {
        setLayout(new FlowLayout(FlowLayout.CENTER));
    }

    // Метод добавляет кастомные кнопки на панель и обеспечивает реагирование на нажатие
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
                    parameters.setEnabledSettings(true);
                    BasicLogic.stop();
                } else {
                    BasicLogic.start();
                    onButton.setIcon(new ImageIcon("src\\resources\\onButton.png"));
                    pauseButton.setEnabled(true);
                    parameters.setEnabledSettings(false);
                    BasicLogic.start();
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
                        BasicLogic.pause();
                    } else {
                        pauseButton.setIcon(new ImageIcon("src\\resources\\pauseButton.png"));
                        BasicLogic.start();
                }
                    isSuspended = !isSuspended;
            }
        });
        add(pauseButton);
    }
}
