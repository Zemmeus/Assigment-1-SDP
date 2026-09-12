package model;

import java.util.ArrayList;
import java.util.List;

public class CharacterSheetBuilder implements CharacterBuilder<String> {
    private String name;
    private String race;
    private String characterClass;
    private int agility;
    private int strength;
    private int intelligence;
    private int level;
    private final List<Item> equipment = new ArrayList<>();

    @Override
    public CharacterSheetBuilder name(String name) {
        this.name = name;
        return this;
    }

    @Override
    public CharacterSheetBuilder race(String race) {
        this.race = race;
        return this;
    }

    @Override
    public CharacterSheetBuilder characterClass(String characterClass) {
        this.characterClass = characterClass;
        return this;
    }

    @Override
    public CharacterSheetBuilder agility(int agility) {
        this.agility = agility;
        return this;
    }

    @Override
    public CharacterSheetBuilder strength(int strength) {
        this.strength = strength;
        return this;
    }

    @Override
    public CharacterSheetBuilder intelligence(int intelligence) {
        this.intelligence = intelligence;
        return this;
    }

    @Override
    public CharacterSheetBuilder level(int level) {
        this.level = level;
        return this;
    }

    @Override
    public CharacterSheetBuilder addItem(Item item) {
        this.equipment.add(item);
        return this;
    }

    @Override
    public String build() {
        StringBuilder sheet = new StringBuilder();
        sheet.append("========================================\n");
        sheet.append("  ").append(name).append(" the ").append(characterClass).append('\n');
        sheet.append("  ").append(race).append(", level ").append(level).append('\n');
        sheet.append("========================================\n");
        sheet.append(" STR ").append(strength);
        sheet.append("  |  AGI ").append(agility);
        sheet.append("  |  INT ").append(intelligence).append('\n');
        sheet.append("----------------------------------------\n");

        if (equipment.isEmpty()) {
            sheet.append(" No equipment\n");
        } else {
            for (Item item : equipment) {
                sheet.append(" ").append(item).append('\n');
            }
        }
        sheet.append("========================================\n");
        return sheet.toString();
    }
}