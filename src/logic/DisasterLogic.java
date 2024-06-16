package logic;

import graphics.MainPanel;
import graphics.Map.Map;
import graphics.Map.MapCoder;

import javax.swing.*;
import java.awt.*;

import static graphics.Map.Map.MAP_SIZE;

// Класс реализует логику бедствий (пожара и вырубки леса)
public class DisasterLogic {

    private int day;                              // Количество дней, сколько идет пожар
    private static Point epicenter;               // Координаты ячейки, с которой начался пожар
    private static Point centerDeforestation;     // Координаты центра зоны вырубки леса
    private static int radiusDeforestationArea;   // Радиус зоны вырубки леса
    private static boolean isFire;                // Флаг наличия пожара (true - горит, false - пожара нет)
    private static boolean isCutDown;             // Флаг вырубки леса (true - лес вырублен, false - не вырублен)
    private long[][] savedCellsFire;              // Массив для сохранения данных пораженных огнём территорий
    private long[][] savedCellsDeforestation;     // Массив для сохранения данных вырубленных территорий

    public DisasterLogic() {
        isFire = false;
        isCutDown = false;
        radiusDeforestationArea = 0;
        day = 0;
        savedCellsFire = new long[MAP_SIZE * MAP_SIZE][3];
        savedCellsDeforestation = new long[MAP_SIZE * MAP_SIZE][3];
    }

    // Метод управляет поведением огня во время пожара
    public void makeFire() {
        if (isFire) {
            // Вычислить координаты выделенной ячейкм
            if (epicenter == null) {
                epicenter = findEpicenter();
            }
            // Если выделенная ячейка это прилежащие территории или вода, обнулить переменные и вернуться
            else if (MainPanel.map.getLandscapeType(epicenter.y, epicenter.x) == MapCoder.LANDSCAPE_TYPE_OUTSIDE
                    || MainPanel.map.getLandscapeType(epicenter.y, epicenter.x) == MapCoder.LANDSCAPE_TYPE_WATER) {
                reset();
                return;
            }
            if (savedCellsFire == null) {
                savedCellsFire = new long[MAP_SIZE * MAP_SIZE][3];
            }
            // Во время пожара огонь захватывает ячейки, уничтоженный ландшафт сохраняется в массиве
            if (epicenter != null) {
                BasicLogic.deletePlant(epicenter.y, epicenter.x);
                boolean creatureBurned = BasicLogic.randomBoolean(40); // вероятность, что животное погибнет от пожара
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
                // Сохранение ячеек в массиве
                savedCellsFire[day][0] = MainPanel.map.getDataAt(epicenter.y, epicenter.x);
                savedCellsFire[day][1] = epicenter.y;
                savedCellsFire[day][2] = epicenter.x;
                tryToSpreadFire(MainPanel.map.getDataAt(epicenter.y, epicenter.x), epicenter.y, epicenter.x, 0, 0);
                day++;
                if (day > 180) {
                    isFire = false;
                } // если пожар длится более полугода (180 дней), прекратить его
            }
        }
        // Если пожар закончился, восстановить ландшафт
        if (!isFire && day > 0) {
            day--;
            MainPanel.map.setDataAt(savedCellsFire[day][0], (int) savedCellsFire[day][1], (int) savedCellsFire[day][2]);
        }
        // Обнулить данные в конце пожара и разрешить доступ к вырубке леса
        if (!isFire && day <= 0) {
            MainPanel.parameters.setEnabledDisasterButtons(true);
            resetFire();
        }
    }

    // Метод реализует вырубку леса
    // Вырубка происходит квадратом, который строится на основе выделенной ячейки,
    // расстояние от которой до границ квадрата равняется радиусу вырубки
    public void makeDeforestation() {
        if (!isCutDown && radiusDeforestationArea > 0) {
            if (savedCellsDeforestation == null) { savedCellsDeforestation = new long[MAP_SIZE * MAP_SIZE][3]; }
            centerDeforestation = findEpicenter();
            // Вывод напоминания, если выбран радиус вырубки, но ячейка не была заранее выделена или определена
            if (centerDeforestation == null) {
                ImageIcon icon = new ImageIcon("src/resources/icon.png");
                JOptionPane.showMessageDialog(MainPanel.map,
                        "Выделите ячейку!!!",
                        "Напоминание", JOptionPane.INFORMATION_MESSAGE,
                        icon);
                radiusDeforestationArea = 0;
                return;
            }
            // Удаление растений и запись ячеек в массив
            int leftUpCornerX = centerDeforestation.x - radiusDeforestationArea;
            int leftUpCornerY = centerDeforestation.y - radiusDeforestationArea;
            int cell = 0;
            for (int yStep = 0; yStep < radiusDeforestationArea * 2 + 1; yStep++) {
                for (int xStep = 0; xStep < radiusDeforestationArea * 2 + 1; xStep++) {
                    if (BasicLogic.isCellInMapRange(leftUpCornerY + yStep, leftUpCornerX + xStep)
                            && MainPanel.map.getLandscapeType(leftUpCornerY + yStep, leftUpCornerX + xStep) != MapCoder.LANDSCAPE_TYPE_WATER) {
                        BasicLogic.deletePlant(leftUpCornerY + yStep, leftUpCornerX + xStep);
                        savedCellsDeforestation[cell][0] = MainPanel.map.getDataAt(leftUpCornerY + yStep, leftUpCornerX + xStep);
                        savedCellsDeforestation[cell][1] = leftUpCornerY + yStep;
                        savedCellsDeforestation[cell][2] = leftUpCornerX + xStep;
                        MainPanel.map.setLandscapeType(MapCoder.LANDSCAPE_TYPE_OUTSIDE, leftUpCornerY + yStep, leftUpCornerX + xStep);
                        cell++;
                    }
                }
            }
            isCutDown = true;
            return;
        }
        // На второй тик таймера восстановить ландшафт без растений и обнулить переменные, закончить вырубку
        if (isCutDown && radiusDeforestationArea > 0) {
            int numberOfCells = (2 * radiusDeforestationArea + 1) * (2 * radiusDeforestationArea + 1);
            for (int cell = 0; cell < numberOfCells; cell++) {
                MainPanel.map.setDataAt(savedCellsDeforestation[cell][0], (int) savedCellsDeforestation[cell][1], (int) savedCellsDeforestation[cell][2]);
            }
            MainPanel.events.update(BasicLogic.getDays(), "Вырубка леса");
            resetDeforestation();
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

    // Смена состояния горит/не горит
    public boolean InvertIsFire() {
        isFire = !isFire;
        return isFire;
    }

    public void setRadiusDeforestationArea(int radius) {
        radiusDeforestationArea = radius;
    }

    public void resetDeforestation() {
        isCutDown = false;
        centerDeforestation = null;
        radiusDeforestationArea = 0;
        savedCellsDeforestation = null;
    }

    public void resetFire() {
        isFire = false;
        day = 0;
        epicenter = null;
        savedCellsFire = null;
    }

    public void reset() {
        isFire = false;
        isCutDown = false;
        radiusDeforestationArea = 0;
        day = 0;
        epicenter = null;
        savedCellsFire = null;
    }
}
