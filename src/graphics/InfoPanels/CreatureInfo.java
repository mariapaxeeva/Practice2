package graphics.InfoPanels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import graphics.Map.MapCoder;
import graphics.MainPanel;

public class CreatureInfo extends JPanel {
    private JLabel info;

    public CreatureInfo() {
        view();
        info();
        setVisible(true);
    }

    private void view() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder("Сведения о существе"));
        setPreferredSize(new Dimension(450, 0));
    }

    private void info() {
        this.info = new JLabel("-");
        add(this.info);
    }

    public void update(int y, int x) {
        long cellData = MainPanel.map.getDataAt(y, x);
        String info = String.format("<html> Пол лося: %s"
                        + "<br> Возраст: %s"
                        + "<br> Энергия: %s"
                        + "<br> Голод: %s"
                        + "<br> Беременность: %s"
                        + "<br> ",
                MapCoder.decodeElkType(cellData),
                MapCoder.decodeElkAge(cellData),
                MapCoder.decodeElkEnergy(cellData),
                MapCoder.decodeElkHunger(cellData),
                MapCoder.decodeElkPregnancy(cellData));
        this.info.setText(info);
    }
}
