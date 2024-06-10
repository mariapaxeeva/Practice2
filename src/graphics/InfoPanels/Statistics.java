package graphics.InfoPanels;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import graphics.Map.MapCoder;

// Класс реализует панель с общей информацией о процессе моделирования
public class Statistics extends JPanel {
    private JLabel text; // Строка с информацией

    public Statistics() {
        view();
        text();
        setVisible(true);
    }

    // Внешний вид панели, расположение на ней компонентов
    private void view() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY),
                "Статистика", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
                new Font("serif", Font.ROMAN_BASELINE, 14), Color.DARK_GRAY));
        setPreferredSize(new Dimension(380, 260));
    }

    // Отображение информации на панели
    private void text() {
        this.text = new JLabel("-");
        text.setFont(new Font("Arial Narrow", Font.ROMAN_BASELINE, 13));
        add(this.text);
    }

    // Обновление отображаемой информации (date - количество прошедших дней)
    public void update(int date) {
        String info = String.format("<html>С начала симуляции прошло %s"
                    + "<br> Общая площадь территории: 167 996 км<sup>2</sup> (%s ячеек)"
                    + "<br> Популяция лосей: %s особей"
                    + "<br> &nbsp&nbsp&nbsp • Самцов: %s особей"
                    + "<br> &nbsp&nbsp&nbsp • Самок: %s особей"
                    + "<br> &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp • Беременных: %s особей"
                    + "<br> Родилось %s особей"
                    + "<br> Умерло %s особей:"
                    + "<br> &nbsp&nbsp&nbsp • От старости: %s"
                    + "<br> &nbsp&nbsp&nbsp • От усталости: %s"
                    + "<br> &nbsp&nbsp&nbsp • От голода: %s"
                    + "<br> &nbsp&nbsp&nbsp • От нападения хищника: %s"
                    + "<br> &nbsp&nbsp&nbsp • От встречи с охотником: %s"
                    + "<br> &nbsp&nbsp&nbsp • От эпидемии: %s"
                    + "<br> Численность врагов: %s"
                    + "<br> &nbsp&nbsp&nbsp • Охотников: %s"
                    + "<br> &nbsp&nbsp&nbsp • Хищников: %s"
                    + "<br> Средняя продолжительность жизни: %s"
                    + "<br> Эмигрировали %s особей"
                    + "<br> Популяция обитает на %d %% территории"
                    + "<br> Обеспеченность пищей: %d </html>", // количество пищи на особь (в среднем)
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    0,
                    0);
        this.text.setText(info);
    }

    public void reset() {
        this.text.setText("-");
    }
}
