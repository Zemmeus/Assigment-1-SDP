import director.CharacterDirector;
import model.Character;
import model.CharacterSheetBuilder;
import model.EquipmentSlot;
import model.Item;
import model.ItemModifier;
import model.StatType;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        CharacterDirector director = new CharacterDirector();

        System.out.println("=== 1. Built from a director recipe ===\n");
        Character warlock = director.createWarlock("Zemmeus", new Character.Builder());
        System.out.println(warlock);

        System.out.println("\n=== 2. Same recipe, different representation ===\n");
        String sheet = director.createWarlock("Zemmeus", new CharacterSheetBuilder());
        System.out.println(sheet);

        System.out.println("\n=== 3. Built manually, without a director ===\n");
        Item dagger = new Item(
                "Nightfall Kris",
                EquipmentSlot.MAIN_HAND,
                List.of(
                        new ItemModifier(StatType.AGILITY, 21),
                        new ItemModifier(StatType.CRIT_CHANCE, 5)
                )
        );

        Item ring = new Item(
                "Signet of Shadows",
                EquipmentSlot.FINGER,
                List.of(
                        new ItemModifier(StatType.AGILITY, 7),
                        new ItemModifier(StatType.SHADOW_RESISTANCE, 10)
                )
        );

        Character custom = new Character.Builder()
                .name("Ragnar")
                .race("Orc")
                .characterClass("Warrior")
                .strength(25)
                .agility(10)
                .intelligence(8)
                .level(5)
                .addItem(dagger)
                .addItem(ring)
                .build();

        System.out.println(custom);

        System.out.println("\n=== 4. Validation prevents invalid characters ===\n");

        try {
            new Character.Builder()
                    .race("Orc")
                    .characterClass("Warrior")
                    .level(5)
                    .build();
        } catch (IllegalStateException e) {
            System.out.println("Missing field  -> " + e.getMessage());
        }

        try {
            new Character.Builder()
                    .name("Ragnar")
                    .race("Orc")
                    .characterClass("Warrior")
                    .level(200)
                    .build();
        } catch (IllegalArgumentException e) {
            System.out.println("Bad value      -> " + e.getMessage());
        }

        try {
            new Character.Builder()
                    .name("Ragnar")
                    .race("Orc")
                    .characterClass("Warrior")
                    .level(5)
                    .addItem(ring)
                    .addItem(ring)
                    .addItem(ring)
                    .build();
        } catch (IllegalStateException e) {
            System.out.println("Slot overflow  -> " + e.getMessage());
        }
    }
}