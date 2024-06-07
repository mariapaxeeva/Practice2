package graphics.InfoPanels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import graphics.Map.MapCoder;
import graphics.MainPanel;

public class CreatureInfo extends JPanel {
    private JLabel text;
    private String info;

    public CreatureInfo() {
        view();
        text();
        setVisible(true);
    }

    private void view() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder("Сведения о существе"));
        setPreferredSize(new Dimension(450, 0));
    }

    private void text() {
        this.text = new JLabel("-");
        add(this.text);
    }

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

    public static String formatDate(int days) {
        int months = days / 30;
        int years = months / 12;
        return String.format("%s лет %s месяцев %s дней", years, months % 12, days % 30);
    }

    public static String formatDateShort(int days) {
        int months = days / 30;
        int years = months / 12;
        return String.format("%04d.%02d.%02d", years, months % 12, days % 30);
    }

    public static String formatElkType(int type) {
        String gender = "-";
        if (type == MapCoder.ELK_TYPE_MALE) { gender = "Мужской"; }
        else if (type == MapCoder.ELK_TYPE_FEMALE) { gender = "Женский"; }
        return gender;
    }

    public static String formatPercents(int value) {
        return String.format("%d %%", value*100/63);
    }

    public void reset() {
        this.text.setText("-");
    }
}
