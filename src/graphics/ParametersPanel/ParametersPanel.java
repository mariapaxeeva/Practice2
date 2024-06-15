package graphics.ParametersPanel;

import graphics.MainPanel;
import logic.BasicLogic;
import logic.DisasterLogic;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Реализация панели, посредством которой осуществляется взаимодействие
// с пользователем. Содержит панель с бегунками для установки начальных
// значений и панель с кнопками, которые провоцируют бедствия.
public class ParametersPanel extends JPanel {
    private static JPanel settings = new JPanel();
    private static JPanel disasterButtons = new JPanel();

    private JSlider elksSlider = CustomSliders.newCustomSlider(0, 200, 200); // Количество лосей
    private JSlider huntersSlider = CustomSliders.newCustomSlider(0, 100, 30); // Количество охотников
    private JSlider limitOfHuntingElkSlider = CustomSliders.newCustomSlider(0, 100, 2); // Лимит добычи лосей
    private JSlider limitOfHuntingPredatorSlider = CustomSliders.newCustomSlider(0, 100, 10); // Лимит добычи хищников
    private JSlider predatorsSlider = CustomSliders.newCustomSlider(0, 100, 57); // Количество хищников
    private DisasterLogic disaster = new DisasterLogic();

    public ParametersPanel() {
        view();
        panels();
        settings();
        disasterButtons();
        setVisible(true);
    }

    // Внешний вид панели, расположение на ней компонентов
    private void view() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY),
                "Параметры модели", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
                new Font("serif", Font.ROMAN_BASELINE, 20), Color.DARK_GRAY));
        setPreferredSize(new Dimension(280, 0));
    }

    // Вид вложенных панелей, расположение на них компонентов
    private void panels() {
        settings.setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY),
                "Настройки", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
                new Font("serif", Font.ROMAN_BASELINE, 14), Color.DARK_GRAY));
        settings.setLayout(new GridLayout(10, 1, 0, 10));
        this.add(settings);

        disasterButtons.setBorder(new CompoundBorder(new TitledBorder (new LineBorder(Color.DARK_GRAY),
                "Действия", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
                new Font("serif", Font.ROMAN_BASELINE, 14), Color.DARK_GRAY), new EmptyBorder(20, 35, 30, 35)));
        disasterButtons.setLayout(new GridLayout(2, 1, 0, 30));
        this.add(disasterButtons);
    }

    // Метод реализует панель настроек с ползунками
    public void settings() {
        setSlider(elksSlider, "Количество лосей: ");
        setSlider(huntersSlider, "Количество охотников: ");
        setSlider(limitOfHuntingElkSlider, "Лимит добычи лосей (в %): ");
        setSlider(limitOfHuntingPredatorSlider, "Лимит добычи хищников (в %): ");
        setSlider(predatorsSlider, "Количество хищников: ");
    }

    // Метод реализует панель с кнопками бедствий
    public void disasterButtons() {
        ImageIcon fire = new ImageIcon("src/resources/fire.jpg");
        ImageIcon deforestation = new ImageIcon("src/resources/deforestation.jpg");

        JButton makeFireButton = new JButton("Устроить пожар", fire);
        JButton deforestationButton = new JButton("Вырубить лес", deforestation);

        buttonStyle(makeFireButton);
        buttonStyle(deforestationButton);

        disasterButtons.add(makeFireButton);
        disasterButtons.add(deforestationButton);

        makeFireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disaster.InvertIsFire();
            }
        });
        deforestationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                disaster.makeDeforestation();
            }
        });
    }

    public void reset() {
        elksSlider.setValue(200);
        huntersSlider.setValue(30);
        limitOfHuntingElkSlider.setValue(2);
        limitOfHuntingPredatorSlider.setValue(10);
        predatorsSlider.setValue(57);
    }

    // Метод устанавливает ползунок с подписью о том, что он регулирует
    // и текущим значением на панель настроек.
    private void setSlider(JSlider slider, String text) {
        JLabel label = new JLabel();
        label.setFont(new Font("serif", Font.ROMAN_BASELINE, 14));
        label.setBorder(BorderFactory.createEmptyBorder(0, 13, 0, 0));
        label.setText(text + slider.getValue());
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                int value = ((JSlider)e.getSource()).getValue();
                label.setText(text + value + "%");
            }
        });
        settings.add(label);
        settings.add(slider);
    }

    // Метод определяет внешний вид кнопок с изображением
    public void buttonStyle(JButton button) {
        Font font = new Font("Serif", Font.BOLD, 30);
        button.setPreferredSize(new Dimension(120,20));
        button.setForeground(new Color(0xE3E3E3));
        button.setBackground(Color.LIGHT_GRAY);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
    }

    // Метод изменения доступа к панели и ее содержимому
    public void setEnabledSettings(boolean b) {
        settings.setEnabled(b);
        elksSlider.setEnabled(b);
        huntersSlider.setEnabled(b);
        limitOfHuntingElkSlider.setEnabled(b);
        limitOfHuntingPredatorSlider.setEnabled(b);
        predatorsSlider.setEnabled(b);
    }

    public int getValueElksSlider() {
        return elksSlider.getValue();
    }

    public int getValueHuntersSlider() {
        return huntersSlider.getValue();
    }

    public int getValuePredatorsSlider() {
        return predatorsSlider.getValue();
    }

    public int getValueLimitOfElkSlider() {
        return limitOfHuntingElkSlider.getValue();
    }

    public int getValueLimitOfPredatorsSlider() {
        return limitOfHuntingPredatorSlider.getValue();
    }
}