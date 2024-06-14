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
        StatisticsLogic.reset();
        days = 1;
    }

    // Метод, побуждающий к выполнению действия животоных, охотников и растений
    private static void perform(long cellData, int y, int x) {
        if (MapCoder.decodeActiveFlagCreature(cellData) == 1 && (MapCoder.decodeElkType(cellData) != MapCoder.ELK_TYPE_EMPTY || MapCoder.decodeKillerType(cellData) != MapCoder.KILLER_TYPE_EMPTY)) {
            if (MainPanel.map.getKillerType(y, x) != MapCoder.KILLER_TYPE_HUNTER
                    && (tryToDie(cellData, y, x)
                    || tryToGiveBirth(cellData, y, x)
                    || tryToEat(cellData, y, x)
                    || tryToSleep(cellData, y, x)
                    || tryToGetPregnant(cellData, y, x)
                    || tryToMove(cellData, y, x, 0, 0))) {}
            if (MainPanel.map.getKillerType(y, x) == MapCoder.KILLER_TYPE_HUNTER
                    && (tryToKill(cellData, y, x)
                    || tryToMove(cellData, y, x, 0, 0))) {}
        }
        if (MapCoder.decodeActiveFlagPlant(cellData) == 1 && MapCoder.decodePlantType(cellData) != MapCoder.PLANT_TYPE_EMPTY) {
            tryToGrowFood(cellData, y, x);
            tryToSpreadFood(cellData, y, x);
        }
    }

    // Метод реализует смерть животных от разных факторов.
    private static boolean tryToDie(long cellData, int y, int x) {
        Events events = MainPanel.events;
        boolean isElk = MainPanel.map.getElkType(y, x) == MapCoder.ELK_TYPE_EMPTY ? false : true;
        int energy = MapCoder.decodeCreatureEnergy(cellData);
        if (energy == 0) {
            deleteCreature(y, x);
            if (isElk) {
                StatisticsLogic.elkDied++;
                StatisticsLogic.elkDiedByEnergy++;
                events.update(days, "Лось погиб от недостатка энергии.");
            }
            else {
                StatisticsLogic.predatorDied++;
                StatisticsLogic.predatorDiedByEnergy++;
                events.update(days, "Хищник погиб от недостатка энергии.");
            }
            return true;
        }
        int hunger = MapCoder.decodeCreatureHunger(cellData);
        if (hunger == 63) {
            deleteCreature(y, x);
            if (isElk) {
                StatisticsLogic.elkDied++;
                StatisticsLogic.elkDiedByHunger++;
                events.update(days, "Лось погиб от голода.");
            }
            else {
                StatisticsLogic.predatorDied++;
                StatisticsLogic.predatorDiedByHunger++;
                events.update(days, "Хищник погиб от голода.");
            }
            return true;
        }
        // Чем старше животное, тем больше вероятность, что оно умрет от старости.
        // Вероятность рассчитывается по формуле probability = 10 * age (в годах) - 150;
        // Средняя максимальная продолжительность жизни животных в неволе 25 лет.
        boolean decideToDie = randomBoolean(10 * (MapCoder.decodeCreatureAge(cellData) / 360) - 150);
        if (decideToDie) {
            deleteCreature(y, x);
            if (isElk) {
                StatisticsLogic.elkDied++;
                StatisticsLogic.elkDiedByAge++;
                events.update(days, "Лось погиб от старости.");
            }
            else {
                StatisticsLogic.predatorDied++;
                StatisticsLogic.predatorDiedByAge++;
                events.update(days, "Хищник погиб от старости.");
            }
            return true;
        }
        return false;
    }

    // Рождение детёныша. Беременность животного длится 240 дней.
    // Детеныш умирает, если нет свободной ячейки рядом с матерью.
    private static boolean tryToGiveBirth(long cellData, int y, int x) {
        Map map = MainPanel.map;
        int pregnancy = map.getCreaturePregnancy(y, x);
        if (pregnancy != 0 && pregnancy < 240) {
            map.setCreaturePregnancy(map.getCreaturePregnancy(y, x) + 1, y, x);
        }
        else if (pregnancy == 240) {
            map.setCreaturePregnancy(0, y, x);
            map.setCreatureEnergy(map.getCreatureEnergy(y, x) - 4, y, x);
            map.setCreatureHunger(map.getCreatureHunger(y, x) + 4, y, x);
            map.setCreatureAge(map.getCreatureAge(y, x) + 1, y, x);
            map.setActiveFlagCreature(0, y, x);
            for (int yStep = -1; yStep < 2; yStep++) {
                for (int xStep = -1; xStep < 2; xStep++) {
                    int yTarget = y + yStep;
                    int xTarget = x + xStep;
                    if (isCellInMapRange(yTarget, xTarget) && map.getElkType(yTarget, xTarget) == MapCoder.ELK_TYPE_EMPTY && map.getKillerType(yTarget, xTarget) == MapCoder.KILLER_TYPE_EMPTY) {
                        map.setCreatureAge(1, yTarget, xTarget);
                        map.setCreatureEnergy(63, yTarget, xTarget);
                        map.setCreatureHunger(0, yTarget, xTarget);
                        map.setCreaturePregnancy(0, yTarget, xTarget);
                        map.setActiveFlagCreature(0, yTarget, xTarget);
                        if (map.getElkType(y, x) != MapCoder.ELK_TYPE_EMPTY) {
                            map.setElkType(random.nextBoolean() ? MapCoder.ELK_TYPE_MALE : MapCoder.ELK_TYPE_FEMALE, yTarget, xTarget);
                            StatisticsLogic.babyElkWereBorn++;
                            MainPanel.events.update(days, "Родился детёныш лося.");
                        }
                        else {
                            map.setKillerType(random.nextBoolean() ? MapCoder.KILLER_TYPE_PREDATOR_MALE : MapCoder.KILLER_TYPE_PREDATOR_FEMALE, yTarget, xTarget);
                            StatisticsLogic.babyPredatorWereBorn++;
                            MainPanel.events.update(days, "Родился детёныш хищника.");
                        }
                        return true;
                    }
                }
            }
            map.setCreaturePregnancy(0, y, x);
            if (map.getElkType(y, x) != MapCoder.ELK_TYPE_EMPTY) {
                StatisticsLogic.babyElkDied++;
                MainPanel.events.update(days, "Детеныш лося погиб.");
            }
            else {
                StatisticsLogic.babyPredatorDied++;
                MainPanel.events.update(days, "Детеныш хищника погиб.");
            }
            return true;
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

    // Метод реализует процесс утоления голода посредством потребления растений
    // лосями и лосей хищниками.
    // Чем больше уровень голода, тем больше вероятность, что существо будет есть
    // Вероятность рассчитывается по формуле probability = 2 * hunger - 20;
    private static boolean tryToEat(long cellData, int y, int x) {
        Map map = MainPanel.map;
        boolean decideToEat = false;
        if (map.getElkType(y, x) != MapCoder.ELK_TYPE_EMPTY) { decideToEat = randomBoolean(2 * MapCoder.decodeCreatureHunger(cellData) - 20); }
        else { decideToEat = randomBoolean(3 * MapCoder.decodeCreatureHunger(cellData) - 89); }
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
                                decideToEat = randomBoolean(10);
                                if (decideToEat) {
                                    deleteCreature(yTarget, xTarget);
                                    MainPanel.events.update(days, "Лось съеден хищником.");
                                    StatisticsLogic.elkDied++;
                                    StatisticsLogic.elkDiedByPredator++;
                                    map.setCreatureHunger(map.getCreatureHunger(y, x) - 63, y, x);
                                }
                                else { map.setCreatureHunger(map.getCreatureHunger(y, x) - 63, y, x); }
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

    // Метод, отвечающий за беременность существ.
    // Животные могут забеременеть в возрасте старше 1го года (360 дней)
    // Лоси беременеют с августа по ноябрь (прошло от 7 до 11 месяцев).
    // Хищники беременеют с апреля по август.
    private static boolean tryToGetPregnant(long cellData, int y, int x) {
        Map map = MainPanel.map;
        int month = days / 30 % 12;
        if (map.getCreaturePregnancy(y, x) == 0
                && map.getCreatureAge(y, x) > 359
                && ((map.getElkType(y, x) == MapCoder.ELK_TYPE_FEMALE
                && month > 7 && month < 11)
                || (map.getKillerType(y, x) == MapCoder.KILLER_TYPE_PREDATOR_FEMALE
                && month > 3 && month < 8))) {
            for (int yStep = -1; yStep < 2; yStep++) {
                for (int xStep = -1; xStep < 2; xStep++) {
                    int yTarget = y + yStep;
                    int xTarget = x + xStep;
                    if (isCellInMapRange(yTarget, xTarget)
                            && map.getActiveFlagCreature(yTarget, xTarget) == 1
                            && (map.getElkType(y, x) == MapCoder.ELK_TYPE_FEMALE && map.getElkType(yTarget, xTarget) == MapCoder.ELK_TYPE_MALE
                            || map.getKillerType(y, x) == MapCoder.KILLER_TYPE_PREDATOR_FEMALE && map.getKillerType(yTarget, xTarget) == MapCoder.KILLER_TYPE_PREDATOR_MALE)) {
                        boolean decideToGetPregnant = randomBoolean(95);
                        if (decideToGetPregnant) {
                            map.setCreaturePregnancy(1, y, x);
                            map.setCreatureEnergy(map.getCreatureEnergy(y, x) - 4, y, x);
                            map.setCreatureHunger(map.getCreatureHunger(y, x) + 4, y, x);
                            map.setCreatureAge(map.getCreatureAge(y, x) + 1, y, x);
                            map.setActiveFlagCreature(0, y, x);
                            map.setCreatureEnergy(map.getCreatureEnergy(yTarget, xTarget) - 4, yTarget, xTarget);
                            map.setCreatureHunger(map.getCreatureHunger(yTarget, xTarget) + 4, yTarget, xTarget);
                            map.setCreatureAge(map.getCreatureAge(yTarget, xTarget) + 1, yTarget, xTarget);
                            map.setActiveFlagCreature(0, yTarget, xTarget);
                            if (map.getElkType(y, x) != MapCoder.ELK_TYPE_EMPTY) {
                                MainPanel.events.update(days, "Лосиха забеременела."); }
                            else {MainPanel.events.update(days, "Хищница забеременела."); }
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private static boolean tryToKill(long cellData, int y, int x) {
        Map map = MainPanel.map;
        // Если 1 апреля, рассчитать лимит добычи
        if (days / 30 % 12 == 3 && days % 30 == 0) {
            ParametersSetter parametersSetter = new ParametersSetter();
            StatisticsLogic.limitOfHuntingElkForSeason = parametersSetter.setLimitOfHuntingElk();
            StatisticsLogic.limitOfHuntingPredatorForSeason = parametersSetter.setLimitOfHuntingPredator();
            StatisticsLogic.elkDiedByHunterForSeason = 0;
            StatisticsLogic.predatorDiedByHunterForSeason = 0;
        }
        // C сентября по январь сезон охоты
        if (days / 30 % 12 > 7 && (StatisticsLogic.elkDiedByHunterForSeason < StatisticsLogic.limitOfHuntingElkForSeason
                || StatisticsLogic.predatorDiedByHunterForSeason < StatisticsLogic.limitOfHuntingPredatorForSeason)) {
            boolean decideToHunt = randomBoolean(5);
            if (decideToHunt) {
                // Если принято решение поохотиться и добыча находится в ближайшем доступном диапазоне, то добыть
                for (int yStep = -1; yStep < 2; yStep++) {
                    for (int xStep = -1; xStep < 2; xStep++) {
                        int yTarget = y + yStep;
                        int xTarget = x + xStep;
                        if (isCellInMapRange(yTarget, xTarget)) {
                            if (StatisticsLogic.elkDiedByHunterForSeason < StatisticsLogic.limitOfHuntingElkForSeason) {
                                int huntTarget = map.getElkType(yTarget, xTarget);
                                if (huntTarget != MapCoder.ELK_TYPE_EMPTY) {
                                    deleteCreature(yTarget, xTarget);
                                    MainPanel.events.update(days, "Лось убит охотником.");
                                    StatisticsLogic.elkDied++;
                                    StatisticsLogic.elkDiedByHunter++;
                                    StatisticsLogic.elkDiedByHunterForSeason++;
                                    map.setActiveFlagCreature(0, y, x);
                                    return true;
                                }
                            } else if (StatisticsLogic.predatorDiedByHunterForSeason < StatisticsLogic.limitOfHuntingPredatorForSeason) {
                                int huntTarget = map.getKillerType(yTarget, xTarget);
                                if (huntTarget == MapCoder.KILLER_TYPE_PREDATOR_MALE || huntTarget == MapCoder.KILLER_TYPE_PREDATOR_FEMALE) {
                                    deleteCreature(yTarget, xTarget);
                                    MainPanel.events.update(days, "Хищник убит охотником.");
                                    StatisticsLogic.predatorDied++;
                                    StatisticsLogic.predatorDiedByHunter++;
                                    StatisticsLogic.predatorDiedByHunterForSeason++;
                                    map.setActiveFlagCreature(0, y, x);
                                    return true;
                                }
                            }
                        }
                    }
                }
                // Если рядом нет добычи, то искать ближайшую ячейку с ней.
                // По гипотенузе прямоугольного треугольника с катетами xDistance и yDistance,
                // где xDistance и yDistance - расстояния от текущей ячейки до целевой по осям x и y.
                int minTarget = Map.MAP_SIZE * Map.MAP_SIZE + Map.MAP_SIZE * Map.MAP_SIZE;
                int yTarget = y;
                int xTarget = x;
                for (int yTemp = 0; yTemp < Map.MAP_SIZE; yTemp++) {
                    for (int xTemp = 0; xTemp < Map.MAP_SIZE; xTemp++) {
                        if ((map.getKillerType(yTemp, xTemp) != MapCoder.KILLER_TYPE_EMPTY
                                && map.getKillerType(yTemp, xTemp) != MapCoder.KILLER_TYPE_HUNTER
                                && StatisticsLogic.predatorDiedByHunterForSeason < StatisticsLogic.limitOfHuntingPredatorForSeason)
                                || (map.getElkType(yTemp, xTemp) != 0 && StatisticsLogic.elkDiedByHunterForSeason < StatisticsLogic.limitOfHuntingElkForSeason)) {
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
                // Пока охотник не дойдет до ячейки с добычей, он направляется к цели по кратчайшему пути
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
            // Исключить миграцию охотников за пределы Алтайского края
            if (map.getKillerType(y, x) == MapCoder.KILLER_TYPE_HUNTER
                    && map.getLandscapeType(yTarget, xTarget) == MapCoder.LANDSCAPE_TYPE_OUTSIDE) {
                return tryToMove(cellData, y, x, 0, 0);
            }
            if (!isCellInMapRange(yTarget, xTarget)) {
                if (map.getElkType(y, x) != MapCoder.ELK_TYPE_EMPTY) {
                    StatisticsLogic.elkMigrated++;
                    MainPanel.events.update(days, "Лось мигрировал.");
                }
                else if (map.getKillerType(y, x) == MapCoder.KILLER_TYPE_PREDATOR_MALE
                    || map.getKillerType(y, x) == MapCoder.KILLER_TYPE_PREDATOR_FEMALE) {
                    StatisticsLogic.predatorMigrated++;
                    MainPanel.events.update(days, "Хищник мигрировал.");
                }
                deleteCreature(y, x);
                return true;
            }
            // Если в целевой ячейке никого нет, то переместить в нее.
            // По воде и горам перемещаться сложнее и энергозатратнее.
            if (map.getElkType(yTarget, xTarget) == 0 && map.getKillerType(yTarget, xTarget) == 0) {
                moveCreature(y, x, yTarget, xTarget);
                int landscapeTarget = map.getLandscapeType(yTarget, xTarget);
                if (map.getElkType(y, x) != MapCoder.ELK_TYPE_EMPTY) {
                    if (landscapeTarget == MapCoder.LANDSCAPE_TYPE_WATER || landscapeTarget == MapCoder.LANDSCAPE_TYPE_MOUNTAIN) {
                        map.setCreatureEnergy(map.getCreatureEnergy(yTarget, xTarget) - 3, yTarget, xTarget);
                        map.setCreatureHunger(map.getCreatureHunger(yTarget, xTarget) + 3, yTarget, xTarget);
                    } else {
                        map.setCreatureEnergy(map.getCreatureEnergy(yTarget, xTarget) - 2, yTarget, xTarget);
                        map.setCreatureHunger(map.getCreatureHunger(yTarget, xTarget) + 2, yTarget, xTarget);
                    }
                }
                else {
                    map.setCreatureEnergy(map.getCreatureEnergy(yTarget, xTarget) - 5, yTarget, xTarget);
                    map.setCreatureHunger(map.getCreatureHunger(yTarget, xTarget) + 1, yTarget, xTarget);}
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
