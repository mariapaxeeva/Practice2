package graphics.Map;

import java.awt.Color;
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
    }

    private void cellsValue() {
        for (int x = 0; x < this.MAP_SIZE; x++) {
            for (int y = 0; y < this.MAP_SIZE; y++) {
                setValueAt(0x0000, y, x);
            }
        }
    }
}
