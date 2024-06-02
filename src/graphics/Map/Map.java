package graphics.Map;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class Map extends JTable {
    private final int MAP_SIZE = 65;
    private final int CELL_SIZE = 10;

    public Map() {
        view();
        cellsValue();
        setVisible(true);
    }

    private void view() {
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
        setBorder(BorderFactory.createLineBorder(new Color (0x333333)));
        setDefaultRenderer(Object.class, new Renderer());
        try {
            Font myFont = Font.createFont(Font.TRUETYPE_FONT, new File("src\\resources\\creatures.ttf")).deriveFont(7f);
            setFont(myFont);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cellsValue() {
//        for (int x = 0; x < this.MAP_SIZE; x++) {
//            for (int y = 0; y < this.MAP_SIZE; y++) {
//                setValueAt(0x0000L, y, x);
//            }
//        }
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
}
