package logic;

import graphics.MainPanel;
import graphics.Map.Map;
import graphics.Map.MapCoder;

import java.util.Random;

public class ParametersSetter {

    private static final int MAX_NUMBERS_OF_CREATURES = 1500;
    private static final int MIN_X = 2;
    private static final int MAX_X = 61;
    private static final int MIN_Y = 6;
    private static final int MAX_Y = 57;
    private static Random random = new Random();

    private final int numberOfElks;
    private final int numberOfHunters;
    private final int numberOfPredators;
    private final int limitOfHuntingElk;
    private final int limitOfHuntingPredator;

    ParametersSetter() {
        numberOfElks = MainPanel.parameters.getValueElksSlider();
        numberOfHunters = MainPanel.parameters.getValueHuntersSlider();
        numberOfPredators = MainPanel.parameters.getValuePredatorsSlider();
        limitOfHuntingElk = MainPanel.parameters.getValueLimitOfElkSlider();
        limitOfHuntingPredator = MainPanel.parameters.getValueLimitOfPredatorsSlider();
    }

    public void setParameters() {
        setNumberOfElks();
        setNumberOfHunters();
        setNumberOfPredators();
        setLimitOfHuntingElk();
        setLimitOfHuntingPredator();
    }

    public void setNumberOfElks() {
        Map map = MainPanel.map;
        StatisticsLogic.elk = numberOfElks;
        int countOfElks = numberOfElks;
        int randomX = 0, randomY = 0;
        while (countOfElks > 0) {
            randomX = MIN_X + random.nextInt(MAX_X - MIN_X + 1);
            randomY = MIN_Y + random.nextInt(MAX_Y - MIN_Y + 1);
            if (map.getElkType(randomY, randomX) == MapCoder.ELK_TYPE_EMPTY
                    && map.getKillerType(randomY, randomX) == MapCoder.KILLER_TYPE_EMPTY
                    && map.getPlantType(randomY, randomX) == MapCoder.PLANT_TYPE_EMPTY
                    && map.getLandscapeType(randomY, randomX) != MapCoder.LANDSCAPE_TYPE_OUTSIDE
                    && map.getLandscapeType(randomY, randomX) != MapCoder.LANDSCAPE_TYPE_WATER) {
                map.setElkType(random.nextBoolean() ? MapCoder.ELK_TYPE_MALE : MapCoder.ELK_TYPE_FEMALE, randomY, randomX);
                map.setCreatureEnergy(63, randomY, randomX);
                map.setCreatureAge(BasicLogic.randomIntegerInRange(1, 1620), randomY, randomX);
                countOfElks--;
            }
        }
    }

    public void setNumberOfHunters() {
        Map map = MainPanel.map;
        StatisticsLogic.hunter = numberOfHunters;
        int countOfHunters = numberOfHunters;
        int randomX = 0, randomY = 0;
        while (countOfHunters > 0) {
            randomX = MIN_X + random.nextInt(MAX_X - MIN_X + 1);
            randomY = MIN_Y + random.nextInt(MAX_Y - MIN_Y + 1);
            if (map.getElkType(randomY, randomX) == MapCoder.ELK_TYPE_EMPTY
                    && map.getKillerType(randomY, randomX) == MapCoder.KILLER_TYPE_EMPTY
                    && map.getPlantType(randomY, randomX) == MapCoder.PLANT_TYPE_EMPTY
                    && map.getLandscapeType(randomY, randomX) != MapCoder.LANDSCAPE_TYPE_OUTSIDE
                    && map.getLandscapeType(randomY, randomX) != MapCoder.LANDSCAPE_TYPE_WATER) {
                map.setKillerType(MapCoder.KILLER_TYPE_HUNTER, randomY, randomX);
                countOfHunters--;
            }
        }
    }

    public int setLimitOfHuntingElk() {
        int numberOfElk = StatisticsLogic.elk;
        return numberOfElk * limitOfHuntingElk / 100;
    }

    public int setLimitOfHuntingPredator() {
        int numberOfPredator = StatisticsLogic.predator;
        return numberOfPredator * limitOfHuntingPredator / 100;
    }

    public void setNumberOfPredators() {
        Map map = MainPanel.map;
        StatisticsLogic.predator = numberOfPredators;
        int countOfPredators = numberOfPredators;
        int randomX = 0, randomY = 0;
        while (countOfPredators > 0) {
            randomX = MIN_X + random.nextInt(MAX_X - MIN_X + 1);
            randomY = MIN_Y + random.nextInt(MAX_Y - MIN_Y + 1);
            if (map.getElkType(randomY, randomX) == MapCoder.ELK_TYPE_EMPTY
                    && map.getKillerType(randomY, randomX) == MapCoder.KILLER_TYPE_EMPTY
                    && map.getPlantType(randomY, randomX) == MapCoder.PLANT_TYPE_EMPTY
                    && map.getLandscapeType(randomY, randomX) != MapCoder.LANDSCAPE_TYPE_OUTSIDE
                    && map.getLandscapeType(randomY, randomX) != MapCoder.LANDSCAPE_TYPE_WATER) {
                map.setKillerType(random.nextBoolean() ? MapCoder.KILLER_TYPE_PREDATOR_MALE : MapCoder.KILLER_TYPE_PREDATOR_FEMALE, randomY, randomX);
                map.setCreatureEnergy(63, randomY, randomX);
                map.setCreatureAge(BasicLogic.randomIntegerInRange(1, 1620), randomY, randomX);
                countOfPredators--;
            }
        }
    }
}
