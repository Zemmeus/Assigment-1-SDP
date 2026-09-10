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

        Character warrior = new Character(
                "Ragnar",
                "Orc",
                "Warrior",
                10,
                25,
                8,
                5,
                List.of(sword, ring)
        );

        System.out.println(warrior);
    }
}