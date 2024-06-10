package graphics.InfoPanels;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

import graphics.Map.MapCoder;
import graphics.MainPanel;

// Класс реализует панель с информацией о ландшафте данной территории (определенной ячейки)
public class LandscapeInfo extends JPanel {
    private JLabel text; // Строка с информацией

    public LandscapeInfo() {
        view();
        text();
        setVisible(true);
    }

    // Внешний вид панели, расположение на ней компонентов
    private void view() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBorder(new TitledBorder(new LineBorder(Color.DARK_GRAY),
                "Сведения о местности", TitledBorder.LEFT, TitledBorder.DEFAULT_POSITION,
                new Font("serif", Font.ROMAN_BASELINE, 14), Color.DARK_GRAY));
        setPreferredSize(new Dimension(0, 0));
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
        String info = String.format("<html> Тип ландшафта: %s"
                        + "<br> Наличие растительности: %s"
                        + "<br> Доля съедобных растений: %s",
                formatLandscapeType(MapCoder.decodeLandscapeType(cellData)),
                formatPlantType(MapCoder.decodePlantType(cellData)),
                CreatureInfo.formatPercents(MapCoder.decodePlantFood(cellData)));
        this.text.setText(info);
    }

    // Форматирование вывода сведений о наличии растений
    public static String formatPlantType(int type) {
        String plant = "-";
        if (type == MapCoder.PLANT_TYPE_PLANT) { plant = "+"; }
        else { plant = "-"; }
        return plant;
        }

    // Форматирование вывода сведений о ландшафте
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
