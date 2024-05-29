import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Map extends JTable {
    private final int mapSize = 129;
    private final int cellSize = 5;

    public Map() {
        setModel(new DefaultTableModel(this.mapSize, this.mapSize));
        setRowHeight(this.cellSize);
        for (int i = 0; i < this.mapSize; i++) {
            this.getColumnModel().getColumn(i).setMinWidth(this.cellSize);
            this.getColumnModel().getColumn(i).setMaxWidth(this.cellSize);
        }
        for (int x = 0; x < this.mapSize; x++) {
            for (int y = 0; y < this.mapSize; y++) {
                setValueAt(0x0000, y, x);
            }
        }
        setVisible(true);
    }
}
