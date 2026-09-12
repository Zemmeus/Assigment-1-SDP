import model.Character;
import model.EquipmentSlot;
import model.Item;
import model.ItemModifier;
import model.StatType;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Item sword = new Item(
                "Sword of Dawn",
                EquipmentSlot.MAIN_HAND,
                List.of(
                        new ItemModifier(StatType.STRENGTH, 5),
                        new ItemModifier(StatType.CRIT_CHANCE, 3)
                )
        );

        Item ring = new Item(
                "Ring of Shadows",
                EquipmentSlot.FINGER,
                List.of(
                        new ItemModifier(StatType.SHADOW_RESISTANCE, 12),
                        new ItemModifier(StatType.INTELLECT, -2)
                )
        );



        Character warlock = new Character.Builder()
                .name("Zemmeus")
                .race("Undead")
                .characterClass("Warlock")
                .strength(25)
                .agility(10)
                .intelligence(8)
                .level(5)
                .addItem(sword)
                .addItem(ring)
                .build();

        System.out.println(warlock);


        try {
            Character warrior = new Character.Builder()
                    .name("")
                    .race("Orc")
                    .characterClass("Warrior")
                    .strength(25)
                    .agility(10)
                    .intelligence(8)
                    .level(5)
                    .addItem(sword)
                    .addItem(ring)
                    .build();

            System.out.println(warrior);
        } catch (IllegalStateException e) {
            System.out.println("Expected validation error: " + e.getMessage());
        }

    }
}