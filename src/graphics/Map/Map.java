package graphics.Map;

import graphics.MainPanel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

// Класс, реализующий модель карты Алтайского края
public class Map extends JTable {
    private final int MAP_SIZE = 65;
    private final int CELL_SIZE = 10;

    public Map() {
        view();
        cellsValue();
        listeners();
        setVisible(true);
    }

    // Внешний вид карты и ее свойства
    private void view() {
        // Создание таблицы 65х65 с нередактируемыми ячейками 10х10 пикселей
        setModel(new DefaultTableModel(this.MAP_SIZE, this.MAP_SIZE) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        setRowHeight(this.CELL_SIZE);
        for (int i = 0; i < this.MAP_SIZE; i++) {
            this.getColumnModel().getColumn(i).setMinWidth(this.CELL_SIZE);
            this.getColumnModel().getColumn(i).setMaxWidth(this.CELL_SIZE);
        }
        setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(0x333333),
                10), new TitledBorder(new LineBorder(new Color(0xFEFEFE)),
                "Алтайский край", TitledBorder.CENTER, TitledBorder.DEFAULT_POSITION,
                new Font("serif", Font.ROMAN_BASELINE, 14), Color.BLACK)));
        setDefaultRenderer(Object.class, new Renderer());

        // Установка кастомного шрифта для отображения сущностей в виде значков
        try {
            Font myFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\resources\\creatures.ttf")).deriveFont(7f);
            setFont(myFont);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Метод читает значения ячеек карты из файла и устанавливает их
    private void cellsValue() {
        long[][] data = new long[MAP_SIZE][MAP_SIZE];
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream("src\\resources\\mydata.dat");
            ois = new ObjectInputStream(fis);
            data = (long[][]) ois.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                setValueAt(data[y][x], y, x);
            }
        }
    }


    // x, y - координаты ячейки
    // u    - последовательность битов
    public int getLandscapeType(int y, int x) {
        return MapCoder.decodeLandscapeType(getDataAt(y, x));
    }
    public int getElkType(int y, int x) {
        return MapCoder.decodeElkType(getDataAt(y, x));
    }
    public int getElkAge(int y, int x) {
        return MapCoder.decodeElkAge(getDataAt(y, x));
    }
    public int getElkEnergy(int y, int x) {
        return MapCoder.decodeElkHunger(getDataAt(y, x));
    }
    public int getElkHunger(int y, int x) {
        return MapCoder.decodeElkHunger(getDataAt(y, x));
    }
    public int getElkPregnancy(int y, int x) {
        return MapCoder.decodeElkPregnancy(getDataAt(y, x));
    }
    public int getPlantType(int y, int x) {
        return MapCoder.decodePlantType(getDataAt(y, x));
    }
    public int getPlantFood(int y, int x) {
        return MapCoder.decodePlantFood(getDataAt(y, x));
    }
    public int getKillerType(int y, int x) {
        return MapCoder.decodeKillerType(getDataAt(y, x));
    }
    public int getActiveFlagElk(int y, int x) {
        return MapCoder.decodeActiveFlagElk(getDataAt(y, x));
    }
    public int getActiveFlagPlant(int y, int x) {
        return MapCoder.decodeActiveFlagPlant(getDataAt(y, x));
    }

    public void setLandscapeType(int u, int y, int x) {
        setDataAt(MapCoder.encodeLandscapeType(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 7)), y, x);
    }

    public void setElkType(int u, int y, int x) {
        setDataAt(MapCoder.encodeElkType(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 3)), y, x);
    }

    public void setElkAge(int u, int y, int x) {
        setDataAt(MapCoder.encodeElkAge(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 16383)), y, x);
    }

    public void setElkEnergy(int u, int y, int x) {
        setDataAt(MapCoder.encodeElkEnergy(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 63)), y, x);
    }

    public void setElkHunger(int u, int y, int x) {
        setDataAt(MapCoder.encodeElkHunger(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 63)), y, x);
    }

    public void setElkPregnancy(int u, int y, int x) {
        setDataAt(MapCoder.encodeElkPregnancy(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 255)), y, x);
    }

    public void setPlantType(int u, int y, int x) {
        setDataAt(MapCoder.encodePlantType(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 1)), y, x);
    }

    public void setPlantFood(int u, int y, int x) {
        setDataAt(MapCoder.encodePlantFood(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 63)), y, x);
    }

    public void setKillerType(int u, int y, int x) {
        setDataAt(MapCoder.encodeKillerType(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 3)), y, x);
    }

    public void setActiveFlagElk(int u, int y, int x) {
        setDataAt(MapCoder.encodeActiveFlagElk(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 1)), y, x);
    }

    public void setActiveFlagPlant(int u, int y, int x) {
        setDataAt(MapCoder.encodeActiveFlagPlant(getDataAt(y, x), logic.Protection.getValueInRange(u, 0, 1)), y, x);
    }

    public void setDataAt(long uc, int y, int x) {
        setValueAt(uc, y, x);
    }

    public long getDataAt(int y, int x) {
        return (long) getValueAt(y, x);
    }

    // Метод обечпечивает реакцию на нажатие на ячейку с координатами (x, y)
    private void listeners() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = rowAtPoint(e.getPoint());
                int y = columnAtPoint(e.getPoint());
                MainPanel.creatureInfo.update(x, y);
                MainPanel.landscapeInfo.update(x, y);
            }
        });
    }
}
