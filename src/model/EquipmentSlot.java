package model;

public enum EquipmentSlot {
    HEAD(1),
    CHEST(1),
    LEGS(1),
    FEET(1),
    NECK(1),
    SHOULDERS(1),
    WRIST(1),
    BACK(1),
    HANDS(1),
    WAIST(1),
    FINGER(2),
    TRINKET(2),
    MAIN_HAND(1),
    OFF_HAND(1),
    TABARD(1),
    SHIRT(1);

    private final int maxEquipped;

    EquipmentSlot(int maxEquipped) {
        this.maxEquipped = maxEquipped;
    }

    public int getMaxEquipped() {
        return maxEquipped;
    }
}