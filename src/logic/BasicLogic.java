package logic;

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
    private static int timerDelay = 100;
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
            if (tryToSleep(cellData, y, x)
                    ||tryToEat(cellData, y, x)
                    || tryToMove(cellData, y, x, 0, 0)) {}
        }
        if (MapCoder.decodeActiveFlagPlant(cellData) == 1 && MapCoder.decodePlantType(cellData) != MapCoder.PLANT_TYPE_EMPTY) {
            tryToGrowFood(cellData, y, x);
            tryToSpreadFood(cellData, y, x);
        }
    }

    // Метод реализует процесс утоления голода посредством ..................
    // Чем больше уровень голода, тем больше вероятность, что существо будет есть
    // Вероятность рассчитывается по формуле probability = 2 * hunger - 20;
    private static boolean tryToEat(long cellData, int y, int x) {
        Map map = MainPanel.map;
        boolean decideToEat = randomBoolean(2 * MapCoder.decodeCreatureHunger(cellData) - 20);
        if (decideToEat) {
            // Если принято решение поесть и еда находится в ближайшем доступном диапазоне, то есть
            for (int yStep = -1; yStep < 2; yStep++) {
                for (int xStep = -1; xStep < 2; xStep++) {
                    int yTarget = y + yStep;
                    int xTarget = x + xStep;
                    if (isCellInMapRange(yTarget, xTarget)) {
                        if (map.getKillerType(y, x) == MapCoder.KILLER_TYPE_PREDATOR_MALE || map.getKillerType(y, x) == MapCoder.KILLER_TYPE_PREDATOR_FEMALE) {
                            int foodTarget = map.getElkType(yTarget, xTarget);
                            if (foodTarget != MapCoder.ELK_TYPE_EMPTY) {
                                deleteCreature(yTarget, xTarget);
                                MainPanel.events.update(days, "Лось съеден хищником.");
                                map.setCreatureHunger(map.getCreatureHunger(y, x) - 63, y, x);
                                map.setCreatureEnergy(map.getCreatureEnergy(y, x) - 1, y, x);
                                map.setCreatureAge(map.getCreatureAge(y, x) + 1, y, x);
                                map.setActiveFlagCreature(0, y, x);
                                return true;
                            }
                        } else if (map.getElkType(y, x) != MapCoder.ELK_TYPE_EMPTY) {
                            int foodTarget = map.getPlantFood(yTarget, xTarget);
                            if (foodTarget != 0) {
                                if (map.getCreatureAge(y, x) < 720) {
                                    map.setPlantFood(--foodTarget, yTarget, xTarget);
                                } else {
                                    map.setPlantFood(--foodTarget, yTarget, xTarget);
                                }
                                map.setCreatureHunger(map.getCreatureHunger(y, x) - 32, y, x);
                                map.setCreatureEnergy(map.getCreatureEnergy(y, x) - 1, y, x);
                                map.setCreatureAge(map.getCreatureAge(y, x) + 1, y, x);
                                map.setActiveFlagCreature(0, y, x);
                                return true;
                            }
                        }
                    }
                }
            }
            // Если рядом нет еды, то искать ближайшую ячейку со съедобными растениями
            // По гипотенузе прямоугольного треугольника с катетами xDistance и yDistance,
            // где xDistance и yDistance - расстояния от текущей ячейки до целевой по осям x и y.
            int minTarget = Map.MAP_SIZE * Map.MAP_SIZE + Map.MAP_SIZE * Map.MAP_SIZE;
            int yTarget = y;
            int xTarget = x;
            for (int yTemp = 0; yTemp < Map.MAP_SIZE; yTemp++) {
                for (int xTemp = 0; xTemp < Map.MAP_SIZE; xTemp++) {
                    if ((map.getPlantFood(yTemp, xTemp) != 0 && map.getElkType(y, x) != MapCoder.ELK_TYPE_EMPTY)
                    || (map.getElkType(yTemp, xTemp) != 0 && (map.getKillerType(y, x) == MapCoder.KILLER_TYPE_PREDATOR_MALE || map.getKillerType(y, x) == MapCoder.KILLER_TYPE_PREDATOR_FEMALE))) {
                        int yDistance = Math.abs(yTemp - y);
                        int xDistance = Math.abs(xTemp - x);
                        int minTemp = yDistance * yDistance + xDistance * xDistance;
                        if (minTemp < minTarget) {
                            minTarget = minTemp;
                            yTarget = yTemp;
                            xTarget = xTemp;
                        }
                    }
                }
            }
            // Пока животное не дойдет до ячейки с едой, оно направляется к цели по кратчайшему пути
            if (yTarget != y || xTarget != x) {
                int yStep = 0; // шаг, который нужно сделать
                int xStep = 0;
                int yDistance = yTarget - y; // расстояние до целевой ячейки
                int xDistance = xTarget - x;
                if (yDistance < 0) {
                    yStep = -1;
                } else if (yDistance > 0) {
                    yStep = 1;
                }
                if (xDistance < 0) {
                    xStep = -1;
                } else if (xDistance > 0) {
                    xStep = 1;
                }
                return tryToMove(cellData, y, x, yStep, xStep);
            }
        }
        return false;
    }

    // Метод реализует сон существ, при котором происходит пополнение энергии
    // Энергия кодируется 6 битами, то есть максимальное значение 63,
    // Чем меньше энергия, тем больше вероятность, что существо будет спать
    // Вероятность рассчитывается по формуле probability = 120 - 2 * energy;
    private static boolean tryToSleep(long cellData, int y, int x) {
        boolean decideToSleep = randomBoolean(120 - 2 * MapCoder.decodeCreatureEnergy(cellData));
        if (decideToSleep) {
            Map map = MainPanel.map;
            map.setCreatureEnergy(63, y, x);
            map.setCreatureHunger(map.getCreatureHunger(y, x) + 1, y, x);
            map.setCreatureAge(map.getCreatureAge(y, x) + 1, y, x);
            map.setActiveFlagCreature(0, y, x);
            return true;
        }
        return false;
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
        map.setCreatureHunger(map.getCreatureHunger(y, x) + 1, y, x);
        map.setCreatureAge(map.getCreatureAge(y, x) + 1, y, x);
        map.setActiveFlagCreature(0, y, x);
        return false;
    }

    // Метод определяет рост съедобных растений в ячейках, где они уже имеются.
    // За месяц возобновляется до 9% съедобных растений (100% - все растения данной территории).
    private static void tryToGrowFood(long cellData, int y, int x) {
        if (days % 30 == 0) {
            Map map = MainPanel.map;
            map.setPlantFood(map.getPlantFood(y, x) + randomIntegerInRange(5, 15), y, x);
        }
    }

    // Метод определяет распространение съедобных растений на соседние территории.
    // С вероятностью 0,5% наступают благоприятные условия для разрастания,
    // и если рядом имеется пустая ячейка, то она заполняется растением.
    // Если в ячейке съедобных растений не осталось, то удаляем растение с карты.
    private static void tryToSpreadFood(long cellData, int y, int x) {
        Map map = MainPanel.map;
        if (map.getPlantFood(y, x) == 0) {
            map.setPlantType(MapCoder.PLANT_TYPE_EMPTY, y, x);
            return;
        }
        // 1% && 50% = 0.01 * 0.50 = 0.005 = 0.5%
        boolean decideToSpread = randomBoolean(1) && randomBoolean(50);
        if (decideToSpread) {
            int yTarget = y + randomIntegerInRange(-2, 2);
            int xTarget = x + randomIntegerInRange(-2, 2);
            if (isCellInMapRange(yTarget, xTarget) && (yTarget != y || xTarget != x)) {
                int landscapeTarget = map.getLandscapeType(yTarget, xTarget);
                int elkTarget = map.getElkType(yTarget, xTarget);
                int killerTarget = map.getKillerType(yTarget, xTarget);
                int plantTarget = map.getPlantType(yTarget, xTarget);
                if (landscapeTarget != MapCoder.LANDSCAPE_TYPE_WATER
                        && landscapeTarget != MapCoder.LANDSCAPE_TYPE_OUTSIDE
                        && elkTarget == MapCoder.ELK_TYPE_EMPTY
                        && killerTarget == MapCoder.KILLER_TYPE_EMPTY
                        && plantTarget == MapCoder.PLANT_TYPE_EMPTY) {
                    map.setPlantType(MapCoder.PLANT_TYPE_PLANT, yTarget, xTarget);
                }
            }
        }
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

    // Метод генерирует true с вероятностью probability
    public static boolean randomBoolean(int probability) {
        probability = Protection.getValueInRange(probability, 0, 100);
        return (random.nextInt(100) < probability);
    }

    // Метод генерирует случайное целое число в диапазоне [min, max]
    public static int randomIntegerInRange(int min, int max) {
        if (min > max) {
            throw new RuntimeException(String.format("Ошибка! Диапазон значений от [min] = [%s] до [max] = [%s].", min, max));
        }
        return min + random.nextInt(max - min + 1);
    }
}
