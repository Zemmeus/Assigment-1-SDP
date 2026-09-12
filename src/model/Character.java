package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Character {
    private final String name;
    private final String race;
    private final String characterClass;
    private final int agility;
    private final int strength;
    private final int intelligence;
    private final int level;
    private final List<Item> equipment;

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 60;

    private Character(String name, String race, String characterClass, int agility, int strength, int intelligence, int level, List<Item> equipment) {
        this.name = name;
        this.race = race;
        this.characterClass = characterClass;
        this.agility = agility;
        this.strength = strength;
        this.intelligence = intelligence;
        this.level = level;
        this.equipment = List.copyOf(equipment);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Name: ").append(this.name).append('\n');
        stringBuilder.append("Race: ").append(this.race).append('\n');
        stringBuilder.append("Class: ").append(this.characterClass).append('\n');
        stringBuilder.append("Level: ").append(this.level).append('\n');
        stringBuilder.append("Stats: ").append('\n');
        stringBuilder.append("   - Agility: ").append(this.agility).append('\n');
        stringBuilder.append("   - Strength: ").append(this.strength).append('\n');
        stringBuilder.append("   - Intellect: ").append(this.intelligence).append('\n');

        stringBuilder.append("Equipment: ").append('\n');
        for (Item item : this.equipment) {
            stringBuilder.append("   - ").append(item).append('\n');
        }

        return stringBuilder.toString();


    }

    public static class Builder {
        private String name;
        private String race;
        private String characterClass;
        private int agility;
        private int strength;
        private int intelligence;
        private int level;
        private final List<Item> equipment = new ArrayList<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder race(String race) {
            this.race = race;
            return this;
        }

        public Builder characterClass(String characterClass) {
            this.characterClass = characterClass;
            return this;
        }

        public Builder agility(int agility) {
            this.agility = agility;
            return this;
        }

        public Builder strength(int strength) {
            this.strength = strength;
            return this;
        }

        public Builder intelligence(int intelligence) {
            this.intelligence = intelligence;
            return this;
        }

        public Builder level(int level) {
            this.level = level;
            return this;
        }

        public Builder addItem(Item item) {
            this.equipment.add(item);
            return this;
        }

        private void requireText(String value, String fieldName) {
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalStateException(fieldName + " is required");
            }
        }

        private void requireNonNegative(int value, String fieldName) {
            if (value < 0) {
                throw new IllegalArgumentException(fieldName + " must not be negative, but was " + value);
            }
        }

        private void validateEquipment() {
            Map<EquipmentSlot, Integer> equipmentMap = new HashMap<>();

            for (Item item : this.equipment) {
                equipmentMap.merge(item.getSlot(), 1, Integer::sum);
            }

            for (Map.Entry<EquipmentSlot, Integer> entry : equipmentMap.entrySet()) {
                EquipmentSlot slot = entry.getKey();
                int equipped = entry.getValue();

                if (equipped > slot.getMaxEquipped()) {
                    throw new IllegalStateException(
                            "Slot " + slot + " allows at most " + slot.getMaxEquipped() + " item(s), but " + equipped + " were equipped"
                    );
                }
            }
        }

        private void validate() {
            requireText(this.name, "Character Name");
            requireText(this.race, "Race");
            requireText(this.characterClass, "Character class");

            if (this.level < MIN_LEVEL || this.level > MAX_LEVEL) {
                throw new IllegalArgumentException("Invalid level: " + this.level + " must be between " + MIN_LEVEL + " and " + MAX_LEVEL);
            }

            requireNonNegative(this.agility, "Agility");
            requireNonNegative(this.strength, "Strength");
            requireNonNegative(this.intelligence, "Intelligence");

            validateEquipment();

        }

        public Character build() {
            validate();
            return new Character(name, race, characterClass, agility, strength, intelligence, level, equipment);

        }
    }
}
