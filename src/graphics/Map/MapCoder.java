package graphics.Map;

// Класс, реализующий битовое представление информации о каждой ячейке карты
public class MapCoder {
    // Типы ландшафта
    private final static long LANDSCAPE_TYPE_MASK       = 0x0000_0000_0000_0007L;
    private final static int LANDSCAPE_TYPE_SHIFT       = 0;
    public final static int LANDSCAPE_TYPE_EMPTY        = 0x0;
    public final static int LANDSCAPE_TYPE_WATER        = 0x1;
    public final static int LANDSCAPE_TYPE_PLAIN        = 0x2;
    public final static int LANDSCAPE_TYPE_HILL         = 0x3;
    public final static int LANDSCAPE_TYPE_MOUNTAIN     = 0x4;
    public final static int LANDSCAPE_TYPE_GRASS_PLAIN  = 0x5;
    public final static int LANDSCAPE_TYPE_OUTSIDE      = 0x6;
    // Типы лосей
    private final static long ELK_TYPE_MASK             = 0x0000_0000_0000_0018L;
    private final static int ELK_TYPE_SHIFT             = 3;
    public final static int ELK_TYPE_EMPTY              = 0x0;
    public final static int ELK_TYPE_MALE               = 0x1;
    public final static int ELK_TYPE_FEMALE             = 0x2;
    // Возраст лосей
    private final static long ELK_AGE_MASK              = 0x0000_0000_0007_FFE0L;
    private final static int ELK_AGE_SHIFT              = 5;
    // Энергия лосей
    private final static long ELK_ENERGY_MASK           = 0x0000_0000_01F8_0000L;
    private final static int ELK_ENERGY_SHIFT           = 19;
    // Голод лосей
    private final static long ELK_HUNGER_MASK           = 0x0000_0000_7E00_0000L;
    private final static int ELK_HUNGER_SHIFT           = 25;
    // Беременность лосей
    private final static long ELK_PREGNANCY_MASK        = 0x0000_007F_8000_0000L;
    private final static int ELK_PREGNANCY_SHIFT        = 31;
    // Типы растений
    private final static long PLANT_TYPE_MASK           = 0x0000_0080_0000_0000L;
    private final static int PLANT_TYPE_SHIFT           = 39;
    public final static int PLANT_TYPE_EMPTY            = 0x0;
    public final static int PLANT_TYPE_PLANT            = 0x1;
    // Количество съедобных растений
    private final static long PLANT_FOOD_MASK           = 0x0000_3F00_0000_0000L;
    private final static int PLANT_FOOD_SHIFT           = 40;
    // Типы врагов
    private final static long KILLER_TYPE_MASK          = 0x0000_C000_0000_0000L;
    private final static int KILLER_TYPE_SHIFT          = 46;
    public final static int KILLER_TYPE_EMPTY           = 0x0;
    public final static int KILLER_TYPE_HUNTER          = 0x1;
    public final static int KILLER_TYPE_PREDATOR        = 0x2;
    // Флаг активности ячейки (возможность совершения действий)
    private final static long ACTIVE_FLAG_ELK_MASK      = 0x4000_0000_0000_0000L;
    private final static int ACTIVE_FLAG_ELK_SHIFT      = 62;
    private final static long ACTIVE_FLAG_PLANT_MASK    = 0x8000_0000_0000_0000L;
    private final static int ACTIVE_FLAG_PLANT_SHIFT    = 63;

    // uc - данная последовательность битов, u - биты, которые требуется записать
    public final static long encodeLandscapeType(long uc, int u) {
        return encode(uc, u, LANDSCAPE_TYPE_MASK, LANDSCAPE_TYPE_SHIFT);
    }

    public final static int decodeLandscapeType(long uc) {
        return decode(uc, LANDSCAPE_TYPE_MASK, LANDSCAPE_TYPE_SHIFT);
    }

    public final static long encodeElkType(long uc, int u) {
        return encode(uc, u, ELK_TYPE_MASK, ELK_TYPE_SHIFT);
    }

    public final static int decodeElkType(long uc) {
        return decode(uc, ELK_TYPE_MASK, ELK_TYPE_SHIFT);
    }

    public final static long encodeElkAge(long uc, int u) {
        return encode(uc, u, ELK_AGE_MASK, ELK_AGE_SHIFT);
    }

    public final static int decodeElkAge(long uc) {
        return decode(uc, ELK_AGE_MASK, ELK_AGE_SHIFT);
    }

    public final static long encodeElkEnergy(long uc, int u) {
        return encode(uc, u, ELK_ENERGY_MASK, ELK_ENERGY_SHIFT);
    }

    public final static int decodeElkEnergy(long uc) {
        return decode(uc, ELK_ENERGY_MASK, ELK_ENERGY_SHIFT);
    }

    public final static long encodeElkHunger(long uc, int u) {
        return encode(uc, u, ELK_HUNGER_MASK, ELK_HUNGER_SHIFT);
    }

    public final static int decodeElkHunger(long uc) {
        return decode(uc, ELK_HUNGER_MASK, ELK_HUNGER_SHIFT);
    }

    public final static long encodeElkPregnancy(long uc, int u) {
        return encode(uc, u, ELK_PREGNANCY_MASK, ELK_PREGNANCY_SHIFT);
    }

    public final static int decodeElkPregnancy(long uc) {
        return decode(uc, ELK_PREGNANCY_MASK, ELK_PREGNANCY_SHIFT);
    }

    public final static long encodePlantType(long uc, int u) {
        return encode(uc, u, PLANT_TYPE_MASK, PLANT_TYPE_SHIFT);
    }

    public final static int decodePlantType(long uc) {
        return decode(uc, PLANT_TYPE_MASK, PLANT_TYPE_SHIFT);
    }

    public final static long encodePlantFood(long uc, int u) {
        return encode(uc, u, PLANT_FOOD_MASK, PLANT_FOOD_SHIFT);
    }

    public final static int decodePlantFood(long uc) {
        return decode(uc, PLANT_FOOD_MASK, PLANT_FOOD_SHIFT);
    }

    public final static long encodeKillerType(long uc, int u) {
        return encode(uc, u, KILLER_TYPE_MASK, KILLER_TYPE_SHIFT);
    }

    public final static int decodeKillerType(long uc) {
        return decode(uc, KILLER_TYPE_MASK, KILLER_TYPE_SHIFT);
    }

    public final static long encodeActiveFlagElk(long uc, int u) {
        return encode(uc, u, ACTIVE_FLAG_ELK_MASK, ACTIVE_FLAG_ELK_SHIFT);
    }

    public final static int decodeActiveFlagElk(long uc) {
        return decode(uc, ACTIVE_FLAG_ELK_MASK, ACTIVE_FLAG_ELK_SHIFT);
    }

    public final static long encodeActiveFlagPlant(long uc, int u) {
        return encode(uc, u, ACTIVE_FLAG_PLANT_MASK, ACTIVE_FLAG_PLANT_SHIFT);
    }

    public final static int decodeActiveFlagPlant(long uc) {
        return decode(uc, ACTIVE_FLAG_PLANT_MASK, ACTIVE_FLAG_PLANT_SHIFT);
    }

    // Общий для всех полей метод кодирования информации
    private final static long encode(long uc, int u, long mask, int shift) {
        return (uc & ~mask) | ((long) u << shift & mask);
    }

    // Общий для всех полей метод декодирования информации
    private final static int decode(long uc, long mask, int shift) {
        return (int) ((uc & mask) >>> shift);
    }
}
