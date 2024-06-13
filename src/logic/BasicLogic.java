package logic;

import graphics.InfoPanels.Events;
import graphics.MainPanel;
import graphics.Map.Map;
import graphics.Map.MapCoder;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class BasicLogic {
    private static int days = 1;        // Количество пройденных с начала симуляции дней
    private static Timer timer;
    private static int timerDelay = 1000;
    private static Random random = new Random();

    // Метод, запускающий/возобновляющий работу модели
    // создает и запускает таймер, генерирующий действия каждый тик,
    // делает ячейки карты активными, то есть разрешает действия существ и растений.
    public static void start(){
        if (timer == null) {
            timer = new Timer(timerDelay, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Map map = MainPanel.map;
                    for (int y = 0; y < Map.MAP_SIZE; y++) {
                        for (int x = 0; x < Map.MAP_SIZE; x++) {
                            map.setActiveFlagCreature(1, y, x);
                            map.setActiveFlagPlant(1, y, x);
                        }
                    }
                    for (int y = 0; y < Map.MAP_SIZE; y++) {
                        for (int x = 0; x < Map.MAP_SIZE; x++) {
                            perform(map.getDataAt(y, x), y, x);
                        }
                    }
                    map.repaint();
                    StatisticsLogic.update();
                    MainPanel.statistics.update(++days);
                }
            });
            ParametersSetter parametersSetter = new ParametersSetter();
            parametersSetter.setParameters();
        }
        timer.start();
    }

    // Метод приостанавливает работу таймера
    public static void pause() {
        if (timer != null) {
            timer.stop();
        }
    }

    // Метод останавливает таймер и сбрасывает состояние панелей до начального состояния
    public static void stop() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
        MainPanel.map.reset();
        MainPanel.creatureInfo.reset();
        MainPanel.landscapeInfo.reset();
        MainPanel.statistics.reset();
        MainPanel.events.reset();
        days = 1;
    }

    // Метод, побуждающий к выполнению действия животоных, охотников и растений
    private static void perform(long cellData, int y, int x) {
        if (MapCoder.decodeActiveFlagCreature(cellData) == 1 && (MapCoder.decodeElkType(cellData) != MapCoder.ELK_TYPE_EMPTY || MapCoder.decodeKillerType(cellData) != MapCoder.KILLER_TYPE_EMPTY)) {
            if (tryToMove(cellData, y, x, 0, 0)) {}
        }
    }

    // Передвижение существ
    // cellData - данные ячейки, (x,y) - координаты ячейки, из которой ходим
    // xStep, yStep - шаг по осям x и y, на который должно сместиться существо
    private static boolean tryToMove(long cellData, int y, int x, int yStep, int xStep) {
        Map map = MainPanel.map;
        if (yStep == 0 && xStep == 0) { // если существо стоит на месте, генерируется случайное направление
            yStep = randomIntegerInRange(-1, 1);
            xStep = randomIntegerInRange(-1, 1);
        }
        if (yStep != 0 || xStep != 0) {
            // (xTarget, yTarget) - координаты ячейки,в которую совершается движение
            int yTarget = y + yStep;
            int xTarget = x + xStep;
            if (!isCellInMapRange(yTarget, xTarget)) {
                deleteCreature(y, x);
                StatisticsLogic.elkMigrated++;
                MainPanel.events.update(days, "Лось мигрировал.");
                return true;
            }
            // Если в целевой ячейке никого нет, то переместить в нее.
            // По воде и горам перемещаться сложнее и энергозатратнее.
            if (map.getElkType(yTarget, xTarget) == 0 && map.getKillerType(yTarget, xTarget) == 0) {
                moveCreature(y, x, yTarget, xTarget);
                int landscapeTarget = map.getLandscapeType(yTarget, xTarget);
                if (landscapeTarget == MapCoder.LANDSCAPE_TYPE_WATER || landscapeTarget == MapCoder.LANDSCAPE_TYPE_MOUNTAIN) {
                    map.setCreatureEnergy(map.getCreatureEnergy(yTarget, xTarget) - 3, yTarget, xTarget);
                    map.setCreatureHunger(map.getCreatureHunger(yTarget, xTarget) + 3, yTarget, xTarget);
                }
                else {
                    map.setCreatureEnergy(map.getCreatureEnergy(yTarget, xTarget) - 2, yTarget, xTarget);
                    map.setCreatureHunger(map.getCreatureHunger(yTarget, xTarget) + 2, yTarget, xTarget);
                }
                map.setCreatureAge(map.getCreatureAge(yTarget, xTarget) + 1, yTarget, xTarget);
                map.setActiveFlagCreature(0, yTarget, xTarget);
                return true;
            }
        }
        map.setCreatureEnergy(map.getCreatureEnergy(y, x) - 1, y, x);
        map.setCreatureHunger(map.getCreatureHunger(y, x) - 1, y, x);
        map.setCreatureAge(map.getCreatureAge(y, x) + 1, y, x);
        map.setActiveFlagCreature(0, y, x);
        return false;
    }

    // Метод копирует данные из ячейки (x,y) в (xTarget, yTarget) и очищает ячейку (x,y)
    private static void moveCreature(int y, int x, int yTarget, int xTarget) {
        Map map = MainPanel.map;
        if (map.getKillerType(y, x) != 0) {map.setKillerType(map.getKillerType(y, x), yTarget, xTarget); }
        if (map.getElkType(y, x) != 0) { map.setElkType(map.getElkType(y, x), yTarget, xTarget); }
        map.setCreatureAge(map.getCreatureAge(y, x), yTarget, xTarget);
        map.setCreatureEnergy(map.getCreatureEnergy(y, x), yTarget, xTarget);
        map.setCreatureHunger(map.getCreatureHunger(y, x), yTarget, xTarget);
        map.setCreaturePregnancy(map.getCreaturePregnancy(y, x), yTarget, xTarget);
        deleteCreature(y, x);
    }

    // Метод удаляет все данные ячейки (x, y), кроме ландшафта и растения
    private static void deleteCreature(int y, int x) {
        Map map = MainPanel.map;
        if (map.getElkType(y, x) != 0) { map.setElkType(0, y, x); }
        if (map.getKillerType(y, x) != 0) { map.setKillerType(0, y, x); }
        map.setCreatureAge(0, y, x);
        map.setCreatureEnergy(0, y, x);
        map.setCreatureHunger(0, y, x);
        map.setCreaturePregnancy(0, y, x);
    }

    // Метод для проверки, находится ли ячейка (x, y) в пределах карты
    private static boolean isCellInMapRange(int y, int x) {
        return y >= 0 && y < Map.MAP_SIZE && x >= 0 && x < Map.MAP_SIZE;
    }

    // Метод генерирует случайное целое число в диапазоне [min, max]
    public static int randomIntegerInRange(int min, int max) {
        if (min > max) {
            throw new RuntimeException(String.format("Ошибка! Диапазон значений от [min] = [%s] до [max] = [%s].", min, max));
        }
        return min + random.nextInt(max - min + 1);
    }
}
