package model;

public class CharacterSheetBuilder extends AbstractCharacterBuilder<String>{

    @Override
    public String build() {
        validate();
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