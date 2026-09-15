package model;

import java.util.List;

public class Character {
    private final String name;
    private final String race;
    private final String characterClass;
    private final int agility;
    private final int strength;
    private final int intelligence;
    private final int level;
    private final List<Item> equipment;

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

    public static class Builder extends AbstractCharacterBuilder<Character> {

        public Character build() {
            validate();
            return new Character(name, race, characterClass, agility, strength, intelligence, level, equipment);

        }
    }
}
