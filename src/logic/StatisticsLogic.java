package logic;

import graphics.MainPanel;
import graphics.Map.Map;
import graphics.Map.MapCoder;

public class StatisticsLogic {

    public static final int CELLS       = 4225;             // Количество всех ячеек карты
    public static final int CELLS_ALTAY       = 1911;       // Количество ячеек Алтайского края
    public static final int CELLS_ALTAY_LAND  = 1666;       // Количество ячеек Алтайского края, не считая водных ресурсов
    public static int elk = 0;                              // Количество лосей на карте
    public static int elkMale = 0;                          // женского пола
    public static int elkFemale = 0;                        // мужского пола
    public static int elkPregnant = 0;                      // Количество беременных лосей
    public static int elkDied = 0 ;                         // Количество умерших лосей
    public static int elkAge = 0 ;                          // Суммарный возраст лосей
    public static int predator = 0;                         // Количество хищников
    public static int predatorMale = 0;                     // мужского пола
    public static int predatorFemale = 0;                   // женского пола
    public static int predatorPregnant = 0;                 // Количество беременных хищников
    public static int predatorDied = 0;                     // Количество умерших хишников
    public static int elkDiedByEnergy = 0;                  // Количество лосей, умерших от недостатка энергии
    public static int predatorDiedByEnergy = 0;             // Количество хищников, умерших от недостатка энергии
    public static int elkDiedByHunger = 0;                  // Количество лосей, умерших от голода
    public static int predatorDiedByHunger = 0;             // Количество хищников, умерших от голода
    public static int elkDiedByAge = 0;                     // Количество лосей, умерших от старости
    public static int predatorDiedByAge = 0;                // Количество хищников, умерших от старости
    public static int elkDiedByPredator = 0;                // Количество лосей, умерших от нападения хищника
    public static int elkDiedByHunter = 0;                  // Количество лосей, умерших от рук охотника
    public static int predatorDiedByHunter = 0;             // Количество хищников, умерших от рук охотника
    public static int elkDiedByHunterForSeason = 0;         // Количество лосей, убитых охотниками за сезон
    public static int predatorDiedByHunterForSeason = 0;    // Количество хищников, убитых охотниками за сезон
    public static int limitOfHuntingElkForSeason = 0;       // Лимит добычи лосей (кол-во особей за сезон)
    public static int limitOfHuntingPredatorForSeason = 0;  // Лимит добычи хищников (кол-во особей за сезон)
    public static int babyElkWereBorn = 0;                  // Количество родившихся лосят
    public static int babyPredatorWereBorn = 0;             // Количество родившихся детёнышей хищника
    public static int babyElkDied = 0;                      // Количество лосят, которые не смогли родиться
    public static int babyPredatorDied = 0;                 // Количество детёнышей хищника, которые не смогли родиться
    public static int elkMigrated = 0;                      // Количество лосей, ушедших далеко от Алтайского края
    public static int predatorMigrated = 0;                 // Количество хишников, ушедших далеко от Алтайского края
    public static int hunter = 0;                           // Количество охотников
    public static int plantsFood = 0;                       // Доля съедобных растений на карте

    // Метод считает актуальное количество животных и растений на карте
    public static void update() {
        Map map = MainPanel.map;
        int elkTemp = 0;
        int elkMaleTemp = 0;
        int elkFemaleTemp = 0;
        int elkPregnantTemp = 0;
        int elkAgeTemp = 0;
        int predatorTemp = 0;
        int predatorMaleTemp = 0;
        int predatorFemaleTemp = 0;
        int predatorPregnantTemp = 0;
        int plantsFoodTemp = 0;
        for (int y = 0; y < Map.MAP_SIZE; y++) {
            for (int x = 0; x < Map.MAP_SIZE; x++) {
                if (map.getElkType(y, x) != MapCoder.ELK_TYPE_EMPTY) {
                    elkTemp++;
                    elkAgeTemp += map.getCreatureAge(y, x);
                    if (map.getElkType(y, x) == MapCoder.ELK_TYPE_MALE) {
                        elkMaleTemp++;
                    }
                    else if (map.getElkType(y, x) == MapCoder.ELK_TYPE_FEMALE) {
                        elkFemaleTemp++;
                        if (map.getCreaturePregnancy(y, x) != 0) {
                            elkPregnantTemp++;
                        }
                    }
                }
                else if (map.getKillerType(y, x) != MapCoder.KILLER_TYPE_EMPTY
                    && map.getKillerType(y, x) != MapCoder.KILLER_TYPE_HUNTER) {
                    predatorTemp++;
                    if (map.getKillerType(y, x) == MapCoder.KILLER_TYPE_PREDATOR_MALE) {
                        predatorMaleTemp++;
                    }
                    else if (map.getKillerType(y, x) == MapCoder.KILLER_TYPE_PREDATOR_FEMALE) {
                        predatorFemaleTemp++;
                        if (map.getCreaturePregnancy(y, x) != 0) {
                            predatorPregnantTemp++;
                        }
                    }
                }
                if (map.getPlantType(y, x) != MapCoder.PLANT_TYPE_EMPTY) {
                    plantsFoodTemp += map.getPlantFood(y, x);
                }
            }
        }
        elk = elkTemp;
        elkMale = elkMaleTemp;
        elkFemale = elkFemaleTemp;
        elkPregnant = elkPregnantTemp;
        elkAge = elkAgeTemp;
        predator = predatorTemp;
        predatorMale = predatorMaleTemp;
        predatorFemale = predatorFemaleTemp;
        predatorPregnant = predatorPregnantTemp;
        plantsFood = plantsFoodTemp;
    }

    public static void reset() {
        elk = 0;
        elkMale = 0;
        elkFemale = 0;
        elkPregnant = 0;
        elkDied = 0 ;
        elkAge = 0 ;
        predator = 0;
        predatorMale = 0;
        predatorFemale = 0;
        predatorPregnant = 0;
        predatorDied = 0;
        elkDiedByEnergy = 0;
        predatorDiedByEnergy = 0;
        elkDiedByHunger = 0;
        predatorDiedByHunger = 0;
        elkDiedByAge = 0;
        predatorDiedByAge = 0;
        elkDiedByPredator = 0;
        elkDiedByHunter = 0;
        predatorDiedByHunter = 0;
        elkDiedByHunterForSeason = 0;
        predatorDiedByHunterForSeason = 0;
        babyElkWereBorn = 0;
        babyPredatorWereBorn = 0;
        babyElkDied = 0;
        babyPredatorDied = 0;
        elkMigrated = 0;
        predatorMigrated = 0;
        hunter = 0;
        plantsFood = 0;

    }

    // Метод возвращает, какую часть территории Алтайского края заселяет популяция лосей
    public static int getElkPopulationDensity() { return elk * 100 / CELLS_ALTAY; }
    // Метод возвращает средний возраст популяции
    public static int getAverageElkAge() { return elk != 0 ? elkAge / elk : 0; }
    // Метод возвращает коэффициент, оценивающий обеспеченность каждой особи лося пищей
    public static int getFoodSupply() { return plantsFood != 0 && elk != 0 ? plantsFood / 63 * 100 / elk : 0; }
}
