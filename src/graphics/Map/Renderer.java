package graphics.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class Renderer extends DefaultTableCellRenderer {

    private final static int COLOR_EMPTY                            = 0xF0F0F0;
    private final static int COLOR_UNDEFINED                        = 0xC4240C;
    private final static int LANDSCAPE_TYPE_COLOR_WATER             = 0x60A4B1;
    private final static int LANDSCAPE_TYPE_COLOR_PLAIN             = 0xA5E087;
    private final static int LANDSCAPE_TYPE_COLOR_HILL              = 0xF2E085;
    private final static int LANDSCAPE_TYPE_COLOR_MOUNTAIN          = 0xBA8416;
    private final static int LANDSCAPE_TYPE_COLOR_GRASS_PLAIN       = 0x5ABD20;
    private final static int LANDSCAPE_TYPE_COLOR_OUTSIDE           = 0x8A8A8A;
    private final static int ELK_TYPE_COLOR_MALE                    = 0x010E82;
    private final static int ELK_TYPE_COLOR_FEMALE                  = 0xC75DC2;
    private final static int KILLER_TYPE_COLOR_HUNTER               = 0x233D20;
    private final static int KILLER_TYPE_COLOR_PREDATOR             = 0xA10A2A;
    private final static int PLANT_TYPE_COLOR_FOOD                  = 0x0E5200;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, "", isSelected, hasFocus, row, column);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBackground(new Color(getBackgroundColor((long) value)));
        setForeground(new Color(getForegroundColor((long) value)));
        setValue(getCell((long) value));
        return this;
    }

    private static int getBackgroundColor(long cellData) {
        int color = COLOR_EMPTY;
        int landscapeType = MapCoder.decodeLandscapeType(cellData);
        switch (landscapeType) {
            case MapCoder.LANDSCAPE_TYPE_WATER: {
                color = LANDSCAPE_TYPE_COLOR_WATER;
                break;
            }
            case MapCoder.LANDSCAPE_TYPE_PLAIN: {
                color = LANDSCAPE_TYPE_COLOR_PLAIN;
                break;
            }
            case MapCoder.LANDSCAPE_TYPE_HILL: {
                color = LANDSCAPE_TYPE_COLOR_HILL;
                break;
            }
            case MapCoder.LANDSCAPE_TYPE_MOUNTAIN: {
                color = LANDSCAPE_TYPE_COLOR_MOUNTAIN;
                break;
            }
            case MapCoder.LANDSCAPE_TYPE_GRASS_PLAIN: {
                color = LANDSCAPE_TYPE_COLOR_GRASS_PLAIN;
                break;
            }
            case MapCoder.LANDSCAPE_TYPE_OUTSIDE: {
                color = LANDSCAPE_TYPE_COLOR_OUTSIDE;
                break;
            }
            default: {
                color = COLOR_UNDEFINED;
                break;
            }
        }
        return color;
    }

    private static int getForegroundColor(long cellData) {
        int color = COLOR_EMPTY;
        int elkType = MapCoder.decodeElkType(cellData);
        int plantType = MapCoder.decodePlantType(cellData);
        int killerType = MapCoder.decodeKillerType(cellData);
        if (elkType != MapCoder.ELK_TYPE_EMPTY) {
            switch (elkType) {
                case MapCoder.ELK_TYPE_MALE: {
                    color = ELK_TYPE_COLOR_MALE;
                    break;
                }
                case MapCoder.ELK_TYPE_FEMALE: {
                    color = ELK_TYPE_COLOR_FEMALE;
                    break;
                }
                default: {
                    color = COLOR_UNDEFINED;
                    break;
                }
            }
        }
        else if (plantType != MapCoder.PLANT_TYPE_EMPTY) {
            switch (plantType) {
                case MapCoder.PLANT_TYPE_FOOD: {
                    color = PLANT_TYPE_COLOR_FOOD;
                    break;
                }
                default: {
                    color = COLOR_UNDEFINED;
                    break;
                }
            }
        }
        else if (killerType != MapCoder.KILLER_TYPE_EMPTY) {
            switch (killerType) {
                case MapCoder.KILLER_TYPE_HUNTER: {
                    color = KILLER_TYPE_COLOR_HUNTER;
                    break;
                }
                case MapCoder.KILLER_TYPE_PREDATOR: {
                    color = KILLER_TYPE_COLOR_PREDATOR;
                    break;
                }
                default: {
                    color = COLOR_UNDEFINED;
                    break;
                }
            }
        }
        else {
            color = COLOR_UNDEFINED;
        }
        return color;
    }

    private static char getCell(long cellData) {
        char cell = ' ';
        int elkType = MapCoder.decodeElkType(cellData);
        int plantType = MapCoder.decodePlantType(cellData);
        int killerType = MapCoder.decodeKillerType(cellData);
        if (elkType != MapCoder.ELK_TYPE_EMPTY) {
            cell = 'F';
        }
        if (plantType != MapCoder.PLANT_TYPE_EMPTY) {
            cell = 'P';
        }
        if (killerType != MapCoder.KILLER_TYPE_EMPTY) {
            cell = 'H';
        }
        return cell;
    }
}
