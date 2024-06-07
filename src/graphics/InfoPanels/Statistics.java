package graphics.InfoPanels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import graphics.Map.MapCoder;

public class Statistics extends JPanel {
    private JLabel text;
    private String info;

    public Statistics() {
        view();
        text();
        setVisible(true);
    }

    private void view() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder("Статистика"));
        setPreferredSize(new Dimension(450, 250));
    }

    private void text() {
        this.text = new JLabel("-");
        add(this.text);
    }

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

    public static String formatPlantType(int type) {
        String plant = "-";
        if (type == MapCoder.PLANT_TYPE_PLANT) { plant = "+"; }
        else { plant = "-"; }
        return plant;
    }

    public static String formatLandscapeType(int type) {
        String landscape = "-";
        switch (type) {
            case MapCoder.LANDSCAPE_TYPE_PLAIN: {
                landscape = "Равнина";
                break;
            }
            case MapCoder.LANDSCAPE_TYPE_HILL: {
                landscape = "Холм";
                break;
            }
            case MapCoder.LANDSCAPE_TYPE_MOUNTAIN: {
                landscape = "Горы";
                break;
            }
            case MapCoder.LANDSCAPE_TYPE_WATER: {
                landscape = "Вода";
                break;
            }
            case MapCoder.LANDSCAPE_TYPE_OUTSIDE: {
                landscape = "Прилежащие территории";
                break;
            }
            default: {
                landscape = "Не определён";
                break;
            }
        }
        return landscape;
    }

    public void reset() {
        this.text.setText("-");
    }
}
