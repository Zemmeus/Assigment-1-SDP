package model;

public class ItemModifier {
    private final StatType statType;
    private final int value;

    public ItemModifier(StatType statType, int value) {
        this.statType = statType;
        this.value = value;
    }

    @Override
    public String toString() {
        return (this.value > 0 ? "+" : "") + this.value + " " + this.statType;
    }
}
