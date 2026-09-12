package model;

import java.util.List;

public class Item {
    private final String name;
    private final EquipmentSlot slot;
    private final List<ItemModifier> modifiers;

    public Item(String name, EquipmentSlot slot, List<ItemModifier> modifiers) {
        this.name = name;
        this.slot = slot;
        this.modifiers = List.copyOf(modifiers);
    }

    public EquipmentSlot getSlot() {
        return slot;
    }

    @Override
    public String toString() {
        return name  + " " + slot+ " " + modifiers;
    }
}
