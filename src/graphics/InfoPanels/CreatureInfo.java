package graphics.InfoPanels;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import graphics.Map.MapCoder;
import graphics.MainPanel;

// Класс реализует панель с информацией о сущности, находящейся на данной
// территории (в определенной ячейке)
public class CreatureInfo extends JPanel {
    private JLabel text;
    private String info;

    public CreatureInfo() {
        view();
        text();
        setVisible(true);
    }

    // Внешний вид панели, расположение на ней компонентов
    private void view() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY),
                "Сведения о существе", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
                new Font("serif", Font.ROMAN_BASELINE, 14), Color.DARK_GRAY));
        setPreferredSize(new Dimension(450, 0));
    }

    // Отображение информации на панели
    private void text() {
        this.text = new JLabel("-");
        text.setFont(new Font("Arial Narrow", Font.ROMAN_BASELINE, 13));
        add(this.text);
    }

    // Обновление отображаемой информации (x, y - координаты ячейки)
    public void update(int y, int x) {
        long cellData = MainPanel.map.getDataAt(y, x);
        int isKiller = MapCoder.decodeKillerType(cellData);
        int elkType  = MapCoder.decodeElkType(cellData);

        if (isKiller == MapCoder.KILLER_TYPE_HUNTER){
            info = ("Тип существа: Охотник");
        }
        else if (isKiller == MapCoder.KILLER_TYPE_PREDATOR) {
            info = ("Тип существа: Хищник");
        }
        else if (isKiller == MapCoder.KILLER_TYPE_EMPTY && elkType != MapCoder.ELK_TYPE_EMPTY) {
            info = String.format("<html> Тип существа: Лось"
                            + "<br> Пол: %s"
                            + "<br> Возраст: %s"
                            + "<br> Энергия: %s"
                            + "<br> Голод: %s",
                    formatElkType(elkType),
                    formatDate(MapCoder.decodeElkAge(cellData)),
                    formatPercents(MapCoder.decodeElkEnergy(cellData)),
                    formatPercents(MapCoder.decodeElkHunger(cellData)));
            if (MapCoder.decodeElkPregnancy(cellData) > 0) {
                info += String.format("<br> Беременность: %s", formatDate(MapCoder.decodeElkPregnancy(cellData)));
            }
        } else if (isKiller == MapCoder.KILLER_TYPE_EMPTY && elkType == MapCoder.ELK_TYPE_EMPTY) {
            info = "На этой территории никого нет";
        }
        this.text.setText(info);
    }

    // Форматирование вывода прошедшего времени
    public static String formatDate(int days) {
        int months = days / 30;
        int years = months / 12;
        return String.format("%s лет %s месяцев %s дней", years, months % 12, days % 30);
    }

    // Форматирование вывода сведений о поле лося
    public static String formatElkType(int type) {
        String gender = "-";
        if (type == MapCoder.ELK_TYPE_MALE) { gender = "Мужской"; }
        else if (type == MapCoder.ELK_TYPE_FEMALE) { gender = "Женский"; }
        return gender;
    }

    // Форматирование вывода сведений в процентах
    public static String formatPercents(int value) {
        return String.format("%d %%", value*100/63);
    }

    public void reset() {
        this.text.setText("-");
    }
}
