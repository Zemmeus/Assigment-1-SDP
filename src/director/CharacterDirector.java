package director;

import model.Character;
import model.EquipmentSlot;
import model.Item;
import model.ItemModifier;
import model.StatType;

import java.util.List;

public class CharacterDirector {

    public Character createWarlock(String name) {
        Item staff = new Item(
                "Staff of Eternal Torment",
                EquipmentSlot.MAIN_HAND,
                List.of(
                        new ItemModifier(StatType.INTELLECT, 18),
                        new ItemModifier(StatType.SPELL_DAMAGE, 42)
                )
        );

        Item robe = new Item(
                "Felcloth Robe",
                EquipmentSlot.CHEST,
                List.of(
                        new ItemModifier(StatType.INTELLECT, 12),
                        new ItemModifier(StatType.SHADOW_RESISTANCE, 20),
                        new ItemModifier(StatType.STAMINA, 9)
                )
        );

        Item trinket = new Item(
                "Soul Harvester",
                EquipmentSlot.TRINKET,
                List.of(
                        new ItemModifier(StatType.SPELL_DAMAGE, 25),
                        new ItemModifier(StatType.CRIT_CHANCE, 2)
                )
        );

        return new Character.Builder()
                .name(name)
                .race("Undead")
                .characterClass("Warlock")
                .intelligence(24)
                .agility(8)
                .strength(5)
                .level(60)
                .addItem(staff)
                .addItem(robe)
                .addItem(trinket)
                .build();
    }

    public Character createWarrior(String name) {
        Item axe = new Item(
                "Bloodreaver Axe",
                EquipmentSlot.MAIN_HAND,
                List.of(
                        new ItemModifier(StatType.STRENGTH, 26),
                        new ItemModifier(StatType.CRIT_CHANCE, 3)
                )
        );

        Item shield = new Item(
                "Bulwark of the Fallen",
                EquipmentSlot.OFF_HAND,
                List.of(
                        new ItemModifier(StatType.STAMINA, 22),
                        new ItemModifier(StatType.STRENGTH, 7)
                )
        );

        Item plate = new Item(
                "Ironhide Breastplate",
                EquipmentSlot.CHEST,
                List.of(
                        new ItemModifier(StatType.STAMINA, 18),
                        new ItemModifier(StatType.STRENGTH, 11),
                        new ItemModifier(StatType.SHADOW_RESISTANCE, 5)
                )
        );

        return new Character.Builder()
                .name(name)
                .race("Orc")
                .characterClass("Warrior")
                .strength(28)
                .agility(12)
                .intelligence(4)
                .level(60)
                .addItem(axe)
                .addItem(shield)
                .addItem(plate)
                .build();
    }

    public Character createRogue(String name) {
        Item mainDagger = new Item(
                "Nightfall Kris",
                EquipmentSlot.MAIN_HAND,
                List.of(
                        new ItemModifier(StatType.AGILITY, 21),
                        new ItemModifier(StatType.CRIT_CHANCE, 5)
                )
        );

        Item offDagger = new Item(
                "Whisper of the Alley",
                EquipmentSlot.OFF_HAND,
                List.of(
                        new ItemModifier(StatType.AGILITY, 14),
                        new ItemModifier(StatType.CRIT_CHANCE, 3)
                )
        );

        Item firstRing = new Item(
                "Band of Swift Hands",
                EquipmentSlot.FINGER,
                List.of(new ItemModifier(StatType.AGILITY, 9))
        );

        Item secondRing = new Item(
                "Signet of Shadows",
                EquipmentSlot.FINGER,
                List.of(
                        new ItemModifier(StatType.AGILITY, 7),
                        new ItemModifier(StatType.SHADOW_RESISTANCE, 10)
                )
        );

        return new Character.Builder()
                .name(name)
                .race("Night Elf")
                .characterClass("Rogue")
                .agility(30)
                .strength(14)
                .intelligence(6)
                .level(60)
                .addItem(mainDagger)
                .addItem(offDagger)
                .addItem(firstRing)
                .addItem(secondRing)
                .build();
    }
}