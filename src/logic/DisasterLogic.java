package logic;

import graphics.MainPanel;
import graphics.Map.Map;
import graphics.Map.MapCoder;

import java.awt.*;

import static graphics.Map.Map.MAP_SIZE;

// Класс реализует логику бедствий (пожара и вырубки леса)
public class DisasterLogic {

    private  int sizeDeforestationArea;     //
    private int day;                        // Количество дней, сколько идет пожар
    private static Point epicenter;         // Координаты ячейки, с которой начался пожар
    private static boolean isFire;          // Флаг наличия пожара
    private long[][] savedCells;            // Массив для сохранения данных пораженных территорий

    public DisasterLogic() {
        isFire = false;
        sizeDeforestationArea = 3;
        day = 0;
        savedCells = new long[MAP_SIZE * MAP_SIZE][3];
    }

    public void makeFire() {
        // Вычислить координаты выделенной ячейкм
        if (epicenter == null) { epicenter = findEpicenter(); }
        // Если выделенная ячейка это прилежащие территории или вода, обнулить переменные и вернуться
        else if (MainPanel.map.getLandscapeType(epicenter.y, epicenter.x) == MapCoder.LANDSCAPE_TYPE_OUTSIDE
                || MainPanel.map.getLandscapeType(epicenter.y, epicenter.x) == MapCoder.LANDSCAPE_TYPE_WATER) {
            reset();
            return;
        }
        if (savedCells == null) { savedCells = new long[MAP_SIZE * MAP_SIZE][3]; }
        // Во время пожара огонь захватывает ячейки, уничтоженный ландшафт сохраняется в массиве
        if (epicenter != null && isFire) {
            BasicLogic.deletePlant(epicenter.y, epicenter.x);
            boolean creatureBurned = BasicLogic.randomBoolean(40);
            if (creatureBurned && MainPanel.map.getElkType(epicenter.y, epicenter.x) != MapCoder.ELK_TYPE_EMPTY) {
                MainPanel.map.setElkType(MapCoder.ELK_TYPE_EMPTY, epicenter.y, epicenter.x);
                StatisticsLogic.elkDied++;
                StatisticsLogic.elkDiedByFire++;
                MainPanel.events.update(BasicLogic.getDays(), "Лось погиб в пожаре.");
            }
            if (creatureBurned && (MainPanel.map.getKillerType(epicenter.y, epicenter.x) == MapCoder.KILLER_TYPE_PREDATOR_MALE
                    || MainPanel.map.getKillerType(epicenter.y, epicenter.x) == MapCoder.KILLER_TYPE_PREDATOR_FEMALE)) {
                MainPanel.map.setKillerType(MapCoder.ELK_TYPE_EMPTY, epicenter.y, epicenter.x);
                StatisticsLogic.predatorDied++;
                StatisticsLogic.predatorDiedByFire++;
                MainPanel.events.update(BasicLogic.getDays(), "Хищник погиб в пожаре.");
            }
            savedCells[day][0] = MainPanel.map.getDataAt(epicenter.y, epicenter.x);
            savedCells[day][1] = epicenter.y;
            savedCells[day][2] = epicenter.x;
            tryToSpreadFire(MainPanel.map.getDataAt(epicenter.y, epicenter.x), epicenter.y, epicenter.x, 0, 0);
            day++;
            if (day > 180) { isFire = false; } // если пожар длится более полугода (180 дней), прекратить его
        }
        // Если пожар закончился, восстановить ландшафт
        if (!isFire && day > 0) {
            day--;
            MainPanel.map.setDataAt(savedCells[day][0], (int) savedCells[day][1], (int) savedCells[day][2]);
        }
        // Обнулить данные в конце пожара
        if (!isFire && day <= 0) {
            reset();
        }
    }

    // Распространение пожара
    // cellData - данные ячейки, (x,y) - координаты эпицентра
    // xStep, yStep - шаг по осям x и y, на который распространяется пожар
    private boolean tryToSpreadFire(long cellData, int y, int x, int yStep, int xStep) {
        Map map = MainPanel.map;
        if (yStep == 0 && xStep == 0) { // если пожар не распространяется, генерируется случайное направление
            yStep = BasicLogic.randomIntegerInRange(-1, 1);
            xStep = BasicLogic.randomIntegerInRange(-1, 1);
        }
        if (yStep != 0 || xStep != 0) {
            // (xTarget, yTarget) - координаты ячейки,в которую распространяется пожар
            int yTarget = y + yStep;
            int xTarget = x + xStep;
            int landscapeTarget = map.getLandscapeType(yTarget, xTarget);
            // Исключить распространение за пределы Алтайского края, на воду и уже горящие местности
            if (!BasicLogic.isCellInMapRange(yTarget, xTarget)
                    || landscapeTarget == MapCoder.LANDSCAPE_TYPE_OUTSIDE
                    || landscapeTarget == MapCoder.LANDSCAPE_TYPE_WATER
                    || landscapeTarget == MapCoder.LANDSCAPE_TYPE_FIRE) {
                return tryToSpreadFire(cellData, y, x, 0, 0);
            }
            map.setLandscapeType(MapCoder.LANDSCAPE_TYPE_FIRE, y, x);
            moveEpicenter(yStep, xStep);
        }
        return false;
    }

    // Метод возвращает координаты выделенной ячейки и null, если ничего не выделено
    public Point findEpicenter() {
        int y = MainPanel.map.getSelectedRow();
        int x = MainPanel.map.getSelectedColumn();
        if (y != -1 && x != -1) {
            epicenter = new Point(x, y);
            return epicenter;
        }
        return null;
    }

    // Метод перемещает эпицентр на вектор (xStep, yStep)
    public void moveEpicenter(int yStep, int xStep) {
        epicenter.y = epicenter.y + yStep;
        epicenter.x = epicenter.x + xStep;
    }

    public void InvertIsFire() {
        isFire = !isFire;
    }

    public void reset() {
        isFire = false;
        sizeDeforestationArea = 3;
        day = 0;
        epicenter = null;
        savedCells = null;
    }
}
