package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractCharacterBuilder<T> implements CharacterBuilder<T> {
    protected String name, race, characterClass;
    protected int agility, strength, intelligence, level;
    protected final List<Item> equipment = new ArrayList<>();

    protected static final int MIN_LEVEL = 1;
    protected static final int MAX_LEVEL = 60;

    public AbstractCharacterBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public AbstractCharacterBuilder<T> race(String race) {
        this.race = race;
        return this;
    }

    public AbstractCharacterBuilder<T> characterClass(String characterClass) {
        this.characterClass = characterClass;
        return this;
    }

    public AbstractCharacterBuilder<T> agility(int agility) {
        this.agility = agility;
        return this;
    }

    public AbstractCharacterBuilder<T> strength(int strength) {
        this.strength = strength;
        return this;
    }

    public AbstractCharacterBuilder<T> intelligence(int intelligence) {
        this.intelligence = intelligence;
        return this;
    }

    public AbstractCharacterBuilder<T> level(int level) {
        this.level = level;
        return this;
    }

    public AbstractCharacterBuilder<T> addItem(Item item) {
        this.equipment.add(item);
        return this;
    }

    protected void requireText(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalStateException(fieldName + " is required");
        }
    }

    protected void requireNonNegative(int value, String fieldName) {
        if (value < 0) {
            throw new IllegalArgumentException(fieldName + " must not be negative, but was " + value);
        }
    }

    protected void validateEquipment() {
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

    protected void validate() {
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

    public abstract T build();

}
