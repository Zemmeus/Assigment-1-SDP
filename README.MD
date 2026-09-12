# Assignment #1 — Builder Pattern

**Course:** Software Design Patterns
**Language:** Java 11

## 1. What is being built

The product is a **game character** (an RPG hero) — the kind of object you configure
on a character-creation screen: a name, a race, a class, three primary stats, a level,
and a set of equipped items, where each item carries its own list of stat modifiers
(`+5 STRENGTH`, `+12 SHADOW_RESISTANCE`, `-2 INTELLECT`).

### Why this domain needs the Builder pattern

A character has 8 fields. Only a few of them are conceptually mandatory, the rest are
optional or have sensible defaults, and four of them are plain `int` values sitting next
to each other. Constructing one directly looks like this:

```java
new Character("Ragnar", "Orc", "Warrior", 10, 25, 8, 5, List.of(sword, ring));
```

Nothing in that call says which number is agility, which is strength and which is the
level. Swapping two of them compiles cleanly and produces a silently wrong character.
Adding a ninth field would mean either a longer constructor or a second telescoping
overload.

With the Builder the same character reads as:

```java
new Character.Builder()
        .name("Ragnar")
        .race("Orc")
        .characterClass("Warrior")
        .strength(25)
        .agility(10)
        .intelligence(8)
        .level(5)
        .addItem(sword)
        .addItem(ring)
        .build();
```

Every value is labelled, the order of the calls does not matter, and items are added one
at a time instead of being assembled into a list beforehand.

## 2. Pattern components

| Component | Class | Role |
| --- | --- | --- |
| Product | `model.Character` | Immutable character. All fields are `final`, the constructor is `private`. |
| Builder (abstraction) | `model.CharacterBuilder<T>` | Generic interface: the same construction steps, a result type chosen by the implementation. |
| Concrete builder 1 | `model.Character.Builder` | `CharacterBuilder<Character>` — produces the object. Nested, so it is the only holder of the key to the private constructor. |
| Concrete builder 2 | `model.CharacterSheetBuilder` | `CharacterBuilder<String>` — produces a printable character sheet without ever creating a `Character`. |
| Director | `director.CharacterDirector` | Knows the archetype recipes: `createWarrior`, `createWarlock`, `createRogue`. |
| Client | `Main` | Demonstrates the director, manual construction, both representations and failing validation. |

### Multiple representations

`CharacterDirector` does not mention the `Character` type anywhere. Its methods are
declared as `<T> T createWarrior(String name, CharacterBuilder<T> builder)`, so the same
recipe — the same ordered sequence of steps — produces different results depending on the
builder it is handed:

```java
Character warlock = director.createWarlock("Zemmeus", new Character.Builder());
String   sheet    = director.createWarlock("Zemmeus", new CharacterSheetBuilder());
```

The second call never constructs a `Character` at all: the builder accumulates the steps
and renders text directly in `build()`.

### Why the object builder is nested

`Character`'s constructor is `private`. A nested class has access to the private members
of its enclosing class, so `Character.Builder` can call it while nothing outside can. This
is what makes the guarantee real: **every existing `Character` has passed validation**,
because there is no other way to create one.

`Item` deliberately has no builder — three fields, none optional, no step-by-step
construction problem to solve. The pattern is applied where it removes pain, not
everywhere.

## 3. Clean Code principles applied

### 3.1 Meaningful, intention-revealing names

The builder methods were first written as JavaBean-style setters, which reads like a
sequence of assignments rather than a description of the object:

```java
// before
public Builder setName(String name) { ... }
public Builder setTimeoutMinutes(int t) { ... }
```

```java
// after
public Builder name(String name) { ... }
public Builder level(int level) { ... }
```

The `set` prefix also actively misleads in a fluent API: a `setX` method is expected to
return `void`, while these return the builder.

The same applies to the collection method. `setEquipment(List<Item>)` would force the
caller to assemble the list first, which defeats step-by-step construction; `addItem(Item)`
states exactly what one call does.

### 3.2 Small methods, each doing one thing

`build()` is deliberately two lines — validate, then construct:

```java
public Character build() {
    validate();
    return new Character(name, race, characterClass, agility, strength, intelligence, level, equipment);
}
```

`validate()` itself started as one long method holding field checks, the level range, the
stat checks and the whole equipment-counting loop:

```java
// before
private void validate() {
    if (name == null || name.trim().isEmpty()) { ... }
    if (race == null || race.trim().isEmpty()) { ... }
    if (characterClass == null || characterClass.trim().isEmpty()) { ... }
    if (level < 1 || level > 60) { ... }
    if (agility < 0) { ... }
    if (strength < 0) { ... }
    if (intelligence < 0) { ... }

    Map<EquipmentSlot, Integer> equipmentMap = new HashMap<>();
    for (Item item : equipment) {
        equipmentMap.merge(item.getSlot(), 1, Integer::sum);
    }
    for (Map.Entry<EquipmentSlot, Integer> entry : equipmentMap.entrySet()) {
        ...
    }
}
```

