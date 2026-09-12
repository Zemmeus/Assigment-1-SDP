package model;

public interface CharacterBuilder<T> {
    CharacterBuilder<T> name(String name);
    CharacterBuilder<T> race(String race);
    CharacterBuilder<T> characterClass(String characterClass);
    CharacterBuilder<T> agility(int agility);
    CharacterBuilder<T> strength(int strength);
    CharacterBuilder<T> intelligence(int intelligence);
    CharacterBuilder<T> level(int level);
    CharacterBuilder<T> addItem(Item item);
    T build();
}