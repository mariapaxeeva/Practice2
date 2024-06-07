package graphics.InfoPanels;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import graphics.Map.MapCoder;
import graphics.MainPanel;

public class LandscapeInfo extends JPanel {
    private JLabel text;
    private String info;

    public LandscapeInfo() {
        view();
        text();
        setVisible(true);
    }

    private void view() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder("Сведения о местности"));
        setPreferredSize(new Dimension(450, 0));
    }

    private void text() {
        this.text = new JLabel("-");
        add(this.text);
    }

    public void update(int y, int x) {
        long cellData = MainPanel.map.getDataAt(y, x);
        String info = String.format("<html> Тип ландшафта: %s"
                        + "<br> Наличие растительности: %s"
                        + "<br> Доля съедобных растений: %s",
                formatLandscapeType(MapCoder.decodeLandscapeType(cellData)),
                formatPlantType(MapCoder.decodePlantType(cellData)),
                CreatureInfo.formatPercents(MapCoder.decodePlantFood(cellData)));
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
