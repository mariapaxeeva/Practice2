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

    private int numberOfElks;
    private int numberOfHunters;
    private int numberOfPredators;

    ParametersSetter() {
        numberOfElks = MainPanel.parameters.getValueElksSlider();
        numberOfHunters = MainPanel.parameters.getValueHuntersSlider();
        numberOfPredators = MainPanel.parameters.getValuePredatorsSlider();
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
                if (random.nextBoolean()) { map.setElkType(MapCoder.ELK_TYPE_MALE, randomY, randomX); }
                else { map.setElkType(MapCoder.ELK_TYPE_FEMALE, randomY, randomX); }
                map.setCreatureEnergy(63, randomY, randomX);
                countOfElks--;
            }
        }
    }

    public void setNumberOfHunters() {
        Map map = MainPanel.map;
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

    public void setLimitOfHuntingElk() {

    }

    public void setLimitOfHuntingPredator() {

    }

    public void setNumberOfPredators() {
        Map map = MainPanel.map;
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
                if (random.nextBoolean()) { map.setKillerType(MapCoder.KILLER_TYPE_PREDATOR_MALE, randomY, randomX); }
                else { map.setKillerType(MapCoder.KILLER_TYPE_PREDATOR_FEMALE, randomY, randomX); }
                map.setCreatureEnergy(63, randomY, randomX);
                countOfPredators--;
            }
        }
    }
}