After extracting the repeated checks and the equipment rule, it became a readable list of
what is being enforced:

```java
// after
private void validate() {
    requireText(this.name, "Character name");
    requireText(this.race, "Race");
    requireText(this.characterClass, "Character class");

    if (this.level < MIN_LEVEL || this.level > MAX_LEVEL) {
        throw new IllegalArgumentException(
                "Invalid level: " + this.level + ", must be between " + MIN_LEVEL + " and " + MAX_LEVEL);
    }

    requireNonNegative(this.agility, "Agility");
    requireNonNegative(this.strength, "Strength");
    requireNonNegative(this.intelligence, "Intelligence");

    validateEquipment();
}
```

The same reasoning drove the two extracted helpers below — each performs one check and
knows nothing about the rest of the object.

### 3.3 No duplicated logic

Three identical null-or-blank checks differing only in the field name were collapsed into
a single helper that also produces a field-specific message:

```java
private void requireText(String value, String fieldName) {
    if (value == null || value.trim().isEmpty()) {
        throw new IllegalStateException(fieldName + " is required");
    }
}

private void requireNonNegative(int value, String fieldName) {
    if (value < 0) {
        throw new IllegalArgumentException(fieldName + " must not be negative, but was " + value);
    }
}
```

A related fix: the first version of the stat check merged three fields into one condition,
`if (agility < 0 || strength < 0 || intelligence < 0)`, throwing
`"Invalid agility or strength or intelligence"` — a message that names three candidates and
points at none of them. Splitting it produces `"Strength must not be negative, but was -5"`.

### 3.4 Validated construction

`build()` cannot return an invalid object. The builder distinguishes two failure kinds
deliberately:

- `IllegalStateException` — the builder is in an unusable state: a mandatory step was never
  called, or the accumulated equipment breaks a rule.
- `IllegalArgumentException` — a value was supplied but is not admissible (a level of 200, a
  negative stat).

The equipment rule is the non-trivial one. Items are counted per slot, then each count is
compared against the capacity the slot declares about itself:

```java
private void validateEquipment() {
    Map<EquipmentSlot, Integer> equipmentMap = new HashMap<>();
    for (Item item : this.equipment) {
        equipmentMap.merge(item.getSlot(), 1, Integer::sum);
    }

    for (Map.Entry<EquipmentSlot, Integer> entry : equipmentMap.entrySet()) {
        EquipmentSlot slot = entry.getKey();
        int equipped = entry.getValue();

        if (equipped > slot.getMaxEquipped()) {
            throw new IllegalStateException(
                    "Slot " + slot + " allows at most " + slot.getMaxEquipped()
                            + " item(s), but " + equipped + " were equipped");
        }
    }
}
```

Two rings pass, three do not; two chest pieces never do — and the rule is expressed once,
for all sixteen slots, without a single slot-specific branch.

### 3.5 No magic numbers or strings

Level bounds are named constants rather than literals buried in a condition:

```java
private static final int MIN_LEVEL = 1;
private static final int MAX_LEVEL = 60;
```

Slot capacity is not a constant at all — it is a property each slot carries, so the
knowledge lives where it belongs instead of being spread across `if` statements:

```java
public enum EquipmentSlot {
    HEAD(1), CHEST(1), /* ... */ FINGER(2), TRINKET(2), MAIN_HAND(1), OFF_HAND(1);

    private final int maxEquipped;

    EquipmentSlot(int maxEquipped) {
        this.maxEquipped = maxEquipped;
    }

    public int getMaxEquipped() {
        return maxEquipped;
    }
}
```

Stat kinds are an enum too. `new ItemModifier(StatType.SHADOW_RESISTANCE, 12)` is checked at
compile time; a string `"shadow resistance"` would not be, and a typo would surface only at
runtime — if at all.

### 3.6 Small, focused classes

Each type has exactly one reason to exist: `ItemModifier` is a single stat bonus, `Item` is a
named piece of equipment carrying modifiers, `Character` holds character state and renders
itself, `Character.Builder` assembles and validates, `CharacterSheetBuilder` renders text,
`CharacterDirector` knows recipes. The director never touches a private field; the product
never formats a sheet; the builders never decide what a warrior looks like.

Immutability is enforced rather than assumed: fields are `final`, there are no setters, and
collections are defensively copied with `List.copyOf(...)` in the constructor, so a caller
holding the original list cannot mutate a finished character.

## 4. Project structure

```
src/
├── Main.java                          Client
├── director/
│   └── CharacterDirector.java         Director — archetype recipes
└── model/
    ├── Character.java                 Product + nested object builder
    ├── CharacterBuilder.java          Generic builder interface
    ├── CharacterSheetBuilder.java     Text-representation builder
    ├── Item.java
    ├── ItemModifier.java
    ├── StatType.java
    └── EquipmentSlot.java
```

## 5. Running

```
javac -d out $(find src -name "*.java")
java -cp out Main
```

`Main` demonstrates, in order: a character built from a director recipe, the same recipe
rendered as a text sheet, a manually built character, and two validation failures caught and
reported.